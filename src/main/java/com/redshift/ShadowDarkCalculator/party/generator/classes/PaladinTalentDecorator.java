package com.redshift.ShadowDarkCalculator.party.generator.classes;

import com.redshift.ShadowDarkCalculator.creatures.Stats;
import com.redshift.ShadowDarkCalculator.dice.Dice;
import com.redshift.ShadowDarkCalculator.dice.MultipleDice;
import com.redshift.ShadowDarkCalculator.party.generator.Bonuses;
import lombok.extern.slf4j.Slf4j;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D6;

/**
 * A decorator that updates any bonuses from talent rolls for the Paladin class.
 */

@Slf4j
public class PaladinTalentDecorator implements TalentDecorator {

    @Override
    public void decorate(Stats stats, Bonuses bonuses) {
        /*
         2 Your Named Blade gains a random magic weapon benefit
         3-6 Gain +1 to attacks and damage with your Named Blade
         7-9 +2 to Strength, Constitution, or Charisma stat
         10-11 Increase your Inspiring Presence dying roll range by 1
         12 Choose a talent or +2 points to distribute to stats
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
            // TODO: 2 Your Named Blade gains a random magic weapon benefit
            rollAndApplyTalent(stats, bonuses); // Re-roll for now.
        } else if (rollOutcome >= 3 && rollOutcome <= 6) {
            // 3-6 Gain +1 to attacks and damage with your Named Blade
            bonuses.addMeleeAttackBonus();
            bonuses.addMeleeDamageBonus();
        } else if (rollOutcome >= 7 && rollOutcome <= 9) {
            // 7-9 +2 to Strength, Constitution, or Charisma stat
            if (stats.getStrength() >= 18) {
                bonuses.addDexterityBonus();
                bonuses.addDexterityBonus();
            } else {
                bonuses.addStrengthBonus();
                bonuses.addStrengthBonus();
            }
        } else if (rollOutcome >= 10 && rollOutcome <= 11) {
            // TODO: 10-11 Increase your Inspiring Presence dying roll range by 1
            rollAndApplyTalent(stats, bonuses); // Re-roll for now.
        } else if (rollOutcome == 12) {
            // 12 Choose a talent or +2 points to distribute to stats
            bonuses.addMeleeAttackBonus(); // Choose one of the better talents.
            bonuses.addMeleeDamageBonus();
        }
    }

}
