package com.redshift.ShadowDarkCalculator.dice;

import java.util.Random;

/**
 * Common dice class used to roll standard and custom sided dice.
 */

public class SingleDie implements Dice {

    public static final SingleDie D1  = new SingleDie(1);
    public static final SingleDie D2  = new SingleDie(2);
    public static final SingleDie D3  = new SingleDie(3);
    public static final SingleDie D4  = new SingleDie(4);
    public static final SingleDie D6  = new SingleDie(6);
    public static final SingleDie D8  = new SingleDie(8);
    public static final SingleDie D10 = new SingleDie(10);
    public static final SingleDie D12 = new SingleDie(12);
    public static final SingleDie D20 = new SingleDie(20);

    private final int sides;

    public SingleDie(int sides) {
        this.sides = sides;
    }

    @Override
    public int roll() {
        if (sides == 0) return 0;
        return RandomAccess.RANDOM.nextInt(sides) + 1;
    }

}
