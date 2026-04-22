package com.redshift.ShadowDarkCalculator.dice;

import java.util.List;

/**
 * Implements the Dice interface and can roll one or more dice with a bonus.
 */

public class MultipleDice implements Dice {

    private final List<Dice> diceSet;

    public MultipleDice(SingleDie... dice) {
        this.diceSet = List.of(dice);
    }

    @Override
    public int roll() {
        int result = 0;

        for (Dice dice : diceSet) {
            result = result + dice.roll();
        }

        return result;
    }
}
