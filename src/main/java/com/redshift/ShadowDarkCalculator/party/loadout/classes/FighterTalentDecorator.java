package com.redshift.ShadowDarkCalculator.party.loadout.classes;

import com.redshift.ShadowDarkCalculator.creatures.Stats;
import com.redshift.ShadowDarkCalculator.dice.Dice;
import com.redshift.ShadowDarkCalculator.dice.MultipleDice;
import com.redshift.ShadowDarkCalculator.party.loadout.Bonuses;
import lombok.extern.slf4j.Slf4j;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D6;

/**
 * A decorator that updates any bonuses from talent rolls for the Fighter class.
 */

@Slf4j
public class FighterTalentDecorator implements TalentDecorator {

    @Override
    public void decorate(Stats stats, Bonuses bonuses) {
        // Specific Fighter bonuses: Weapon Mastery
        // +1 attack and damage for the melee weapon selected.
        bonuses.addMeleeAttackBonus();
        bonuses.addMeleeDamageBonus();

        // Talent roll

        /*
         2     Gain Weapon Mastery with one additional weapon type
         3-6   +1 to melee and ranged attacks
         7-9   +2 to Strength, Dexterity, or Constitution stat
         10-11 Choose one kind of armor. You get +1 AC from that armor
         12    Choose a talent or +2 points to distribute to stats
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
            // 3-6 +1 to melee and ranged attacks
            bonuses.addMeleeAttackBonus();
            bonuses.addRangedAttackBomus();
        } else if ((rollOutcome >= 7 && rollOutcome <= 9) || rollOutcome == 12) {
            // 7-9 +2 to Strength, Dexterity, or Constitution stat
            // 12 Choose a talent or +2 points to distribute to stats
            if (stats.getStrength() >= 18) {
                bonuses.addDexterityBonus();
                bonuses.addDexterityBonus();
            } else {
                bonuses.addStrengthBonus();
                bonuses.addStrengthBonus();
            }
        } else if (rollOutcome >= 10 && rollOutcome <= 11) {
            // 10-11 Choose one kind of armor. You get +1 AC from that armor
            bonuses.addArmorClassBonus();
        } else {
            // 2 Gain Weapon Mastery with one additional weapon type
        }
    }

}
