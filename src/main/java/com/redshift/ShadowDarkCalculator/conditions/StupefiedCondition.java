package com.redshift.ShadowDarkCalculator.conditions;

import com.redshift.ShadowDarkCalculator.creatures.Creature;
import lombok.extern.slf4j.Slf4j;

/**
 * Reduces creatures INT to 1; see creature...
 */

@Slf4j
public class StupefiedCondition implements Condition {

    private int rounds;

    public StupefiedCondition(int rounds) {
        this.rounds = rounds + 1; // Since we check at the beginning of the creatures turn... add 1.
    }

    @Override
    public boolean canAct() {
        return true; // Low intelligence doesn't affect actions... much!
    }

    @Override
    public boolean hasEnded(Creature creature) {
        rounds = Math.max(0, rounds - 1);
        if (rounds == 0) {
            log.info("{} is no longer dazed and confused.", creature.getName());
            creature.getStats().setIntelligence(creature.getStats().getIntelligence());
        }
        return (rounds == 0);
    }

    @Override
    public void perform(Creature creature) {
        log.info("{} is still dazed and confused!", creature.getName());
        creature.getStats().setIntelligence(1);
        // No adverse effect... other than being dumb!
    }
}
