package com.redshift.ShadowDarkCalculator.conditions;

import com.redshift.ShadowDarkCalculator.creatures.Creature;

public class ParalyzedCondition implements Condition {

    private int rounds;

    public ParalyzedCondition(int rounds) {
        this.rounds = rounds + 1; // Since we check at the beginning of the creatures turn... add 1.
    }

    @Override
    public boolean canAct() {
        return false;
    }

    @Override
    public boolean hasEnded(Creature creature) {
        rounds = Math.max(0, rounds - 1);
        return (rounds == 0);
    }

    @Override
    public void perform(Creature creature) {
        // No adverse effect... other than you cant act!
    }

}
