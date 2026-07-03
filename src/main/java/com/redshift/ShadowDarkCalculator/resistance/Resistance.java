package com.redshift.ShadowDarkCalculator.resistance;

import com.redshift.ShadowDarkCalculator.actions.DamageType;
import com.redshift.ShadowDarkCalculator.creatures.Creature;

/**
 * Implementations determine what damage is resisted partially, fully or not at all.
 */

public interface Resistance {

    /**
     * Given the creature determine how much damage, if any, is applied.
     */

    int calculateDamage(Creature creature, int amount, DamageType damageType);
}
