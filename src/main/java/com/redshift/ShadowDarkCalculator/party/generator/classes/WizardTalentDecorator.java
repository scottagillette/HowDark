package com.redshift.ShadowDarkCalculator.party.generator.classes;

import com.redshift.ShadowDarkCalculator.creatures.Stats;
import com.redshift.ShadowDarkCalculator.dice.Dice;
import com.redshift.ShadowDarkCalculator.dice.MultipleDice;
import com.redshift.ShadowDarkCalculator.party.generator.Bonuses;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D6;

/**
 * A decorator that updates any bonuses from talent rolls for the Wizard class.
 */

public class WizardTalentDecorator implements TalentDecorator {

    @Override
    public void decorate(Stats stats, Bonuses bonuses) {
        /*
         2 Make 1 random magic item of a type you choose (pg. 282)
         3-7 +2 to Intelligence stat or +1 to wizard spellcasting checks
         8-9 Gain advantage on casting one spell you know
         10-11 Learn one additional wizard spell of any tier you know
         12 Choose a talent or +2 points to distribute to stats
         */

        // TODO: Figure out ADV on talent rolls.

        // First talent roll
        rollAndApplyTalent(stats, bonuses);

        if (bonuses.getTalentRolls() == 2) {
            rollAndApplyTalent(stats, bonuses);
        }

    }

    private void rollAndApplyTalent(Stats stats, Bonuses bonuses) {
        final Dice dice = new MultipleDice(D6, D6);
        int rollOutcome = dice.roll();

        if (rollOutcome == 2) {
            // TODO: 2 Make 1 random magic item of a type you choose (pg. 282)

        } else if (rollOutcome >= 3 && rollOutcome <= 7) {
            // 3-7 +2 to Intelligence stat or +1 to wizard spellcasting checks
            bonuses.addIntelligenceBonus();
            bonuses.addIntelligenceBonus();
        } else if (rollOutcome >= 8 && rollOutcome <= 9) {
            // 8-9 Gain advantage on casting one spell you know
            bonuses.addSpellAdvantage();
        } else if (rollOutcome >= 10 && rollOutcome <= 11) {
            // 10-11 Learn one additional wizard spell of any tier you know
            bonuses.addExtraSpellChoice();
        } else if (rollOutcome == 12) {
            // 12 Choose a talent or +2 points to distribute to stats
            bonuses.addIntelligenceBonus();
            bonuses.addIntelligenceBonus();
        }
    }

}
