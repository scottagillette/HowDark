package com.redshift.ShadowDarkCalculator.creatures.monsters.beasts;

import com.redshift.ShadowDarkCalculator.actions.PerformAllActions;
import com.redshift.ShadowDarkCalculator.actions.weapons.Weapon;
import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.creatures.CreatureLabel;
import com.redshift.ShadowDarkCalculator.creatures.Stats;
import com.redshift.ShadowDarkCalculator.creatures.monsters.Monster;
import com.redshift.ShadowDarkCalculator.dice.Dice;
import com.redshift.ShadowDarkCalculator.dice.RollModifier;
import lombok.extern.slf4j.Slf4j;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D8;

/**
 * A hulking, swaying brute with claws as long as a finger.
 * AC 13, HP 25, ATK 2 claw +4 (1d8), MV near (climb)
 * S +4, D +1, C +3, I -2, W +1, Ch -2, AL N, LV 5
 * Crush. Deals an extra die of damage if it hits the same target with both claws.
 */

@Slf4j
public class BrownBear extends Monster {

    public BrownBear(String name) {
        super(
                name,
                5,
                new Stats(18,12,17,6,12,5),
                13,
                D8.roll() + D8.roll() + D8.roll() + D8.roll() + D8.roll() + 3,
                new PerformAllActions(
                        new BrownBear.Claw(),
                        new BrownBear.Claw()
                )
        );
        getLabels().add(CreatureLabel.FRONT_LINE);
    }

    private static class Claw extends Weapon {

        private static int attackNumber = 0;
        private static boolean priorAttackHits = false;

        public Claw() {
            super("Claw", D8, RollModifier.STRENGTH);
            addSlashing();
        }

        @Override
        protected boolean performSingleTargetAttack(Creature actor, Creature target, String weaponName, Dice damageDice, RollModifier rollModifier) {
            boolean attackHits = super.performSingleTargetAttack(actor, target, weaponName, damageDice, rollModifier);

            attackNumber = attackNumber + 1;

            if (attackNumber == 2) {
                if (attackHits && priorAttackHits) {
                    // Take extra dice of damage
                    int extraDamage = D8.roll();
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
