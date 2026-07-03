package com.redshift.ShadowDarkCalculator.resistance;

import com.redshift.ShadowDarkCalculator.actions.DamageType;
import com.redshift.ShadowDarkCalculator.creatures.Creature;
import lombok.extern.slf4j.Slf4j;

/**
 * Take no damage from lightning!
 */

@Slf4j
public class LightningImmunity implements Resistance {

    @Override
    public int calculateDamage(Creature creature, int amount, DamageType damageType) {
        if (damageType.isLightning()) {
            log.info("{} seems to take no damage from lightning!", creature.getName());
            return 0;
        } else {
            return amount;
        }
    }
}
