package com.redshift.ShadowDarkCalculator.conditions;

import com.redshift.ShadowDarkCalculator.creatures.Creature;

/**
 * Can't heal while diseased.
 */

public class DiseasedCondition implements Condition {

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

    }

    @Override
    public boolean hasEnded(Creature creature) {
        return false; // Doesn't end unless cure disease...which isn't implemented yet
    }

    @Override
    public void perform(Creature creature) {
        // See Creature.healDamage()
    }
}
