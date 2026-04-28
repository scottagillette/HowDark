package com.redshift.ShadowDarkCalculator.conditions;

import com.redshift.ShadowDarkCalculator.creatures.Creature;
import lombok.extern.slf4j.Slf4j;

/**
 * Can take action other than run away.
 */

@Slf4j
public class FearCondition implements Condition {

    private int rounds;

    public FearCondition() {
        this.rounds = 5 + 1; // Since we check at the beginning of the creatures turn... add 1.
    }

    public FearCondition(int rounds) {
        this.rounds = rounds + 1; // Since we check at the beginning of the creatures turn... add 1.
    }

    @Override
    public boolean appliesToDeadCreatures() {
        return false;
    }

    @Override
    public boolean canAct() {
        return false; // Can't act while you are running!
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
            log.info("{} is no longer feared and can act!", creature.getName());
        }
        return (rounds == 0);
    }

    @Override
    public void perform(Creature creature) {
        log.info("{} is feared and skipping their turn.", creature.getName());

        // Loose any spell focus while running away in terror!
        final SpellFocusCondition spellFocusCondition = (SpellFocusCondition) creature.removeCondition(SpellFocusCondition.class.getName());
        if (spellFocusCondition != null) {
            spellFocusCondition.end();
        }
    }

}
