package com.redshift.ShadowDarkCalculator.creatures.undead;

import com.redshift.ShadowDarkCalculator.actions.PerformAllAction;
import com.redshift.ShadowDarkCalculator.actions.weapons.Weapon;
import com.redshift.ShadowDarkCalculator.creatures.BaseCreature;
import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.creatures.Stats;
import com.redshift.ShadowDarkCalculator.dice.RollModifier;
import com.redshift.ShadowDarkCalculator.targets.RandomTargetSelector;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.*;

@Slf4j
public class Shadow extends BaseCreature {

    public Shadow(String name) {
        super(
                name,
                3,
                true,
                true,
                new Stats(3, 14, 14, 6, 10, 12),
                12,
                D8.roll() + D8.roll() + D8.roll() + 2,
                new PerformAllAction(new DraiingTouch(), new DraiingTouch()),
                new RandomTargetSelector()
        );
    }

    public static class DraiingTouch extends Weapon {

        public DraiingTouch() {
            super("Draining Touch", D4, RollModifier.DEXTERITY);
        }

        @Override
        public void perform(Creature actor, List<Creature> enemies, List<Creature> allies) {
            final Creature target = actor.getTargetSelector().get(enemies);

            if (target == null) {
                log.info(actor.getName() + " is skipping their turn... no target!");
            } else {
                boolean attackHits = performSingleTargetAttack(actor, target, name, dice, rollModifier);

                if (attackHits) {
                    final int currentStrength = target.getStats().strengthDrain(D1);
                    if (currentStrength == 0) {
                        log.info(target.getName() + " is drained of strength to " + currentStrength + " and DIES!");
                        target.setDead(true);
                    } else {
                        log.info(target.getName() + " is drained of strength to " + currentStrength);
                    }
                }
            }
        }
    }
}