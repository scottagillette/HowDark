package com.redshift.ShadowDarkCalculator.conditions;

import com.redshift.ShadowDarkCalculator.creatures.Creature;
import lombok.extern.slf4j.Slf4j;

/**
 * Paralyzed and can't act!
 */

@Slf4j
public class ParalyzedCondition implements Condition {

    private int rounds;

    public ParalyzedCondition(int rounds) {
        this.rounds = rounds + 1; // Since we check at the beginning of the creatures turn... add 1.
    }

    @Override
    public boolean appliesToDeadCreatures() {
        return false;
    }

    @Override
    public boolean canAct() {
        return false;
    }

    @Override
    public void end() {
        // No specific behavior
        rounds = 0;
    }

    @Override
    public boolean hasEnded(Creature creature) {
        rounds = Math.max(0, rounds - 1);
        if (rounds == 0) {
            log.info("{} is no longer paralyzed and can act!", creature.getName());
        }
        return (rounds == 0);
    }

    @Override
    public void perform(Creature creature) {
        log.info("{} is paralyzed and skipping their turn.", creature.getName());

        // Loose any spell focus while paralyzed!
        final SpellFocusCondition spellFocusCondition = (SpellFocusCondition) creature.removeCondition(SpellFocusCondition.class.getName());
        if (spellFocusCondition != null) {
            spellFocusCondition.end();
        }
    }

}
