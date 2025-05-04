package com.redshift.ShadowDarkCalculator.conditions;

import com.redshift.ShadowDarkCalculator.creatures.Creature;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FearCondition implements Condition {

    private int rounds;

    public FearCondition(int rounds) {
        this.rounds = rounds + 1; // Since we check at the beginning of the creatures turn... add 1.
    }

    @Override
    public boolean canAct() {
        return false; // Can't act while you are running!
    }

    @Override
    public boolean hasEnded(Creature creature) {
        rounds = Math.max(0, rounds - 1);
        return (rounds == 0);
    }

    @Override
    public void perform(Creature creature) {
        log.info(creature.getName() + " is feared and skipping their turn.");
        // No ill effects... other than you are just running away!
    }

}
