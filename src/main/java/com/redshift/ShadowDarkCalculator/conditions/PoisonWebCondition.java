package com.redshift.ShadowDarkCalculator.conditions;

import com.redshift.ShadowDarkCalculator.creatures.Creature;
import lombok.extern.slf4j.Slf4j;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D4;

/**
 * Takes D4 damage each round unless it makes a DEX save.
 */

@Slf4j
public class PoisonWebCondition implements Condition {

    @Override
    public boolean canAct() {
        return false; // Stuck in the Web!
    }

    @Override
    public boolean hasEnded(Creature creature) {
        boolean saves = false;

        boolean canCheck = !creature.hasCondition(DyingCondition.class.getName()) &&
                !creature.hasCondition(UnconciousCondition.class.getName()) &&
                !creature.hasCondition(ParalyzedCondition.class.getName());

        if (canCheck) {
            saves = creature.getStats().dexteritySave(12);
            if (saves) {
                log.info("{} escapes the poison web!", creature.getName());
            }
        } else {
            log.info("{} cannot attempt to escape the poison web!", creature.getName());
        }

        return saves;
    }

    @Override
    public void perform(Creature creature) {
        final int damage = D4.roll();
        log.info("{} takes poison damage of {}", creature.getName(), damage);
        creature.takeDamage(damage, false, false, false, false);
    }
}