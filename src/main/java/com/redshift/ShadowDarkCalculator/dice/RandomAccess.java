package com.redshift.ShadowDarkCalculator.dice;

import java.util.Random;

/**
 * Singleton access to a random number generator.
 */

public class RandomAccess {

    public static Random RANDOM = new Random();

    public static void reset() {
        RANDOM = new Random();
    }
}
