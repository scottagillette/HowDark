package com.redshift.ShadowDarkCalculator.conditions;

import com.redshift.ShadowDarkCalculator.creatures.Creature;

/**
 * Creatures attacking creatures with this condition have disadvantage.
 */

public class DisadvantageToAttackCondition implements Condition {

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
        // See Weapon
    }
}
