package com.redshift.ShadowDarkCalculator.conditions;

import com.redshift.ShadowDarkCalculator.creatures.Creature;

/**
 * A condition that indicates you have a protection from evil.
 */

public class ProtectionFromEvilCondition implements Condition {

    @Override
    public boolean appliesToDeadCreatures() {
        return false;
    }

    @Override
    public boolean canAct() {
        return true;
    }

    @Override
    public boolean canMove() {
        return true;
    }

    @Override
    public void end() {

    }

    @Override
    public boolean hasEnded(Creature creature) {
        return false;
    }

    @Override
    public void perform(Creature creature) {
        // Does nothing here... see spell cast and weapon attacks.
    }

}
