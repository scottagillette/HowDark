package com.redshift.ShadowDarkCalculator.conditions;

import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.dice.Dice;

/**
 * The targets next attack will deal extra damage.
 */

public class ExtraDamageDiceCondition implements Condition {

    private final int count;

    public ExtraDamageDiceCondition(int count) {
        this.count = count;
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
        // No specific behavior
    }

    public int getExtraDamage(Dice dice) {
        int damage = 0;
        for (int i = 0; i < count; i++) {
            damage = damage + dice.roll();
        }
        return damage;
    }

    @Override
    public boolean hasEnded(Creature creature) {
        return false;
    }

    @Override
    public void perform(Creature creature) {
        // does nothing... removed once a weapon attack is rolled.
    }
}
