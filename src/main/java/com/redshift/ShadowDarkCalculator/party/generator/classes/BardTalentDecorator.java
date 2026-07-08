package com.redshift.ShadowDarkCalculator.party.generator.classes;

import com.redshift.ShadowDarkCalculator.creatures.Stats;
import com.redshift.ShadowDarkCalculator.dice.Dice;
import com.redshift.ShadowDarkCalculator.dice.MultipleDice;
import com.redshift.ShadowDarkCalculator.party.generator.Bonuses;
import lombok.extern.slf4j.Slf4j;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D6;

/**
 * A decorator that updates any bonuses from talent rolls for the Bard class.
 */

@Slf4j
public class BardTalentDecorator implements TalentDecorator {

    @Override
    public void decorate(Stats stats, Bonuses bonuses) {
        /*
         2 You have ADV on downtime checks (excluding carousing)
         3-6 +1 to melee and ranged attacks or +1 to Fascinate rolls
         7-9 +2 points to distribute to any stats
         10-11 Add +2 to your group's carousing event rolls
         12 Choose a talent
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

        if (rollOutcome >= 3 && rollOutcome <= 6) {
            // 3-6 +1 to melee and ranged attacks or +1 to Fascinate rolls
            bonuses.addMeleeAttackBonus();
            bonuses.addRangedAttackBomus();
            // TODO: Implement Fascinate
        } else if ((rollOutcome >= 7 && rollOutcome <= 9) || rollOutcome == 12) {
            // 7-9 +2 points to distribute to any stats
            // 12 Choose a talent
            if (stats.getStrength() >= stats.getDexterity()) {
                bonuses.addStrengthBonus();
                bonuses.addStrengthBonus();
            } else {
                bonuses.addDexterityBonus();
                bonuses.addDexterityBonus();
            }
        } else {
            // 2 You have ADV on downtime checks (excluding carousing)
            // 10-11 Add +2 to your group's carousing event rolls
        }
    }
}
