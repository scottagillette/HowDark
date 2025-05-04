package com.redshift.ShadowDarkCalculator.conditions;

import com.redshift.ShadowDarkCalculator.creatures.Creature;

public class DyingCondition implements Condition {

    private int rounds;

    public DyingCondition(int rounds) {
        this.rounds = rounds + 1; // Since we check at the beginning of the creatures turn... add 1.
    }

    @Override
    public boolean canAct() {
        return false;
    }

    @Override
    public boolean hasEnded(Creature creature) {
        rounds = Math.max(0, rounds - 1);
        return false; // Only healing stops dying.
    }

    @Override
    public void perform(Creature creature) {
        if (rounds == 0) {
            System.out.println(creature.getName() + " has died!");
            creature.setDead(true);
        } else {
            System.out.println(creature.getName() + " has their death timer tick down: roundsRemaining=" + rounds);
        }
    }
}
