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
    public boolean appliesToDeadCreatures() {
        return false;
    }

    @Override
    public boolean canAct() {
        return true; // Low intelligence doesn't affect actions... much!
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
            log.info("{} is no longer stupefied.", creature.getName());
            creature.getStats().setCurrentIntelligence(creature.getStats().getIntelligence());
        }
        return (rounds == 0);
    }

    @Override
    public void perform(Creature creature) {
        log.info("{} is still stupefied!", creature.getName());
        creature.getStats().setCurrentIntelligence(1);

        // Loose any spell focus while stupefied!
        final SpellFocusCondition spellFocusCondition = (SpellFocusCondition) creature.removeCondition(SpellFocusCondition.class.getName());
        if (spellFocusCondition != null) {
            spellFocusCondition.end();
        }
    }
}
