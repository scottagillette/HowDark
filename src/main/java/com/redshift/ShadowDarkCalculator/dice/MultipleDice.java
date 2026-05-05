package com.redshift.ShadowDarkCalculator.dice;

import java.util.ArrayList;
import java.util.List;

/**
 * Implements the Dice interface and can roll one or more dice with a bonus.
 */

public class MultipleDice implements Dice {

    private final List<Dice> diceSet;

    public MultipleDice(SingleDie... dice) {
        this.diceSet = List.of(dice);
    }

    public MultipleDice(SingleDie dice, int number) {
        List<Dice> multipleDice = new ArrayList<>();

        for (int i = 0; i < number; i++) {
            multipleDice.add(dice);
        }

        diceSet = multipleDice;
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
