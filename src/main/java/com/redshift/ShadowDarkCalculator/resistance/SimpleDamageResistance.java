package com.redshift.ShadowDarkCalculator.resistance;

/**
 * Damage resistance that applied to simple damage types that are non-silvered and non-magical.
 */

public class SimpleDamageResistance implements DamageResistance {

    @Override
    public int takeDamage(int damage, boolean silvered, boolean magical) {
        if (silvered || magical) {
            return damage;
        } else {
            return 0;
        }
    }

}
