package com.redshift.ShadowDarkCalculator.dice;

/**
 * A die that magically always returns zero!
 */

public class ZeroDice implements Dice {

    @Override
    public int roll() {
        return 0;
    }

}
