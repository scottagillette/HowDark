package com.redshift.ShadowDarkCalculator.conditions;

import com.redshift.ShadowDarkCalculator.creatures.Creature;
import lombok.extern.slf4j.Slf4j;

/**
 * A condition of having Mage Armor, either 14 AC or 18 AC; 10 rounds.
 */

@Slf4j
public class MageArmorCondition implements Condition {

    private int rounds;
    private final int armorClass;

    public MageArmorCondition(int rounds, int armorClass) {
        this.rounds = rounds + 1;
        this.armorClass = armorClass;
    }

    @Override
    public boolean appliesToDeadCreatures() {
        return false;
    }

    @Override
    public boolean canAct() {
        return true;
    }

    /**
     * Gets the AC of this spell.. 14 or 18.
     */

    public int getAC() {
        return armorClass;
    }

    @Override
    public boolean hasEnded(Creature creature) {
        rounds = Math.max(0, rounds - 1);
        if (rounds == 0) {
            log.info("{} no longer has Mage Armor.", creature.getName());
        }
        return (rounds == 0);
    }

    @Override
    public void perform(Creature creature) {
        // see creature.getAc()
    }
}
