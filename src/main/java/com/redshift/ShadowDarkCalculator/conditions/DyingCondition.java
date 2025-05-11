package com.redshift.ShadowDarkCalculator.conditions;

import com.redshift.ShadowDarkCalculator.creatures.Creature;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DyingCondition implements Condition {

    private int rounds;

    public DyingCondition(int rounds) {
        this.rounds = rounds + 1; // Since we check at the beginning of the creatures turn... add 1.
    }

    @Override
    public boolean canAct() {
        return false;
    }

    @Override
    public boolean hasEnded(Creature creature) {
        rounds = Math.max(0, rounds - 1);
        return false; // Only healing stops dying.
    }

    @Override
    public void perform(Creature creature) {
        if (rounds == 0) {
            log.info("{} has died!", creature.getName());
            creature.setDead(true);
        } else {
            log.info("{} has their death timer tick down: roundsRemaining={}", creature.getName(), rounds);
        }
    }
}
