package com.redshift.ShadowDarkCalculator.conditions;

import com.redshift.ShadowDarkCalculator.creatures.Creature;

/**
 * Conditions are added to creatures to have some effect. Conditions can restrict a creature to act, take damage, or
 * have a beneficial effect. Conditions can be timed or be removed by other causes. For example the sleeping condition
 * could be lost when the creature is damaged.
 */

public interface Condition {

    /**
     * Does this condition apply to dead creatures?
     */

    boolean appliesToDeadCreatures();

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
