package com.redshift.ShadowDarkCalculator.conditions;

import com.redshift.ShadowDarkCalculator.creatures.Creature;
import lombok.extern.slf4j.Slf4j;

/**
 * Blinded condition that gives disadvantage to an action each round.
 */

@Slf4j
public class BlindedCondition implements Condition {

    private int rounds;

    public BlindedCondition(int rounds) {
        this.rounds = rounds + 1;
    }

    @Override
    public boolean appliesToDeadCreatures() {
        return false;
    }

    @Override
    public boolean canAct() {
        return true;
    }

    @Override
    public boolean hasEnded(Creature creature) {
        rounds = Math.max(0, rounds - 1);
        if (rounds == 0) {
            log.info("{} is no longer blinded!", creature.getName());
        }
        return (rounds == 0);
    }

    @Override
    public void perform(Creature creature) {
        // Each turn add the Disadvantage condition so casting spells, attacking, etc. is at disadvantage.
        creature.addCondition(new DisadvantagedCondition());
    }
}
