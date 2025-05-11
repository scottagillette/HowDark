package com.redshift.ShadowDarkCalculator.creatures.undead;

import com.redshift.ShadowDarkCalculator.actions.PerformAllAction;
import com.redshift.ShadowDarkCalculator.actions.weapons.Weapon;
import com.redshift.ShadowDarkCalculator.creatures.*;
import com.redshift.ShadowDarkCalculator.dice.RollModifier;
import com.redshift.ShadowDarkCalculator.targets.RandomTargetSelector;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.*;

@Slf4j
public class VampireSpawn extends UndeadMonster {

    public VampireSpawn(String name) {
        super(
                name,
                5,
                new Stats(16,14,16,9,12,14),
                13,
                D8.roll() + D8.roll() + D8.roll() + D8.roll() + D8.roll() + 3,
                new PerformAllAction(new Bite(), new Bite()),
                new RandomTargetSelector()
        );
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

    private static class Bite extends Weapon {

        public Bite() {
            super("Bite", D8, RollModifier.STRENGTH, 1);
        }

        @Override
        public void perform(Creature actor, List<Creature> enemies, List<Creature> allies) {
            final Creature target = actor.getSingleTargetSelector().get(enemies);

            if (target == null) {
                log.info("{} is skipping their turn... no target!", actor.getName());
            } else {
                boolean attackHits = performSingleTargetAttack(actor, target, name, dice, rollModifier);

                if (attackHits) {
                    // Heal 2d6
                    actor.healDamage(D6.roll() + D6.roll());

                    // Loose 1d4 CON
                    int constitutionRemaining = target.getStats().constitutionDrain(D4);
                    if (constitutionRemaining == 0) {
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
