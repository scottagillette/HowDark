package com.redshift.ShadowDarkCalculator.creatures.undead;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.*;

import com.redshift.ShadowDarkCalculator.actions.PerformAllAction;
import com.redshift.ShadowDarkCalculator.actions.weapons.Weapon;
import com.redshift.ShadowDarkCalculator.creatures.*;
import com.redshift.ShadowDarkCalculator.dice.RollModifier;
import com.redshift.ShadowDarkCalculator.targets.RandomTargetSelector;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Shadow extends UndeadMonster {

    public Shadow(String name) {
        super(
            name,
            3,
            new Stats(3, 14, 14, 6, 10, 12),
            12,
            D8.roll() + D8.roll() + D8.roll() + 2,
            new PerformAllAction(new DrainingTouch(), new DrainingTouch()),
            new RandomTargetSelector()
        );
        getLabels().add(Label.BRUTE);
    }

    public static class DrainingTouch extends Weapon {

        public DrainingTouch() {
            super("Draining Touch", D4, RollModifier.DEXTERITY);
        }

        @Override
        public void perform(
            Creature actor,
            List<Creature> enemies,
            List<Creature> allies
        ) {
            final Creature target = actor
                .getSingleTargetSelector()
                .get(enemies);

            if (target == null) {
                log.info(
                    actor.getName() + " is skipping their turn... no target!"
                );
            } else {
                boolean attackHits = performSingleTargetAttack(
                    actor,
                    target,
                    name,
                    dice,
                    rollModifier,
                    actor.isDisadvantaged()
                );

                if (attackHits) {
                    final int currentStrength = target
                        .getStats()
                        .strengthDrain(D1);
                    if (currentStrength == 0) {
                        log.info(
                            target.getName() +
                            " is drained of strength to " +
                            currentStrength +
                            " and DIES!"
                        );
                        target.setDead(true);
                    } else {
                        log.info(
                            target.getName() +
                            " is drained of strength to " +
                            currentStrength
                        );
                    }
                }
            }
        }
    }
}
