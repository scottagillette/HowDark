package com.redshift.ShadowDarkCalculator.creatures.monsters.monstrosities;

import com.redshift.ShadowDarkCalculator.actions.PerformAllActions;
import com.redshift.ShadowDarkCalculator.actions.weapons.Weapon;
import com.redshift.ShadowDarkCalculator.creatures.*;
import com.redshift.ShadowDarkCalculator.creatures.monsters.Monster;
import com.redshift.ShadowDarkCalculator.dice.Dice;
import com.redshift.ShadowDarkCalculator.dice.RollModifier;
import lombok.extern.slf4j.Slf4j;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.*;

/**
 * Cantankerous bears with owl eyes, beaks, and feathers.
 * AC 13, HP 30, ATK 2 claw +5 (1d10), MV near (climb)
 * S +4, D +1, C +3, I -2, W +2, Ch -3, AL N, LV 6
 * Crush. Deals an extra die of damage if it hits the same target with both claws.
 */

@Slf4j
public class OwlBear extends Monster {

    public OwlBear(String name) {
        super(
                name,
                6,
                new Stats(18,12,17,7,14,5),
                13,
                D8.roll() + D8.roll() + D8.roll() + D8.roll() + D8.roll() + D8.roll() + 3,
                new PerformAllActions(
                        new OwlBear.Claw(),
                        new OwlBear.Claw()
                )
        );
        getLabels().add(CreatureLabel.FRONT_LINE);
    }

    private static class Claw extends Weapon {

        private static int attackNumber = 0;
        private static boolean priorAttackHits = false;

        public Claw() {
            super("Claw", D10, RollModifier.STRENGTH);
            addSlashing().addAttackRollBonus(1);
        }

        @Override
        protected boolean performSingleTargetAttack(Creature actor, Creature target) {
            boolean attackHits = super.performSingleTargetAttack(actor, target);

            attackNumber = attackNumber + 1;

            if (attackNumber == 2) {
                if (attackHits && priorAttackHits) {
                    // Take extra dice of damage
                    int extraDamage = D10.roll();
                    log.info("{} takes extra crushing damage of {}", target.getName(), extraDamage);
                    target.takeDamage(extraDamage, damageType);
                }
            } else {
                priorAttackHits = attackHits;
            }

            if (attackNumber == 2) { // Reset so another bear doesn't get in the way
                priorAttackHits = false;
                attackNumber = 0;
            }

            return attackHits;
        }
    }

}
