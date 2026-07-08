package com.redshift.ShadowDarkCalculator.creatures.players.config;

import com.redshift.ShadowDarkCalculator.actions.Action;
import com.redshift.ShadowDarkCalculator.actions.PerformOneAction;
import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.creatures.Stats;
import com.redshift.ShadowDarkCalculator.creatures.players.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * Creates players by class name.
 */

public final class PlayerFactory {

    @FunctionalInterface
    private interface PlayerConstructor {
        Creature create(String name, int level, Stats stats, int armorClass, int hitPoints, Action action);
    }

    private record Archetype(PlayerConstructor constructor) {
    }

    private static final Map<String, Archetype> REGISTRY = new LinkedHashMap<>();

    static {
        register("commoner", new Archetype(Player::new));
        register("bard", new Archetype(Bard::new));
        register("fighter", new Archetype(Fighter::new));
        register("knight-of-st-ydris", new Archetype(KnightOfStYdris::new));
        register("necromancer", new Archetype(Necromancer::new));
        register("paladin", new Archetype(Paladin::new));
        register("priest", new Archetype(Priest::new));
        register("ranger", new Archetype(Ranger::new));
        register("thief", new Archetype(Thief::new));
        register("warlock", new Archetype(Warlock::new));
        register("witch", new Archetype(Witch::new));
        register("wizard", new Archetype(Wizard::new));
    }

    private PlayerFactory() {
    }

    public static Creature create(String playerClass, String name, int level) {
        final PartyMemberConfig config = new PartyMemberConfig();
        config.setPlayerClass(playerClass);
        config.setName(name);
        config.setLevel(level);
        return create(config);
    }

    public static Creature create(PartyMemberConfig config) {
        final Archetype archetype = archetype(config.getPlayerClass());

        final int level = config.getLevel();

        final Stats stats = config.getStats().toStats();

        final int armorClass = config.getArmorClass();

        final Action action = buildActions(config);

        final int hitPoints = config.getHitPoints();

        final Creature player = archetype.constructor().create(
                config.getName(),
                level,
                stats,
                armorClass,
                hitPoints,
                action
        );

        if (config.isLuckToken()) {
            player.giveLuckToken();
        }

        return player;
    }

    public static Set<String> availableClasses() {
        return new TreeSet<>(REGISTRY.keySet());
    }

    private static Archetype archetype(String playerClass) {
        final Archetype archetype = REGISTRY.get(normalize(playerClass));

        if (archetype == null) {
            throw new IllegalArgumentException("Unknown player class: '" + playerClass + "' - Available: " + availableClasses());
        }

        return archetype;
    }

    private static Action buildActions(PartyMemberConfig config) {
        final List<Action> actions = new ArrayList<>();

        config.getItems().forEach(item -> actions.add(ItemFactory.create(item)));
        config.getWeapons().forEach(weapon -> actions.add(WeaponFactory.create(weapon)));
        config.getSpells().forEach(spell -> actions.add(SpellFactory.create(spell)));

        return actions.size() == 1 ?
                actions.getFirst() :
                new PerformOneAction(actions.toArray(new Action[0]));
    }

    private static void register(String key, Archetype archetype) {
        REGISTRY.put(key, archetype);
    }

    private static String normalize(String value) {
        return value.trim().toLowerCase().replaceAll("[\\s_]+", "-");
    }

}
