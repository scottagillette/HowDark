package com.redshift.ShadowDarkCalculator.party.generator.classes;

import com.redshift.ShadowDarkCalculator.creatures.Stats;
import com.redshift.ShadowDarkCalculator.dice.Dice;
import com.redshift.ShadowDarkCalculator.dice.MultipleDice;
import com.redshift.ShadowDarkCalculator.party.generator.Bonuses;
import lombok.extern.slf4j.Slf4j;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D6;

/**
 * A decorator that updates any bonuses from talent rolls for the Priest class.
 */

@Slf4j
public class PriestTalentDecorator implements TalentDecorator {

    @Override
    public void decorate(Stats stats, Bonuses bonuses) {
        /*
         2     Gain advantage on casting one spell you know
         3-6   +1 to melee or ranged attacks
         7-9   +1 to priest spellcasting checks
         10-11 +2 to Strength or Wisdom stat
         12    Choose a talent or +2 points to distribute
         */

        // TODO: Figure out ADV on talent rolls.

        for (int i = 0; i < bonuses.getTalentRolls(); i++) {
            rollAndApplyTalent(stats, bonuses);
        }
    }

    private void rollAndApplyTalent(Stats stats, Bonuses bonuses) {
        final Dice dice = new MultipleDice(D6, D6);
        int rollOutcome = dice.roll();

        if (rollOutcome == 2) {
            // 2 Gain advantage on casting one spell you know
            bonuses.addSpellAdvantage();
        } else if (rollOutcome >= 3 && rollOutcome <= 6) {
            // 3-6 +1 to melee or ranged attacks
            if (stats.getStrength() >= stats.getDexterity()) {
                bonuses.addMeleeAttackBonus();
            } else {
                bonuses.addRangedAttackBomus();
            }
        } else if (rollOutcome >= 7 && rollOutcome <= 9) {
            // 7-9 +1 to priest spellcasting checks
            bonuses.addSpellCastingBonus();
        } else if (rollOutcome >= 10 && rollOutcome <= 11) {
            // 10-11 +2 to Strength or Wisdom stat
            if (stats.getWisdom() >= 18) {
                bonuses.addStrengthBonus();
                bonuses.addStrengthBonus();
            } else {
                bonuses.addWisdomBonus();
                bonuses.addWisdomBonus();
            }
        } else if (rollOutcome == 12) {
            // 12 Choose a talent or +2 points to distribute
            bonuses.addSpellAdvantage(); // Seems really good.
        }
    }

}
