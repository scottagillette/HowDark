package com.redshift.ShadowDarkCalculator.resistance;

import com.redshift.ShadowDarkCalculator.actions.DamageType;
import com.redshift.ShadowDarkCalculator.creatures.Creature;
import lombok.extern.slf4j.Slf4j;

/**
 * Take damage only from silvered or magical sources.
 */

@Slf4j
public class SilveredOrMagicalOnlyResistance implements Resistance {

    @Override
    public int calculateDamage(Creature creature, int amount, DamageType damageType) {
        // Take only silvered or magical damage!
        final boolean takeDamage = damageType.isSilvered() || damageType.isMagical();

        if (takeDamage) {
            return amount;
        } else {
            log.info("{} takes no damage from non-silvered, non-magical damage!", creature.getName());
            return 0;
        }
    }

}
