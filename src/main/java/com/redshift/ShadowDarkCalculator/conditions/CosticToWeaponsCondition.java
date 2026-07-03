package com.redshift.ShadowDarkCalculator.conditions;

import com.redshift.ShadowDarkCalculator.creatures.Creature;

/**
 * A condition that marks a creature as being costic to weapons and may destroy them.
 */
public class CosticToWeaponsCondition implements Condition {

    @Override
    public boolean appliesToDeadCreatures() {
        return true;
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
        // Nothing done
    }

    @Override
    public boolean hasEnded(Creature creature) {
        return false;
    }

    @Override
    public void perform(Creature creature) {
        // Nothing to do... see Weapon.
    }

}
