package com.redshift.ShadowDarkCalculator.conditions;

import com.redshift.ShadowDarkCalculator.creatures.Creature;
import lombok.extern.slf4j.Slf4j;

/**
 * Dazed and confused, cant act, removed when damaged.
 */

@Slf4j
public class DazedAndConfusedCondition implements Condition {

    @Override
    public boolean appliesToDeadCreatures() {
        return false;
    }

    @Override
    public boolean canAct() {
        return false;
    }

    @Override
    public boolean hasEnded(Creature creature) {
        return false; // Only ends on damage... or spell focus lost by caster, etc.
    }

    @Override
    public void perform(Creature creature) {
        log.info("{} is dazed and confused and skipping their turn.", creature.getName());

        // Loose any spell focus while dazed and confused!
        final SpellFocusCondition spellFocusCondition = (SpellFocusCondition) creature.removeCondition(SpellFocusCondition.class.getName());
        if (spellFocusCondition != null) {
            spellFocusCondition.end();
        }
    }

}
