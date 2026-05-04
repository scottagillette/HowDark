package com.redshift.ShadowDarkCalculator.creatures.monsters.beasts;

import com.redshift.ShadowDarkCalculator.actions.weapons.Weapon;
import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.creatures.Stats;
import com.redshift.ShadowDarkCalculator.creatures.monsters.Monster;
import com.redshift.ShadowDarkCalculator.dice.Dice;
import com.redshift.ShadowDarkCalculator.dice.RollModifier;
import lombok.extern.slf4j.Slf4j;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D4;
import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D8;

/**
 * Cunning rats as large as cats. Mangy fur and wormlike tails.
 * AC 11, HP 5, ATK 1 bite +1 (1d4 + disease), MV near
 * S -2, D +1, C +1, I -2, W +1, Ch -2, AL N, LV 1
 * Disease. DC 12 CON or 1d4 CON damage (can't heal while ill).
 * Repeat check once per day; ends on success. Die at 0 CON.
 */

@Slf4j
public class GiantRat extends Monster {

    public GiantRat(String name) {
        super(
                name,
                1,
                new Stats(6,12,12,6,12,6),
                11,
                D8.roll() + 1,
                new DiseasedBite()
        );

    }

    private static class DiseasedBite extends Weapon {

        private DiseasedBite() {
            super("Diseased Bite", D4, RollModifier.DEXTERITY);
            addPiercing();
        }

        @Override
        protected boolean performSingleTargetAttack(Creature actor, Creature target, String weaponName, Dice damageDice, RollModifier rollModifier) {
            final boolean attackHits = super.performSingleTargetAttack(actor, target, weaponName, damageDice, rollModifier);

            if (attackHits) {
                if (target.getStats().constitutionSave(12)) {
                    log.info("{} resists the disease.", target.getName());
                } else {
                    int constitutionRemaining = target.getStats().constitutionDrain(D4);
                    if (constitutionRemaining == 0) {
                        log.info("{} is drained of constitution and DIES!", target.getName());
                        target.setDead(true);
                    } else {
                        log.info("{} is drained of constitution to {}", target.getName(), constitutionRemaining);
                    }
                }
            }

            return attackHits;
        }
    }
}
