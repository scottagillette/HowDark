package com.redshift.ShadowDarkCalculator.creatures.undead;

import com.redshift.ShadowDarkCalculator.actions.PerformAllAction;
import com.redshift.ShadowDarkCalculator.actions.weapons.Weapon;
import com.redshift.ShadowDarkCalculator.actions.weapons.WeaponBuilder;
import com.redshift.ShadowDarkCalculator.creatures.*;
import com.redshift.ShadowDarkCalculator.dice.RollModifier;
import com.redshift.ShadowDarkCalculator.dice.ZeroDice;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.*;

@Slf4j
public class Wight extends UndeadMonster {

    public Wight(String name) {
        super(
                name,
                3,
                new Stats(17,12,14,12,10,17),
                14,
                D8.roll() + D8.roll() + D8.roll() + 2,
                new PerformAllAction(WeaponBuilder.BASTARD_SWORD_2H.build(), new LifeDrain()
        ));
        getLabels().add(Label.BRUTE);
    }

    @Override
    public void takeDamage(int amount, boolean silvered, boolean magical, boolean fire, boolean cold) {
        // Take only silvered or magical damage!
        final boolean takeDamage = silvered || magical;

        if (takeDamage) {
            super.takeDamage(amount, silvered, magical, fire, cold);
        } else {
            log.info(getName() + " takes no damage from non-silvered, non-magical damage!");
        }
    }

    private static class LifeDrain extends Weapon {

        public LifeDrain() {
            // Life drain does no damage but only drains life!
            super("Life Drain", new ZeroDice(), RollModifier.STRENGTH);
        }

        @Override
        public void perform(Creature actor, List<Creature> enemies, List<Creature> allies) {
            final Creature target = actor.getSingleTargetSelector().get(enemies);

            if (target == null) {
                log.info(actor.getName() + " is skipping their turn... no target!");
            } else {
                boolean attackHits = performSingleTargetAttack(actor, target, name, dice, rollModifier);

                if (attackHits) {
                    int constitutionRemaining = target.getStats().constitutionDrain(D4);
                    if ( constitutionRemaining == 0) {
                        log.info(target.getName() + " is drained of constitution to " + constitutionRemaining + " and DIES!");
                        target.setDead(true);
                    } else {
                        log.info(target.getName() + " is drained of constitution to " + constitutionRemaining);
                    }
                }
            }
        }
    }

}