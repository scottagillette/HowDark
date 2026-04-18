package com.redshift.ShadowDarkCalculator.conditions;

import com.redshift.ShadowDarkCalculator.creatures.Creature;
import lombok.Getter;

/**
 * +2 AC for 5 rounds; +4 AC if critical success.
 */

@Getter
public class ShieldOfFaithCondition implements Condition {

    private int rounds;
    private final int acBonus;

    public ShieldOfFaithCondition() {
        this(2);
    }

    public ShieldOfFaithCondition(int acBonus) {
        this.rounds = 5 + 1; // Since we check at the beginning of the creatures turn... add 1.
        this.acBonus = acBonus;
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
    public boolean hasEnded(Creature creature) {
        rounds = Math.max(0, rounds - 1);
        return (rounds == 0);
    }

    @Override
    public void perform(Creature creature) {
        // does nothing.. see Creature.getAC() for implementation of effect.
    }
}
