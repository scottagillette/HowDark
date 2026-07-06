package com.redshift.ShadowDarkCalculator.party.loadout.classes;

import com.redshift.ShadowDarkCalculator.creatures.StatType;
import com.redshift.ShadowDarkCalculator.creatures.Stats;
import com.redshift.ShadowDarkCalculator.creatures.players.PlayerClass;
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

        // TODO: Randomly select a class once others are added for given stat types.
        switch (statType) {
            case StatType.STRENGTH -> selectedClass = PlayerClass.FIGHTER;
            case StatType.DEXTERITY -> selectedClass = PlayerClass.THIEF;
            case StatType.WISDOM -> selectedClass = PlayerClass.PRIEST;
            case StatType.INTELLIGENCE -> selectedClass = PlayerClass.WIZARD;
            case StatType.CHARISMA -> selectedClass = PlayerClass.BARD;
            default -> selectedClass = PlayerClass.FIGHTER;
        };

        classDecorators.get(selectedClass).decorate(stats, bonuses);

        return selectedClass;
    }

    private StatType getHighestStatType(Stats stats) {
        StatType highestStatType = StatType.STRENGTH;
        int highestStat = stats.getStrength();

        if (stats.getDexterity() > highestStat) {
            highestStatType = StatType.DEXTERITY;
        }

        // Ignore CON since it's not specific to class types.

        if (stats.getWisdom() > highestStat) {
            highestStatType = StatType.WISDOM;
        }
        if (stats.getIntelligence() > highestStat) {
            highestStatType = StatType.INTELLIGENCE;
        }
        if (stats.getCharisma() > highestStat) {
            highestStatType = StatType.CHARISMA;
        }

        return highestStatType;
    }
}
