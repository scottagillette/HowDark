package com.redshift.ShadowDarkCalculator.conditions;

import com.redshift.ShadowDarkCalculator.creatures.Creature;
import lombok.extern.slf4j.Slf4j;

/**
 * Stuck to something and can't act! Attempt to break free each turn.
 */

@Slf4j
public class StuckToCondition implements Condition {

    private final int strDC;

    public StuckToCondition(int strDC) {
        this.strDC = strDC;
    }

    @Override
    public boolean appliesToDeadCreatures() {
        return false;
    }

    @Override
    public boolean canAct() {
        return false; // Attempt to get unstuck only? If successful then they can act!
    }

    @Override
    public void end() {
        // No specific behavior
    }

    @Override
    public boolean hasEnded(Creature creature) {
        // See if they break free.
        final boolean breaksFree = !creature.isUnconscious() && creature.getStats().strengthSave(strDC);
        if (breaksFree) {
            log.info("{} has broken free from being stuck!", creature.getName());
        }
        return breaksFree;
    }

    @Override
    public void perform(Creature creature) {
        // Nothing but still stuck!
    }
}
