package com.redshift.ShadowDarkCalculator.resistance;

import com.redshift.ShadowDarkCalculator.actions.DamageType;
import com.redshift.ShadowDarkCalculator.creatures.Creature;
import lombok.extern.slf4j.Slf4j;

/**
 * Resistance to piercing damage.
 */

@Slf4j
public class PiercingResistance implements Resistance {

    @Override
    public int calculateDamage(Creature creature, int amount, DamageType damageType) {
        if (damageType.isPiercing()) {
            log.info("{} seems to take less damage than normal from piercing!", creature.getName());
            return (amount / 2);
        } else {
            return amount;
        }
    }

}
