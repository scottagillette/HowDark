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

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D4;
import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D8;

/**
 * A glossy black, blood-drinking slug as large as a cat.
 * AC 9, HP 10, ATK 1 bite +1 (1d4 + attach), MV near (swim)
 * S +1, D -1, C +1, I -3, W -1, Ch -3, AL N, LV 2
 * Attach. Attach to target; bite auto-hits next round. DC 12 STR on turn to tear off.
 */

@Getter
@Setter
@Slf4j
public class GiantLeech extends Monster {

    private Creature currentStuckTarget;
    private StuckToCondition currentStuckCondition;

    public GiantLeech(String name) {
        super(
                name,
                2,
                new Stats(12,8,12,4,8,4),
                9,
                D8.roll() + D8.roll() + 1,
                new Bite()
        );
        getLabels().add(CreatureLabel.NEUTRAL);
    }

    private static class Bite extends Weapon {

        private Bite() {
            super("Bite", D4, RollModifier.STRENGTH);
            addPiercing();
        }

        @Override
        public void perform(Creature actor, List<Creature> enemies, List<Creature> allies, Encounter encounter) {
            final GiantLeech giantLeech = (GiantLeech)actor;

            if (giantLeech.getCurrentStuckTarget() == null) {
                final Creature target = actor.getSingleTargetSelector().get(enemies);

                if (target == null) {
                    log.info("{} is skipping their turn... no target!", actor.getName());
                } else {
                    boolean attackHit = performSingleTargetAttack(actor, target);

                    if (attackHit) {
                        log.info("{} is stuck to {}!", giantLeech.getName(), target.getName());
                        final StuckToCondition stuckToCondition = new StuckToCondition(9);
                        giantLeech.setCurrentStuckTarget(target); // A new stuck target!
                        giantLeech.setCurrentStuckCondition(stuckToCondition);
                        target.addCondition(stuckToCondition);
                    }
                }
            } else {
                if (giantLeech.getCurrentStuckTarget().isDead() || giantLeech.getCurrentStuckTarget().isUnconscious()) {
                    giantLeech.setCurrentStuckTarget(null); // Find a new target is the current is dead or unconscious.
                    giantLeech.setCurrentStuckCondition(null);
                    log.info("{} finds a new target!", actor.getName());
                    perform(actor, enemies, allies, encounter);
                } else {
                    if (giantLeech.getCurrentStuckTarget().hasCondition(giantLeech.currentStuckCondition)) {
                        final int damage = damageDice.roll();
                        log.info("{} AUTO-HITS on stuck target {} for {} damage!", actor.getName(), giantLeech.getCurrentStuckTarget().getName(), damage);
                        giantLeech.getCurrentStuckTarget().takeDamage(damage, damageType);
                    } else {
                        giantLeech.setCurrentStuckTarget(null); // Find a new target the current one broke free!
                        giantLeech.setCurrentStuckCondition(null);
                        perform(actor, enemies, allies, encounter);
                    }
                }
            }
        }

    }

}
