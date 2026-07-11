package com.redshift.ShadowDarkCalculator.conditions;

import com.redshift.ShadowDarkCalculator.creatures.Creature;
import lombok.extern.slf4j.Slf4j;

import static com.redshift.ShadowDarkCalculator.dice.DifficultyClass.HARD;

/**
 * Grappled and can't move!
 */

@Slf4j
public class GrappledCondition implements Condition {

    @Override
    public boolean appliesToDeadCreatures() {
        return false;
    }

    @Override
    public boolean canAct() {
        return true;
    }

    @Override
    public boolean canMove() {
        return false;
    }

    @Override
    public void end() {
        // No specific behavior
    }

    @Override
    public boolean hasEnded(Creature creature) {
        if (creature.getStats().strengthSave(HARD.getDc())) {
            log.info("{} frees itself from being grappled!", creature.getName());
            return true;
        }
        return false;
    }

    @Override
    public void perform(Creature creature) {
        log.info("{} is still grappled and cannot move.", creature.getName());
    }

}
