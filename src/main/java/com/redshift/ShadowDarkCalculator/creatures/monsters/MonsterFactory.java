package com.redshift.ShadowDarkCalculator.creatures.monsters;

import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.creatures.monsters.animated.AnimatedArmor;
import com.redshift.ShadowDarkCalculator.creatures.monsters.beasts.*;
import com.redshift.ShadowDarkCalculator.creatures.monsters.demons.*;
import com.redshift.ShadowDarkCalculator.creatures.monsters.dragons.*;
import com.redshift.ShadowDarkCalculator.creatures.monsters.elementals.*;
import com.redshift.ShadowDarkCalculator.creatures.monsters.giants.*;
import com.redshift.ShadowDarkCalculator.creatures.monsters.goblinoid.*;
import com.redshift.ShadowDarkCalculator.creatures.monsters.golem.FleshGolem;
import com.redshift.ShadowDarkCalculator.creatures.monsters.hag.WeldHag;
import com.redshift.ShadowDarkCalculator.creatures.monsters.humanoid.*;
import com.redshift.ShadowDarkCalculator.creatures.monsters.monstrosities.*;
import com.redshift.ShadowDarkCalculator.creatures.monsters.oozes.*;
import com.redshift.ShadowDarkCalculator.creatures.monsters.undead.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Function;

/**
 * Creates monsters by type name (e.g. "goblin", "storm-giant") so fights can be
 * configured with plain text instead of code.
 */

public final class MonsterFactory {

    private static final Map<String, Function<String, Creature>> REGISTRY = new LinkedHashMap<>();

    static {
        // Animated
        register("animated-armor", AnimatedArmor::new);

        // Beasts
        register("brown-bear", BrownBear::new);
        register("crocodile", Crocodile::new);
        register("dire-wolf", DireWolf::new);
        register("giant-centipede", GiantCentipede::new);
        register("giant-leech", GiantLeech::new);
        register("giant-rat", GiantRat::new);
        register("giant-snake", GiantSnake::new);
        register("giant-spider", GiantSpider::new);
        register("sting-bat", StingBat::new);
        register("tar-bat", TarBat::new);
        register("void-spider", VoidSpider::new);
        register("wolf", Wolf::new);
        register("worg", Worg::new);

        // Demons
        register("dralech", Dralech::new);
        register("hexling", Hexling::new);
        register("marrow-fiend", MarrowFiend::new);
        register("the-willow-man", TheWillowMan::new);

        // Dragons
        register("fire-dragon", FireDragon::new);
        register("swamp-dragon", SwampDragon::new);
        register("wyvern", Wyvern::new);

        // Elementals
        register("azer", Azer::new);
        register("fire-elemental", FireElemental::new);
        register("lesser-fire-elemental", LesserFireElemental::new);

        // Giants
        register("cyclops", Cyclops::new);
        register("hill-giant", HillGiant::new);
        register("ogre", Ogre::new);
        register("storm-giant", StormGiant::new);
        register("troll", Troll::new);

        // Goblinoids
        register("bugbear", Bugbear::new);
        register("goblin", Goblin::new);
        register("goblin-boss", GoblinBoss::new);
        register("goblin-shaman", GoblinShaman::new);
        register("hobgoblin", Hobgoblin::new);
        register("orc", Orc::new);

        // Golems
        register("flesh-golem", FleshGolem::new);

        // Hags
        register("weld-hag", WeldHag::new);

        // Humanoids
        register("acolyte", Acolyte::new);
        register("apprentice", Apprentice::new);
        register("arch-mage", ArchMage::new);
        register("assassin", Assassin::new);
        register("bandit", Bandit::new);
        register("bittermold", Bittermold::new);
        register("cultist", Cultist::new);
        register("druid", Druid::new);
        register("knight", Knight::new);
        register("mage", Mage::new);
        register("reaver", Reaver::new);
        register("thief", Thief::new);
        register("thug", Thug::new);


        // Monstrosities
        register("ankheg", Ankheg::new);
        register("bog-thorn", BogThorn::new);
        register("bulette", Bulette::new);
        register("cave-creeper", CaveCreeper::new);
        register("ettercap", Ettercap::new);
        register("manticore", Manticore::new);
        register("medusa", Medusa::new);
        register("mimic", Mimic::new);
        register("minotaur", Minotaur::new);
        register("owl-bear", OwlBear::new);
        register("wererat", Wererat::new);
        register("werewolf", Werewolf::new);

        // Oozes
        register("gelatinous-cube", GelatinousCube::new);
        register("ichor-ooze", IchorOoze::new);

        // Undead
        register("bone-naga", BoneNaga::new);
        register("ghast", Ghast::new);
        register("ghoul", Ghoul::new);
        register("lich", Lich::new);
        register("mummy", Mummy::new);
        register("shadow", Shadow::new);
        register("skeleton", Skeleton::new);
        register("vampire-spawn", VampireSpawn::new);
        register("wight", Wight::new);
        register("will-o-wisp", WillOWisp::new);
        register("wraith", Wraith::new);
        register("zombie", Zombie::new);
    }

    private MonsterFactory() {
        // No construction.
    }

    /**
     * Returns a set of available monster types.
     */

    public static Set<String> availableTypes() {
        return new TreeSet<>(REGISTRY.keySet());
    }

    /**
     * Create all monsters for a config entry; names are numbered when count > 1.
     */

    public static List<Creature> create(MonsterConfig config) {
        List<Creature> monsters = new ArrayList<>();

        if (config.getType().equals("random")) {
            monsters = List.of(createRandomMonster());
        } else {
            final String baseName = (config.getName() == null || config.getName().isBlank())
                    ? defaultDisplayName(config.getType())
                    : config.getName();

            for (int i = 1; i <= config.getCount(); i++) {
                final String name = config.getCount() > 1 ? baseName + " " + i : baseName;
                monsters.add(create(config.getType(), name));
            }
        }

        return monsters;
    }

    public static Creature create(String type, String name) {
        final Function<String, Creature> constructor = REGISTRY.get(normalize(type));

        if (constructor == null) {
            throw new IllegalArgumentException("Unknown monster type: '" + type + "'. Available: " + availableTypes());
        }

        return constructor.apply(name);
    }

    public static Creature createRandomMonster() {
        final List<String> types = new ArrayList<>(REGISTRY.keySet());
        final int randomIndex = (int) (Math.random() * types.size());
        final String randomType = types.get(randomIndex);
        return create(randomType, defaultDisplayName(randomType));
    }

    private static String defaultDisplayName(String type) {
        final String[] parts = normalize(type).split("-");
        final StringBuilder builder = new StringBuilder();

        for (String part : parts) {
            if (!builder.isEmpty()) {
                builder.append(" ");
            }
            builder.append(Character.toUpperCase(part.charAt(0))).append(part.substring(1));
        }

        return builder.toString();
    }

    private static String normalize(String value) {
        return value.trim().toLowerCase().replaceAll("[\\s_]+", "-");
    }

    private static void register(String key, Function<String, Creature> constructor) {
        REGISTRY.put(key, constructor);
    }

}
