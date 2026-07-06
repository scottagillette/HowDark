package com.redshift.ShadowDarkCalculator.party.loadout.classes;

import com.redshift.ShadowDarkCalculator.creatures.Stats;
import com.redshift.ShadowDarkCalculator.dice.Dice;
import com.redshift.ShadowDarkCalculator.dice.MultipleDice;
import com.redshift.ShadowDarkCalculator.party.loadout.Bonuses;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D6;

/**
 * A decorator that updates any bonuses from talent rolls for the Thief class.
 */

public class ThiefTalentDecorator implements TalentDecorator {

    @Override
    public void decorate(Stats stats, Bonuses bonuses) {
        /*
         2 Gain advantage on initiative rolls (reroll if duplicate)
         3-5 Your Backstab deals +1 dice of damage
         6-9 +2 to Strength, Dexterity, or Charisma stat
         10-11 +1 to melee and ranged attacks
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
            // TODO: 2 Gain advantage on initiative rolls (reroll if duplicate)
        } else if (rollOutcome >= 3 && rollOutcome <= 5) {
            // TODO: 3-5 Your Backstab deals +1 dice of damage
        } else if (rollOutcome >= 6 && rollOutcome <= 9) {
            // 6-9 +2 to Strength, Dexterity, or Charisma stat
            bonuses.addDexterityBonus();
            bonuses.addDexterityBonus();
        } else if (rollOutcome >= 10 && rollOutcome <= 11) {
            // 10-11 +1 to melee and ranged attacks
            bonuses.addMeleeAttackBonus();
            bonuses.addRangedAttackBomus();
        } else if (rollOutcome == 12) {
            // 12 Choose a talent or +2 points to distribute to stats
            bonuses.addDexterityBonus();
            bonuses.addDexterityBonus();
        }
    }

}
