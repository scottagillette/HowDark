package com.redshift.ShadowDarkCalculator.conditions;

import com.redshift.ShadowDarkCalculator.creatures.Creature;

/**
 * Devoured and no longer NOT in a stomach!
 */

public class DevouredCondition implements Condition {

    @Override
    public boolean appliesToDeadCreatures() {
        return true;
    }

    @Override
    public boolean canAct() {
        return false;
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
        // Nothing to do when dead and devoured!
    }
}
