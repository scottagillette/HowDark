package com.redshift.ShadowDarkCalculator.resistance;

public interface DamageResistance {

    /**
     * Calculate how much damage is taken.
     */

    int takeDamage(int damage, boolean silvered, boolean magical);

}
