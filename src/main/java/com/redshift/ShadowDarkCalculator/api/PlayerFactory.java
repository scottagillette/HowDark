package com.redshift.ShadowDarkCalculator.api;

import com.redshift.ShadowDarkCalculator.actions.PerformOneAction;
import com.redshift.ShadowDarkCalculator.actions.spells.*;
import com.redshift.ShadowDarkCalculator.actions.weapons.WeaponBuilder;
import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.creatures.CreatureLabel;
import com.redshift.ShadowDarkCalculator.creatures.Stats;
import com.redshift.ShadowDarkCalculator.creatures.players.*;

import java.util.Set;
import java.util.TreeSet;

/**
 * Creates players by class name with a default loadout (stats, armor, weapons and spells),
 * so callers only need to pick a class, a name and a level.
 *
 * Hit points scale with level using the class hit die average plus the CON modifier
 * (minimum 1 per level), matching how Shadowdark characters gain hit points.
 */

public final class PlayerFactory {

    private PlayerFactory() {
    }

    public static Creature create(PartyMemberConfig config) {
        return create(config.getPlayerClass(), config.getName(), config.getLevel());
    }

    public static Creature create(String playerClass, String name, int level) {
        final String key = normalize(playerClass);

        return switch (key) {
            case "fighter" -> fighter(name, level);
            case "paladin" -> paladin(name, level);
            case "priest" -> priest(name, level);
            case "wizard" -> wizard(name, level);
            case "thief" -> thief(name, level);
            case "necromancer" -> necromancer(name, level);
            case "bard" -> bard(name, level);
            case "ranger" -> ranger(name, level);
            case "warlock" -> warlock(name, level);
            case "witch" -> witch(name, level);
            case "knight-of-st-ydris" -> knightOfStYdris(name, level);
            case "commoner" -> commoner(name);
            default -> throw new IllegalArgumentException(
                    "Unknown player class: '" + playerClass + "'. Available: " + availableClasses());
        };
    }

    public static Set<String> availableClasses() {
        return new TreeSet<>(Set.of(
                "fighter", "paladin", "priest", "wizard", "thief", "necromancer",
                "bard", "ranger", "warlock", "witch", "knight-of-st-ydris", "commoner"));
    }

    private static Creature fighter(String name, int level) {
        final Stats stats = new Stats(16, 14, 12, 8, 11, 10);
        return new Fighter(
                displayName(name, "Fighter"),
                level,
                stats,
                15,
                hitPoints(8, 5, stats, level),
                WeaponBuilder.GREAT_AXE_2H.build()
        );
    }

    private static Creature paladin(String name, int level) {
        final Stats stats = new Stats(18, 13, 13, 8, 7, 9);
        return new Paladin(
                displayName(name, "Paladin"),
                level,
                stats,
                14,
                hitPoints(6, 4, stats, level),
                WeaponBuilder.BASTARD_SWORD_1H.build()
        );
    }

    private static Creature priest(String name, int level) {
        final Stats stats = new Stats(16, 10, 12, 7, 18, 10);
        return new Priest(
                displayName(name, "Priest"),
                level,
                stats,
                13,
                hitPoints(8, 4, stats, level),
                new PerformOneAction(
                        WeaponBuilder.LONGSWORD.build().setPriority(1),
                        new HolyWeapon().setPriority(5),
                        new CureWounds().setPriority(10),
                        new TurnUndead().setPriority(10)
                )
        );
    }

    private static Creature wizard(String name, int level) {
        final Stats stats = new Stats(13, 13, 13, 16, 10, 11);
        return new Wizard(
                displayName(name, "Wizard"),
                level,
                stats,
                11,
                hitPoints(5, 3, stats, level),
                new PerformOneAction(
                        WeaponBuilder.STAFF.build().setPriority(1),
                        new MagicMissile().setPriority(2),
                        new BurningHands().setPriority(5),
                        new Sleep().setPriority(10)
                )
        );
    }

    private static Creature thief(String name, int level) {
        final Stats stats = new Stats(9, 18, 10, 9, 10, 10);
        return new Thief(
                displayName(name, "Thief"),
                level,
                stats,
                15,
                hitPoints(4, 3, stats, level),
                new PerformOneAction(
                        WeaponBuilder.SHORT_BOW.build(),
                        WeaponBuilder.DAGGER_DEX.build()
                )
        );
    }

    private static Creature necromancer(String name, int level) {
        final Stats stats = new Stats(13, 10, 10, 13, 5, 15);
        return new Necromancer(
                displayName(name, "Necromancer"),
                level,
                stats,
                12,
                hitPoints(4, 3, stats, level),
                new PerformOneAction(
                        WeaponBuilder.LONGSWORD.build().setPriority(2),
                        new Withermark().setPriority(1),
                        new Undeath().setPriority(10)
                )
        );
    }

    private static Creature bard(String name, int level) {
        final Stats stats = new Stats(10, 14, 12, 10, 10, 16);
        return new Bard(
                displayName(name, "Bard"),
                level,
                stats,
                13,
                hitPoints(6, 4, stats, level),
                new PerformOneAction(
                        WeaponBuilder.CROSSBOW.build(),
                        WeaponBuilder.SHORT_SWORD.build()
                )
        );
    }

    private static Creature ranger(String name, int level) {
        final Stats stats = new Stats(12, 16, 14, 10, 11, 8);
        return new Ranger(
                displayName(name, "Ranger"),
                level,
                stats,
                13,
                hitPoints(8, 5, stats, level),
                new PerformOneAction(
                        WeaponBuilder.LONGBOW.build(),
                        WeaponBuilder.SHORT_SWORD.build()
                )
        );
    }

    private static Creature warlock(String name, int level) {
        final Stats stats = new Stats(12, 14, 12, 10, 11, 16);
        return new Warlock(
                displayName(name, "Warlock"),
                level,
                stats,
                13,
                hitPoints(6, 4, stats, level),
                new PerformOneAction(
                        WeaponBuilder.LONGBOW.build(),
                        WeaponBuilder.SHORT_SWORD.build()
                )
        );
    }

    private static Creature witch(String name, int level) {
        final Stats stats = new Stats(8, 12, 11, 10, 10, 16);
        return new Witch(
                displayName(name, "Witch"),
                level,
                stats,
                11,
                hitPoints(4, 3, stats, level),
                new PerformOneAction(
                        WeaponBuilder.STAFF.build().setPriority(1),
                        new Eyebite().setPriority(3),
                        new Hypnotize().setPriority(6)
                )
        );
    }

    private static Creature knightOfStYdris(String name, int level) {
        final Stats stats = new Stats(16, 12, 14, 8, 10, 13);
        return new KnightOfStYdris(
                displayName(name, "Knight of St Ydris"),
                level,
                stats,
                15,
                hitPoints(7, 4, stats, level),
                WeaponBuilder.BASTARD_SWORD_1H.build()
        );
    }

    private static Creature commoner(String name) {
        final Creature commoner = new Player(
                displayName(name, "Commoner"),
                0,
                new Stats(10, 10, 10, 10, 10, 10),
                10,
                1,
                WeaponBuilder.CLUB.build()
        );
        commoner.getLabels().add(CreatureLabel.FRONT_LINE);
        return commoner;
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
