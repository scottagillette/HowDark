package com.redshift.ShadowDarkCalculator.conditions;

import com.redshift.ShadowDarkCalculator.creatures.Creature;

public interface Condition {

    /**
     * Returns true if the creature can act with this condition.
     */

    boolean canAct();

    /**
     * Ticks down any counters and checks to see if this condition has ended.
     */

    boolean hasEnded(Creature creature);

    /**
     * Applies the effect to the specified creature.
     */

    void perform(Creature creature);
}
