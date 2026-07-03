package com.redshift.ShadowDarkCalculator.conditions;

import com.redshift.ShadowDarkCalculator.creatures.Creature;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * A condition that indicates spells are harder to cast against the one with this condition.
 */

@Getter
@Slf4j
public class SpellResilience implements Condition {

    private final int difficultyClass;
    private int rounds;

    public SpellResilience(int difficultyClass) {
        this(difficultyClass, 999); // Unlimited
    }

    public SpellResilience(int difficultyClass, int rounds) {
        this.difficultyClass = difficultyClass;
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
    public boolean canMove() {
        return true;
    }

    @Override
    public void end() {

    }

    @Override
    public boolean hasEnded(Creature creature) {
        rounds = Math.max(0, rounds - 1);
        if (rounds == 0) {
            log.info("{} is no longer spell resistant.", creature.getName());
        }
        return rounds == 0;
    }

    @Override
    public void perform(Creature creature) {
        // See Spell for how this affects spell casting.
    }
}
