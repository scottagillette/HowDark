package com.redshift.ShadowDarkCalculator.creatures.undead;

import com.redshift.ShadowDarkCalculator.actions.PerformAllActions;
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
                new PerformAllActions(WeaponBuilder.BASTARD_SWORD_2H.build(), new LifeDrain()
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
            log.info("{} takes no damage from non-silvered, non-magical damage!", getName());
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
                log.info("{} is skipping their turn... no target!", actor.getName());
            } else {
                boolean attackHits = performSingleTargetAttack(actor, target, name, dice, rollModifier);

                if (attackHits) {
                    int constitutionRemaining = target.getStats().constitutionDrain(D4);
                    if ( constitutionRemaining == 0) {
                        log.info("{} is drained of constitution to {} and DIES!", target.getName(), constitutionRemaining);
                        target.setDead(true);
                    } else {
                        log.info("{} is drained of constitution to {}", target.getName(), constitutionRemaining);
                    }
                }
            }
        }
    }

}