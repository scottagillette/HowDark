package com.redshift.ShadowDarkCalculator.creatures.monsters.beasts;

import com.redshift.ShadowDarkCalculator.actions.weapons.Weapon;
import com.redshift.ShadowDarkCalculator.conditions.StuckToCondition;
import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.creatures.CreatureLabel;
import com.redshift.ShadowDarkCalculator.creatures.Stats;
import com.redshift.ShadowDarkCalculator.creatures.monsters.Monster;
import com.redshift.ShadowDarkCalculator.dice.RollModifier;
import com.redshift.ShadowDarkCalculator.encounter.Encounter;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.*;
import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D8;

/**
 * Darting, orange insect-bat with four wings and needle like beak.
 * AC 12, HP 4, ATK 1 beak +2 (1d4 + blood drain), MV near (fly)
 * S -2, D +2, C +0, I -2, W +0, Ch -2, AL N, LV 1
 * Blood Drain. Attach to bitten target; auto-hit the next round.
 * DC 9 STR on turn to remove.
 */

@Getter
@Setter
@Slf4j
public class StingBat extends Monster {

    private Creature currentStuckTarget;
    private StuckToCondition currentStuckCondition;

    public StingBat(String name) {
        super(
                name,
                1,
                new Stats(6,14,10,6,10,6),
                12,
                D8.roll(), // Usually use a D8 for random first level mob.
                new Bite()
        );
        getLabels().add(CreatureLabel.NEUTRAL);
    }

    private static class Bite extends Weapon {

        private Bite() {
            super("Bite", D4, RollModifier.DEXTERITY);
            addPiercing();
        }

        @Override
        public void perform(Creature actor, List<Creature> enemies, List<Creature> allies, Encounter encounter) {
            final StingBat stingBat = (StingBat)actor;

            if (stingBat.getCurrentStuckTarget() == null) {
                final Creature target = actor.getSingleTargetSelector().get(enemies);

                if (target == null) {
                    log.info("{} is skipping their turn... no target!", actor.getName());
                } else {
                    boolean attackHit = performSingleTargetAttack(actor, target);

                    if (attackHit) {
                        log.info("{} is stuck to {}!", stingBat.getName(), target.getName());
                        final StuckToCondition stuckToCondition = new StuckToCondition(9);
                        stingBat.setCurrentStuckTarget(target); // A new stuck target!
                        stingBat.setCurrentStuckCondition(stuckToCondition);
                        target.addCondition(stuckToCondition);
                    }
                }
            } else {
                if (stingBat.getCurrentStuckTarget().isDead() || stingBat.getCurrentStuckTarget().isUnconscious()) {
                    stingBat.setCurrentStuckTarget(null); // Find a new target is the current is dead or unconscious.
                    stingBat.setCurrentStuckCondition(null);
                    log.info("{} finds a new target!", actor.getName());
                    perform(actor, enemies, allies, encounter);
                } else {
                    if (stingBat.getCurrentStuckTarget().hasCondition(stingBat.currentStuckCondition)) {
                        final int damage = damageDice.roll();
                        log.info("{} AUTO-HITS on stuck target {} for {} damage!", actor.getName(), stingBat.getCurrentStuckTarget().getName(), damage);
                        stingBat.getCurrentStuckTarget().takeDamage(damage, damageType);
                    } else {
                        stingBat.setCurrentStuckTarget(null); // Find a new target the current one broke free!
                        stingBat.setCurrentStuckCondition(null);
                        perform(actor, enemies, allies, encounter);
                    }
                }
            }
        }

    }
}
