package com.redshift.ShadowDarkCalculator.party;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.redshift.ShadowDarkCalculator.actions.Action;
import com.redshift.ShadowDarkCalculator.actions.PerformOneAction;
import com.redshift.ShadowDarkCalculator.actions.spells.*;
import com.redshift.ShadowDarkCalculator.actions.spells.SpellBuilder;
import com.redshift.ShadowDarkCalculator.actions.weapons.WeaponBuilder;
import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.creatures.Label;
import com.redshift.ShadowDarkCalculator.creatures.Player;
import com.redshift.ShadowDarkCalculator.creatures.Stats;
import com.redshift.ShadowDarkCalculator.targets.FocusFireTargetSelector;
import com.redshift.ShadowDarkCalculator.targets.SingleTargetSelector;
import com.redshift.ShadowDarkCalculator.targets.TargetSelectorBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Reads a JSON document describing a party and creates the associated creatures.
 */
public class JsonPartyConfig implements PartyBuilder {

    private final String json;
    private final List<Creature> creatures = new ArrayList<>();

    public JsonPartyConfig(String json) {
        this.json = json;
    }

    @Override
    public PartyBuilder add(Creature creature) {
        creatures.add(creature);
        return this;
    }

    @Override
    public List<Creature> build() {
        if (creatures.isEmpty()) {
            parseJson();
        }
        return creatures;
    }

    private void parseJson() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            JsonNode root = mapper.readTree(json);
            if (root.isArray()) {
                for (JsonNode node : root) {
                    creatures.add(parseCreature(node));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse party JSON", e);
        }
    }

    private Creature parseCreature(JsonNode node) {
        String name = node.path("name").asText();
        int level = node.path("level").asInt();

        JsonNode statsNode = node.path("stats");
        Stats stats = new Stats(
                statsNode.path("strength").asInt(),
                statsNode.path("dexterity").asInt(),
                statsNode.path("constitution").asInt(),
                statsNode.path("intelligence").asInt(),
                statsNode.path("wisdom").asInt(),
                statsNode.path("charisma").asInt()
        );

        int armorClass = node.path("armorClass").asInt();
        int hitPoints = node.path("hitPoints").asInt();

        List<Action> actions = new ArrayList<>();
        JsonNode actionsNode = node.path("actions");
        if (actionsNode.isArray()) {
            for (JsonNode actionNode : actionsNode) {
                actions.add(parseAction(actionNode));
            }
        }

        Action action = actions.size() == 1 ? actions.getFirst() : new PerformOneAction(actions);
        SingleTargetSelector selector = parseSelector(node.path("targetSelector").asText());

        Creature creature = new Player(name, level, stats, armorClass, hitPoints, action, selector);

        JsonNode labelsNode = node.path("labels");
        if (labelsNode.isArray()) {
            for (JsonNode labelNode : labelsNode) {
                try {
                    creature.getLabels().add(Label.valueOf(labelNode.asText()));
                } catch (IllegalArgumentException ignored) {
                    // Unknown labels are ignored
                }
            }
        }

        return creature;
    }

    private Action parseAction(JsonNode node) {
        String type = node.path("type").asText();
        if ("weapon".equalsIgnoreCase(type)) {
            WeaponBuilder builder = WeaponBuilder.valueOf(node.path("weapon").asText());
            int attackBonus = node.path("attackBonus").asInt(0);
            int damageBonus = node.path("damageBonus").asInt(0);
            boolean magical = node.path("magical").asBoolean(false);
            boolean silvered = node.path("silvered").asBoolean(false);
            int priority = node.path("priority").asInt(1);

            return builder.build(attackBonus, damageBonus, magical, silvered, priority);
        } else if ("spell".equalsIgnoreCase(type)) {
            Spell spell = SpellBuilder.valueOf(node.path("name").asText()).build();
            int bonus = node.path("bonus").asInt(0);
            if (bonus != 0) {
                spell.addSpellCheckBonus(bonus);
            }
            if (node.path("advantage").asBoolean(false)) {
                spell.addAdvantage();
            }
            spell.setPriority(node.path("priority").asInt(1));
            return spell;
        }
        throw new IllegalArgumentException("Unknown action type: " + type);
    }

    private SingleTargetSelector parseSelector(String selector) {
        try {
            return TargetSelectorBuilder.valueOf(selector).build();
        } catch (IllegalArgumentException e) {
            return new FocusFireTargetSelector();
        }
    }
}

