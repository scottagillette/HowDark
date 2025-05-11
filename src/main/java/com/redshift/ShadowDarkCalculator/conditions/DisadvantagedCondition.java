package com.redshift.ShadowDarkCalculator.conditions;

import com.redshift.ShadowDarkCalculator.creatures.Creature;
import lombok.Getter;

@Getter
public class DisadvantagedCondition implements Condition {

    @Override
    public boolean canAct() {
        return true;
    }

    @Override
    public boolean hasEnded(Creature creature) {
        return false;
    }

    @Override
    public void perform(Creature creature) {
        // does nothing... see
    }
}
