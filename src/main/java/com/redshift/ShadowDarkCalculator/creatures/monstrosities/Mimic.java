package com.redshift.ShadowDarkCalculator.creatures.monstrosities;

import com.redshift.ShadowDarkCalculator.actions.PerformAllActions;
import com.redshift.ShadowDarkCalculator.actions.weapons.Weapon;
import com.redshift.ShadowDarkCalculator.conditions.StuckToCondition;
import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.creatures.Monster;
import com.redshift.ShadowDarkCalculator.creatures.Stats;
import com.redshift.ShadowDarkCalculator.dice.RollModifier;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D8;

@Getter
@Setter
@Slf4j
public class Mimic extends Monster {

    private Creature currentStuckTarget;
    private StuckToCondition currentStuckCondition;

    public Mimic(String name) {
        super(
                name,
                5,
                new Stats(14, 10, 12, 6, 10, 3),
                12,
                D8.roll() + D8.roll() + D8.roll() + D8.roll() + D8.roll() + 1,
                new PerformAllActions(new Bite(), new Bite())
        );
    }

    private static class Bite extends Weapon {

        private Bite() {
            super("Bite", D8, RollModifier.STRENGTH, 3);
        }

        @Override
        public void perform(Creature actor, List<Creature> enemies, List<Creature> allies) {
            final Mimic mimic = (Mimic)actor;

            if (mimic.getCurrentStuckTarget() == null) {
                final Creature target = actor.getSingleTargetSelector().get(enemies);

                if (target == null) {
                    log.info("{} is skipping their turn... no target!", actor.getName());
                } else {
                    boolean attackHit = performSingleTargetAttack(actor, target, name, dice, rollModifier);

                    if (attackHit) {
                        // Check for stuck DC15 STR check
                        final boolean saved = !target.isUnconscious() && target.getStats().strengthSave(15);

                        if (saved) {
                            log.info("{} isn't stuck by sticky Mimic tongue", target.getName());
                        } else {
                            log.info("{} IS STUCK by sticky Mimic tongue!", target.getName());
                            final StuckToCondition stuckToCondition = new StuckToCondition(15);
                            mimic.setCurrentStuckTarget(target); // A new stuck target!
                            mimic.setCurrentStuckCondition(stuckToCondition);
                            target.addCondition(stuckToCondition);
                        }
                    }
                }
            } else {
                if (mimic.getCurrentStuckTarget().isDead() || mimic.getCurrentStuckTarget().isUnconscious()) {
                    mimic.setCurrentStuckTarget(null); // Find a new target is the current is dead or unconscious.
                    mimic.setCurrentStuckCondition(null);
                    log.info("{} finds a new target!", actor.getName());
                    perform(actor, enemies, allies);
                } else {
                    if (mimic.getCurrentStuckTarget().hasCondition(mimic.currentStuckCondition)) {
                        final int damage = D8.roll();
                        log.info("{} AUTO-HITS on stuck target {} for {} damage!", actor.getName(), mimic.getCurrentStuckTarget().getName(), damage);
                        mimic.getCurrentStuckTarget().takeDamage(damage, false, false, false, false);
                    } else {
                        mimic.setCurrentStuckTarget(null); // Find a new target the current one broke free!
                        mimic.setCurrentStuckCondition(null);
                        perform(actor, enemies, allies);
                    }
                }
            }
        }

    }
}
