package com.redshift.ShadowDarkCalculator.conditions;

import com.redshift.ShadowDarkCalculator.creatures.Creature;

public class SleepingCondition implements Condition {

    @Override
    public boolean canAct() {
        return false;
    }

    @Override
    public boolean hasEnded(Creature creature) {
        return false; // Only ends on damage...
    }

    @Override
    public void perform(Creature creature) {
        // No ill effects... other than you are just snoring!
    }

}
