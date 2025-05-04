package com.redshift.ShadowDarkCalculator.dice;

/**
 * A dice that magically always returns zero!
 */

public class ZeroDice implements Dice {

    @Override
    public int roll() {
        return 0;
    }

}
