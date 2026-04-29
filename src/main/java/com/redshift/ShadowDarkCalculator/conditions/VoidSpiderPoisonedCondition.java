package com.redshift.ShadowDarkCalculator.conditions;

import com.redshift.ShadowDarkCalculator.actions.DamageType;
import com.redshift.ShadowDarkCalculator.creatures.Creature;
import lombok.extern.slf4j.Slf4j;

/**
 * Poisoned and falls to 0 hp in 1d4 rounds.
 */

@Slf4j
public class VoidSpiderPoisonedCondition implements Condition {

    private int rounds;
    private boolean hasEnded;

    public VoidSpiderPoisonedCondition(int rounds) {
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
    public void end() {

    }

    @Override
    public boolean hasEnded(Creature creature) {
        rounds = rounds - 1;
        return hasEnded;
    }

    @Override
    public void perform(Creature creature) {
        if (rounds == 0) {
            hasEnded = true;
            log.info("{} is overcome by poison as it takes full effect!", creature.getName());
            creature.takeDamage(creature.getCurrentHitPoints(), new DamageType().addPoison());
        }
    }
}
