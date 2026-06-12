package com.redshift.ShadowDarkCalculator.conditions;

import com.redshift.ShadowDarkCalculator.creatures.Creature;
import lombok.Getter;

/**
 * Paladin inspiration, Dying roll 18-20 instead of 20.
 */

@Getter
public class InspiredCondition implements Condition {

    private final int hpRecovered;

    public InspiredCondition(int hpRecovered) {
        this.hpRecovered = hpRecovered;
    }

    @Override
    public boolean appliesToDeadCreatures() {
        return false;
    }

    @Override
    public boolean canAct() {
        return true;
    }

    @Override
    public void end() {
        // No specific behavior
    }

    @Override
    public boolean hasEnded(Creature creature) {
        return false;
    }

    @Override
    public void perform(Creature creature) {
        // See dying condition.
    }

}
