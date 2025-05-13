package com.redshift.ShadowDarkCalculator.dice;

import java.util.Random;

/**
 * Common dice class used to roll standard and custom sided dice.
 */

public class SingleDie implements Dice {

    public static final SingleDie D1  = new SingleDie(1);
    public static final SingleDie D4  = new SingleDie(4);
    public static final SingleDie D5  = new SingleDie(5);
    public static final SingleDie D6  = new SingleDie(6);
    public static final SingleDie D8  = new SingleDie(8);
    public static final SingleDie D10 = new SingleDie(10);
    public static final SingleDie D12 = new SingleDie(12);
    public static final SingleDie D20 = new SingleDie(20);

    private static final Random random = new Random();

    private final int sides;
    private final int bonus;

    public SingleDie(int sides) {
        this(sides, 0);
    }

    public SingleDie(int sides, int bonus) {
        this.sides = sides;
        this.bonus = bonus;
    }

    @Override
    public int roll() {
        return random.nextInt(sides) + 1 + bonus;
    }

}
