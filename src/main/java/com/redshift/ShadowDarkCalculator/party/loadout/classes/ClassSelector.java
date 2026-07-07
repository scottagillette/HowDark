package com.redshift.ShadowDarkCalculator.party.loadout.classes;

import com.redshift.ShadowDarkCalculator.creatures.StatType;
import com.redshift.ShadowDarkCalculator.creatures.Stats;
import com.redshift.ShadowDarkCalculator.creatures.players.PlayerClass;
import com.redshift.ShadowDarkCalculator.dice.SingleDie;
import com.redshift.ShadowDarkCalculator.party.loadout.Bonuses;

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
        classDecorators.put(PlayerClass.THIEF, new ThiefTalentDecorator());
        classDecorators.put(PlayerClass.WIZARD, new WizardTalentGenerator());
    }

    /**
     * Select an appropriate class based on stats and update any bonuses from talents of that class.
     */

    public PlayerClass selectPlayerClass(Stats stats, Bonuses bonuses) {
        final PlayerClass selectedClass;

        final StatType statType = getHighestStatType(stats);

        switch (statType) {
            case StatType.STRENGTH: {
                int result = SingleDie.D2.roll();
                if (result == 1) {
                    selectedClass = PlayerClass.FIGHTER;
                } else {
                    selectedClass = PlayerClass.PALADIN;
                }
                break;
            }
            case StatType.DEXTERITY: {
                selectedClass = PlayerClass.THIEF;
                break;
            }
            case StatType.WISDOM: {
                selectedClass = PlayerClass.PRIEST;
                break;
            }
            case StatType.INTELLIGENCE: {
                selectedClass = PlayerClass.WIZARD;
                break;
            }
            case StatType.CHARISMA: {
                int result = SingleDie.D2.roll();
                if (result == 1) {
                    selectedClass = PlayerClass.BARD;
                } else {
                    selectedClass = PlayerClass.NECROMANCER;
                }
                break;
            }
            default: {
                throw new IllegalArgumentException("Invalid stat type");
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
