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
                switch (SingleDie.D3.roll()) {
                    case 1 -> selectedClass = PlayerClass.FIGHTER;
                    case 2 -> selectedClass = PlayerClass.PALADIN;
                    case 3 -> selectedClass = PlayerClass.RANGER;
                }
                break;
            }
            case DEXTERITY: {
                switch (SingleDie.D2.roll()) {
                    case 1 -> selectedClass = PlayerClass.RANGER;
                    case 2 -> selectedClass = PlayerClass.THIEF;
                }
                break;
            }
            case WISDOM: {
                selectedClass = PlayerClass.PRIEST;
                break;
            }
            case INTELLIGENCE: {
                switch (SingleDie.D2.roll()) {
                    case 1 -> selectedClass = PlayerClass.RANGER;
                    case 2 -> selectedClass = PlayerClass.WIZARD;
                }
                break;
            }
            case CHARISMA: {
                switch (SingleDie.D3.roll()) {
                    case 1 -> selectedClass = PlayerClass.BARD;
                    case 2 -> selectedClass = PlayerClass.NECROMANCER;
                    case 3 -> selectedClass = PlayerClass.WITCH;
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
