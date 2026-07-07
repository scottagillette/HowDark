package com.redshift.ShadowDarkCalculator.party.loadout.classes;

import com.redshift.ShadowDarkCalculator.creatures.Stats;
import com.redshift.ShadowDarkCalculator.dice.Dice;
import com.redshift.ShadowDarkCalculator.dice.MultipleDice;
import com.redshift.ShadowDarkCalculator.dice.SingleDie;
import com.redshift.ShadowDarkCalculator.party.loadout.Bonuses;
import lombok.extern.slf4j.Slf4j;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D6;

/**
 * A decorator that updates any bonuses from talent rolls for the Bard class.
 */

@Slf4j
public class NecromancerTalentDecorator implements TalentDecorator {

    @Override
    public void decorate(Stats stats, Bonuses bonuses) {
        /*
         2 The next time you die, you may return to life with full HP
         3-7 +1 to your spellcasting checks or +1 to melee attacks
         8-9 +2 to Strength, Constitution, or Charisma stat
         10-11 Gain advantage on casting one spell you know
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

        if (rollOutcome >= 3 && rollOutcome <= 7) {
            // 3-7 +1 to your spellcasting checks or +1 to melee attacks
            int result = SingleDie.D2.roll();
            if (result == 1) {
                bonuses.addSpellCastingBonus();
            } else {
                bonuses.addMeleeAttackBonus();
            }
        } else if ((rollOutcome >= 8 && rollOutcome <= 9) || rollOutcome == 12) {
            // 8-9 +2 to Strength, Constitution, or Charisma stat
            // 12 Choose a talent or +2 points to distribute to stats
            if (stats.getCharisma() > stats.getStrength()) {
                bonuses.addStrengthBonus();
                bonuses.addStrengthBonus();
            } else {
                bonuses.addCharismaBonus();
                bonuses.addCharismaBonus();
            }
        } else if (rollOutcome >= 10 && rollOutcome <= 11) {
            // 10-11 Gain advantage on casting one spell you know
            bonuses.addSpellAdvantage();
        } else {
            // 2 The next time you die, you may return to life with full HP
            // TODO: Implement this feature.
        }
    }
}
