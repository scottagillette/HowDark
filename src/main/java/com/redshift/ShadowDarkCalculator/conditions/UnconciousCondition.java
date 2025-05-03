package com.redshift.ShadowDarkCalculator.conditions;

import com.redshift.ShadowDarkCalculator.creatures.Creature;

public class UnconciousCondition implements Condition {

    @Override
    public boolean canAct() {
        return false;
    }

    @Override
    public boolean hasEnded(Creature creature) {
        return false; // Only ends on healing to the creature; see Creature.healDamage()
    }

    @Override
    public void perform(Creature creature) {
        // Has no effect... other than you most likely will be dead...
    }

}
