package com.redshift.ShadowDarkCalculator.dice;

import java.util.Random;

/**
 * Singleton access to a random number generator.
 */

public class RandomAccess {

    public static Random RANDOM = new Random();

    /**
     * Allow the random number generator to be reset; usually after it's seed was
     * set, used, and now we want it randomly reset.
     */

    public static void reset() {
        RANDOM = new Random();
    }
}
