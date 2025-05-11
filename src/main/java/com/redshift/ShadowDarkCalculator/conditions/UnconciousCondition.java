package com.redshift.ShadowDarkCalculator.conditions;

import com.redshift.ShadowDarkCalculator.creatures.Creature;
import lombok.extern.slf4j.Slf4j;

@Slf4j
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
        log.info("{} is unconscious and skipping their turn.", creature.getName());
        // Has no effect... other than you most likely will be dead...
    }

}
