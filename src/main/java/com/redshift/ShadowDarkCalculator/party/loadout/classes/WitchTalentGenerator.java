package com.redshift.ShadowDarkCalculator.party.loadout.classes;

import com.redshift.ShadowDarkCalculator.creatures.Stats;
import com.redshift.ShadowDarkCalculator.dice.Dice;
import com.redshift.ShadowDarkCalculator.dice.MultipleDice;
import com.redshift.ShadowDarkCalculator.party.loadout.Bonuses;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D6;

/**
 * A decorator that updates any bonuses from talent rolls for the Wizard class.
 */

public class WitchTalentGenerator implements TalentDecorator {

    @Override
    public void decorate(Stats stats, Bonuses bonuses) {
        /*
         2 1/day, teleport to your familiar's location as a move
         3-7 +2 to Charisma stat or +1 to witch spell casting checks
         8-9 Gain advantage on casting one spell you know
         10-11 Learn an additional witch spell of any tier you can cast
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
            // 2 1/day, teleport to your familiar's location as a move
        } else if (rollOutcome >= 3 && rollOutcome <= 7) {
            // 3-7 +2 to Charisma stat or +1 to witch spell casting checks
            if (stats.getCharisma() < 18) {
                bonuses.addCharismaBonus();
                bonuses.addCharismaBonus();
            } else {
                bonuses.addSpellCastingBonus();
                bonuses.addSpellCastingBonus();
            }
        } else if (rollOutcome >= 8 && rollOutcome <= 9) {
            // 8-9 Gain advantage on casting one spell you know
            bonuses.addSpellAdvantage();
        } else if (rollOutcome >= 10 && rollOutcome <= 11) {
            // 10-11 Learn an additional witch spell of any tier you can cast
            bonuses.addExtraSpellChoice();
        } else if (rollOutcome == 12) {
            // 12 Choose a talent or +2 points to distribute to stats
            if (stats.getCharisma() < 18) {
                bonuses.addCharismaBonus();
                bonuses.addCharismaBonus();
            } else {
                bonuses.addDexterityBonus();
                bonuses.addDexterityBonus();
            }
        }
    }

}
