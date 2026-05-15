package com.redshift.ShadowDarkCalculator.resistance;

import com.redshift.ShadowDarkCalculator.actions.DamageType;
import com.redshift.ShadowDarkCalculator.creatures.Creature;
import lombok.extern.slf4j.Slf4j;

/**
 * Take damage only from magical sources.
 */

@Slf4j
public class MagicalOnlyResistance implements Resistance {

    @Override
    public void takeDamage(Creature creature, int amount, DamageType damageType) {
        if (damageType.isMagical()) {
            creature.takeDamage(amount, damageType);
        } else {
            log.info("{} takes no damage from non-magical damage!", creature.getName());
        }
    }
}
