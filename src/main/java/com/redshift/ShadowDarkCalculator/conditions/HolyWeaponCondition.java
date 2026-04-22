package com.redshift.ShadowDarkCalculator.conditions;

import com.redshift.ShadowDarkCalculator.actions.spells.HolyWeapon;
import com.redshift.ShadowDarkCalculator.creatures.Creature;
import lombok.extern.slf4j.Slf4j;

/**
 * A condition that indicates you have a holy weapon spell cast for 5 rounds.
 */

@Slf4j
public class HolyWeaponCondition implements Condition {

    private int rounds;

    public HolyWeaponCondition() {
        this.rounds = 5 + 1; // Since we check at the beginning of the creatures turn... add 1.
    }

    public HolyWeaponCondition(int rounds) {
        this.rounds = rounds + 1; // Since we check at the beginning of the creatures turn... add 1.
    }

    @Override
    public boolean appliesToDeadCreatures() {
        return true; // Hmm
    }

    @Override
    public boolean canAct() {
        return true; // Holy weapon doesn't prevent action!
    }

    @Override
    public boolean hasEnded(Creature creature) {
        rounds = Math.max(0, rounds - 1);
        if (rounds == 0) {
            log.info("{} no longer has a holy weapon as the magic fades.", creature.getName());
        }
        return (rounds == 0);
    }

    @Override
    public void perform(Creature creature) {
        // Does nothing... see Weapon class for effect.
    }
}
