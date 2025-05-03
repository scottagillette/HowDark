package com.redshift.ShadowDarkCalculator.dice;

import java.util.List;

public class MultipleDice implements Dice {

    private final List<Dice> diceSet;
    private final int bonus;

    public MultipleDice(List<Dice> diceSet) {
        this(diceSet, 0);
    }

    public MultipleDice(List<Dice> diceSet, int bonus)  {
        this.diceSet = diceSet;
        this.bonus = bonus;
    }

    @Override
    public int roll() {
        int result = bonus;

        for (Dice dice : diceSet) {
            result = result + dice.roll();
        }

        return result;
    }
}
