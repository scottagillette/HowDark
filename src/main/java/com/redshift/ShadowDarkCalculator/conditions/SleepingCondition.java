package com.redshift.ShadowDarkCalculator.conditions;

import com.redshift.ShadowDarkCalculator.creatures.Creature;
import lombok.extern.slf4j.Slf4j;

/**
 * Asleep and can't act!
 */

@Slf4j
public class SleepingCondition implements Condition {

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
    }

    @Override
    public boolean hasEnded(Creature creature) {
        return false; // Only ends on damage...
    }

    @Override
    public void perform(Creature creature) {
        log.info("{} is sleeping and skipping their turn.", creature.getName());

        // Loose any spell focus while sleeping!
        final SpellFocusCondition spellFocusCondition = (SpellFocusCondition) creature.removeCondition(SpellFocusCondition.class.getName());
        if (spellFocusCondition != null) {
            spellFocusCondition.end();
        }
    }

}
