package com.redshift.ShadowDarkCalculator.conditions;

import com.redshift.ShadowDarkCalculator.creatures.Creature;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SleepingCondition implements Condition {

    @Override
    public boolean canAct() {
        return false;
    }

    @Override
    public boolean hasEnded(Creature creature) {
        return false; // Only ends on damage...
    }

    @Override
    public void perform(Creature creature) {
        log.info(creature.getName() + " is sleeping and skipping their turn.");
        // No ill effects... other than you are just snoring!
    }

}
