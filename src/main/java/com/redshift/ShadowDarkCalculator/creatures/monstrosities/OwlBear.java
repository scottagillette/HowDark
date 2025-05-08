package com.redshift.ShadowDarkCalculator.creatures.monstrosities;

import com.redshift.ShadowDarkCalculator.actions.PerformAllAction;
import com.redshift.ShadowDarkCalculator.actions.weapons.Weapon;
import com.redshift.ShadowDarkCalculator.creatures.BaseCreature;
import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.creatures.Stats;
import com.redshift.ShadowDarkCalculator.dice.Dice;
import com.redshift.ShadowDarkCalculator.dice.RollModifier;
import lombok.extern.slf4j.Slf4j;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.*;

@Slf4j
public class OwlBear extends BaseCreature {

    public OwlBear(String name) {
        super(
                name,
                6,
                new Stats(18,12,17,7,14,5),
                13,
                D8.roll() + D8.roll() + D8.roll() + D8.roll() + D8.roll() + D8.roll() + 3,
                new PerformAllAction(new OwlBear.Claw(), new OwlBear.Claw())
        );
    }

    public static class Claw extends Weapon {

        private static boolean priorAttackHits = false;
        private static boolean priorAttack = false;

        public Claw() {
            super("Claw", D10, RollModifier.STRENGTH, 1);
        }

        @Override
        protected boolean performSingleTargetAttack(Creature actor, Creature target, String weaponName, Dice damageDice, RollModifier rollModifier) {
            boolean attackHits = super.performSingleTargetAttack(actor, target, weaponName, damageDice, rollModifier);

            if (priorAttack) {
                if (attackHits && priorAttackHits) {
                    // Take extra dice of damage
                    int extraDamage = D10.roll();
                    log.info(target.getName() + " takes extra crushing damage of " + extraDamage);
                    target.takeDamage(extraDamage, false, false, false, false);
                }
                priorAttack = false; // Reset
                priorAttackHits = false;
            } else {
                priorAttack = true; // This is first attack
                priorAttackHits = attackHits;
            }

            return attackHits;
        }
    }

}
