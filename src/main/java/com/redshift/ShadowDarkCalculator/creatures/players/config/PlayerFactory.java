package com.redshift.ShadowDarkCalculator.creatures.players.config;

import com.redshift.ShadowDarkCalculator.actions.Action;
import com.redshift.ShadowDarkCalculator.actions.PerformOneAction;
import com.redshift.ShadowDarkCalculator.actions.spells.*;
import com.redshift.ShadowDarkCalculator.actions.weapons.WeaponBuilder;
import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.creatures.CreatureLabel;
import com.redshift.ShadowDarkCalculator.creatures.Stats;
import com.redshift.ShadowDarkCalculator.creatures.players.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Supplier;

/**
 * Creates players by class name. Each class has a default archetype (stats, armor, weapons
 * and spells) so callers only need to pick a class, a name and a level; a PartyMemberConfig
 * can override any part of the build (stats, armor class, hit points, luck token, and the
 * exact weapons, spells and potions carried).
 *
 * Hit points scale with level using the class hit die average plus the CON modifier
 * (minimum 1 per level), matching how Shadowdark characters gain hit points. When custom
 * stats are supplied the derived hit points use the custom CON modifier; an explicit
 * hitPoints value bypasses the derivation entirely.
 */

public final class PlayerFactory {

    @FunctionalInterface
    private interface PlayerConstructor {
        Creature create(String name, int level, Stats stats, int armorClass, int hitPoints, Action action);
    }

    /**
     * The default build for a class plus the constructor that applies class labels and
     * behaviors. Stats and actions are suppliers because both are mutated during combat.
     * A non-null fixedLevel pins the class to that level (commoners are always level 0).
     */
    private record Archetype(
            String displayName,
            Integer fixedLevel,
            Supplier<Stats> defaultStats,
            int defaultArmorClass,
            int baseHitPoints,
            int averageHitDie,
            Supplier<Action> defaultAction,
            PlayerConstructor constructor) {
    }

    private static final Map<String, Archetype> REGISTRY = new LinkedHashMap<>();

    static {
        register("fighter", new Archetype("Fighter", null,
                () -> new Stats(16, 14, 12, 8, 11, 10), 15, 8, 5,
                () -> WeaponBuilder.GREAT_AXE_2H.build(),
                Fighter::new));

        register("paladin", new Archetype("Paladin", null,
                () -> new Stats(18, 13, 13, 8, 7, 9), 14, 6, 4,
                () -> WeaponBuilder.BASTARD_SWORD_1H.build(),
                Paladin::new));

        register("priest", new Archetype("Priest", null,
                () -> new Stats(16, 10, 12, 7, 18, 10), 13, 8, 4,
                () -> new PerformOneAction(
                        WeaponBuilder.LONGSWORD.build().setPriority(1),
                        new HolyWeapon().setPriority(5),
                        new CureWounds().setPriority(10),
                        new TurnUndead().setPriority(10)),
                Priest::new));

        register("wizard", new Archetype("Wizard", null,
                () -> new Stats(13, 13, 13, 16, 10, 11), 11, 5, 3,
                () -> new PerformOneAction(
                        WeaponBuilder.STAFF.build().setPriority(1),
                        new MagicMissile().setPriority(2),
                        new BurningHands().setPriority(5),
                        new Sleep().setPriority(10)),
                Wizard::new));

        register("thief", new Archetype("Thief", null,
                () -> new Stats(9, 18, 10, 9, 10, 10), 15, 4, 3,
                () -> new PerformOneAction(
                        WeaponBuilder.SHORT_BOW.build(),
                        WeaponBuilder.DAGGER_DEX.build()),
                Thief::new));

        register("necromancer", new Archetype("Necromancer", null,
                () -> new Stats(13, 10, 10, 13, 5, 15), 12, 4, 3,
                () -> new PerformOneAction(
                        WeaponBuilder.LONGSWORD.build().setPriority(2),
                        new Withermark().setPriority(1),
                        new Undeath().setPriority(10)),
                Necromancer::new));

        register("bard", new Archetype("Bard", null,
                () -> new Stats(10, 14, 12, 10, 10, 16), 13, 6, 4,
                () -> new PerformOneAction(
                        WeaponBuilder.CROSSBOW.build(),
                        WeaponBuilder.SHORT_SWORD.build()),
                Bard::new));

        register("ranger", new Archetype("Ranger", null,
                () -> new Stats(12, 16, 14, 10, 11, 8), 13, 8, 5,
                () -> new PerformOneAction(
                        WeaponBuilder.LONGBOW.build(),
                        WeaponBuilder.SHORT_SWORD.build()),
                Ranger::new));

        register("warlock", new Archetype("Warlock", null,
                () -> new Stats(12, 14, 12, 10, 11, 16), 13, 6, 4,
                () -> new PerformOneAction(
                        WeaponBuilder.LONGBOW.build(),
                        WeaponBuilder.SHORT_SWORD.build()),
                Warlock::new));

        register("witch", new Archetype("Witch", null,
                () -> new Stats(8, 12, 11, 10, 10, 16), 11, 4, 3,
                () -> new PerformOneAction(
                        WeaponBuilder.STAFF.build().setPriority(1),
                        new Eyebite().setPriority(3),
                        new Hypnotize().setPriority(6)),
                Witch::new));

        register("knight-of-st-ydris", new Archetype("Knight of St Ydris", null,
                () -> new Stats(16, 12, 14, 8, 10, 13), 15, 7, 4,
                () -> WeaponBuilder.BASTARD_SWORD_1H.build(),
                KnightOfStYdris::new));

        register("commoner", new Archetype("Commoner", 0,
                () -> new Stats(10, 10, 10, 10, 10, 10), 10, 1, 0,
                () -> WeaponBuilder.CLUB.build(),
                (name, level, stats, armorClass, hitPoints, action) -> {
                    final Creature commoner = new Player(name, level, stats, armorClass, hitPoints, action);
                    commoner.getLabels().add(CreatureLabel.FRONT_LINE);
                    return commoner;
                }));
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

        final Action action = buildLoadout(config);

        final int hitPoints = config.getHitPoints();

        final Creature player = archetype.constructor().create(
                displayName(config.getName(), archetype.displayName()),
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
            throw new IllegalArgumentException("Unknown player class: '" + playerClass + "'. Available: " + availableClasses());
        }

        return archetype;
    }

    private static Action buildLoadout(PartyMemberConfig config) {
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

    private static int hitPoints(int baseHitPoints, int averageHitDie, Stats stats, int level) {
        int hitPoints = baseHitPoints;

        for (int i = 1; i < level; i++) {
            hitPoints += Math.max(1, averageHitDie + stats.getConstitutionModifier());
        }

        return hitPoints;
    }

    private static String displayName(String name, String fallback) {
        return (name == null || name.isBlank()) ? fallback : name;
    }

    private static String normalize(String value) {
        return value.trim().toLowerCase().replaceAll("[\\s_]+", "-");
    }

}
