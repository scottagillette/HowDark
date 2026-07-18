package com.redshift.ShadowDarkCalculator.party.generator.classes;

import com.redshift.ShadowDarkCalculator.creatures.Stats;
import com.redshift.ShadowDarkCalculator.dice.Dice;
import com.redshift.ShadowDarkCalculator.dice.MultipleDice;
import com.redshift.ShadowDarkCalculator.party.generator.Bonuses;
import lombok.extern.slf4j.Slf4j;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D6;

/**
 * A decorator that updates any bonuses from talent rolls for the Fighter class.
 */

@Slf4j
public class RangerTalentDecorator implements TalentDecorator {

    @Override
    public void decorate(Stats stats, Bonuses bonuses) {
        // Talent roll

        /*
         2 You deal d12 damage with one weapon type you choose
         3-6 +1 to melee or ranged attacks and damage
         7-9 +2 to Strength, Dexterity, or Intelligence stat
         10-11 You gain ADV on Herbalism checks for a remedy you choose
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
            // 2 You deal d12 damage with one weapon type you choose
            bonuses.addDamageDiceD12();
        } else if (rollOutcome >= 3 && rollOutcome <= 6) {
            // 3-6 +1 to melee or ranged attacks and damage
            if (stats.getStrength() > stats.getDexterity()) {
                bonuses.addMeleeAttackBonus();
            } else {
                bonuses.addRangedAttackBomus();
            }
        } else if (rollOutcome >= 7 && rollOutcome <= 9) {
            // 7-9 +2 to Strength, Dexterity, or Intelligence stat
            if (stats.getStrength() > stats.getDexterity()) {
                if (stats.getStrength() > stats.getIntelligence()) {
                    bonuses.addIntelligenceBonus();
                    bonuses.addIntelligenceBonus();
                } else {
                    bonuses.addStrengthBonus();
                    bonuses.addStrengthBonus();
                }
            } else {
                bonuses.addDexterityBonus();
                bonuses.addDexterityBonus();
            }
        } else if (rollOutcome >= 10 && rollOutcome <= 11) {
            // 10-11 You gain ADV on Herbalism checks for a remedy you choose
            bonuses.addSpellAdvantage(); // See action builder for Curative
        } else if (rollOutcome == 12) {
            // 12 Choose a talent or +2 points to distribute to stats
            bonuses.addDamageDiceD12(); // Seems the best!
        }
    }

}
