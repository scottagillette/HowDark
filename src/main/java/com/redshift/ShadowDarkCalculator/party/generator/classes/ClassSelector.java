package com.redshift.ShadowDarkCalculator.party.generator.classes;

import com.redshift.ShadowDarkCalculator.creatures.StatType;
import com.redshift.ShadowDarkCalculator.creatures.Stats;
import com.redshift.ShadowDarkCalculator.creatures.players.PlayerClass;
import com.redshift.ShadowDarkCalculator.dice.SingleDie;
import com.redshift.ShadowDarkCalculator.party.generator.Bonuses;

import java.util.HashMap;
import java.util.Map;

/**
 * A selector of a player class based on stats and applies bonuses based on the class talents.
 */

public class ClassSelector {
    private final Map<PlayerClass, TalentDecorator> classDecorators = new HashMap<>();

    public ClassSelector() {
        classDecorators.put(PlayerClass.BARD, new BardTalentDecorator());
        classDecorators.put(PlayerClass.FIGHTER, new FighterTalentDecorator());
        classDecorators.put(PlayerClass.NECROMANCER, new NecromancerTalentDecorator());
        classDecorators.put(PlayerClass.PALADIN, new PaladinTalentDecorator());
        classDecorators.put(PlayerClass.PRIEST, new PriestTalentDecorator());
        classDecorators.put(PlayerClass.RANGER, new RangerTalentDecorator());
        classDecorators.put(PlayerClass.THIEF, new ThiefTalentDecorator());
        classDecorators.put(PlayerClass.WITCH, new WitchTalentGenerator());
        classDecorators.put(PlayerClass.WIZARD, new WizardTalentGenerator());
    }

    /**
     * Select an appropriate class based on stats and update any bonuses from talents of that class.
     */

    public PlayerClass selectPlayerClass(Stats stats, Bonuses bonuses) {
        PlayerClass selectedClass = PlayerClass.FIGHTER;

        final StatType statType = getHighestStatType(stats);

        switch (statType) {
            case STRENGTH: {
                switch (SingleDie.D8.roll()) {
                    case 1, 2, 3, 4, 5 -> selectedClass = PlayerClass.FIGHTER; // 62.5% of a stat
                    case 6, 7 -> selectedClass = PlayerClass.PALADIN; // 25% of a stat
                    case 8 -> selectedClass = PlayerClass.RANGER; // 62.5% across 3 stats
                }
                break;
            }
            case DEXTERITY: {
                switch (SingleDie.D4.roll()) {
                    case 1 -> selectedClass = PlayerClass.RANGER; // 62.5% across 3 stats
                    case 2, 3, 4 -> selectedClass = PlayerClass.THIEF; // 75%% of a stat
                }
                break;
            }
            case WISDOM: {
                selectedClass = PlayerClass.PRIEST; // 100% of a stat
                break;
            }
            case INTELLIGENCE: {
                switch (SingleDie.D4.roll()) {
                    case 1 -> selectedClass = PlayerClass.RANGER; // 62.5% across 3 stats
                    case 2, 3, 4 -> selectedClass = PlayerClass.WIZARD; // 75% of a stat
                }
                break;
            }
            case CHARISMA: {
                switch (SingleDie.D3.roll()) {
                    case 1 -> selectedClass = PlayerClass.BARD; // 33% of a stat
                    case 2 -> selectedClass = PlayerClass.NECROMANCER; // 33% of a stat
                    case 3 -> selectedClass = PlayerClass.WITCH; // 33% of a stat
                }
                break;
            }
            default: {
                throw new IllegalArgumentException("Invalid stat type: " + statType);
            }
        };

        classDecorators.get(selectedClass).decorate(stats, bonuses);

        return selectedClass;
    }

    private StatType getHighestStatType(Stats stats) {
        StatType highestStatType = null;
        int highestStat = 0;

        if (stats.getStrength() > highestStat) {
            highestStatType = StatType.STRENGTH;
            highestStat = stats.getStrength();
        }

        if (stats.getDexterity() > highestStat) {
            highestStatType = StatType.DEXTERITY;
            highestStat = stats.getDexterity();
        }

        // Ignore CON since it's not specific to class types.

        if (stats.getWisdom() > highestStat) {
            highestStatType = StatType.WISDOM;
            highestStat = stats.getWisdom();
        }
        if (stats.getIntelligence() > highestStat) {
            highestStatType = StatType.INTELLIGENCE;
            highestStat = stats.getIntelligence();
        }
        if (stats.getCharisma() > highestStat) {
            highestStatType = StatType.CHARISMA;
            highestStat = stats.getCharisma();
        }

        if (highestStatType == null) throw new IllegalStateException("Stat type not found!");

        return highestStatType;
    }
}
