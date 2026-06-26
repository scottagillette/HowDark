package com.redshift.ShadowDarkCalculator.actions.spells;

import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.dice.MultipleDice;
import com.redshift.ShadowDarkCalculator.dice.RollModifier;
import com.redshift.ShadowDarkCalculator.dice.SingleDie;
import com.redshift.ShadowDarkCalculator.targets.multi.AliveAwakeNotUndeadTargetSelector;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D4;

/**
 * Tier 1, necromancer
 * Duration: Instant
 * Range: Far
 *
 * You fling a dark rune of necrotic energy at a target in range. The target takes 1d4 damage.
 * This damage increases to 2d4 when you reach 5th level. Undead creatures are unharmed by
 * this spell.
 */

@Slf4j
public class Withermark extends SingleTargetDamageSpell {

    public Withermark() {
        super("Withermark", 11, RollModifier.CHARISMA, new MultipleDice(D4, D4), false);
    }

    @Override
    public boolean canPerform(Creature actor, List<Creature> enemies, List<Creature> allies) {
        final boolean canPerform = super.canPerform(actor, enemies, allies);
        final boolean hasTarget = !new AliveAwakeNotUndeadTargetSelector().getTargets(enemies, enemies.size()).isEmpty();
        return (canPerform && hasTarget);
    }

    @Override
    public List<Creature> getTargets(Creature actor, List<Creature> enemies, List<Creature> allies) {
        final List<Creature> targets = new AliveAwakeNotUndeadTargetSelector().getTargets(enemies, enemies.size());
        if (targets.isEmpty()) {
            return List.of();
        } else {
            final Creature target = actor.getSingleTargetSelector().get(targets);
            return List.of(target);
        }
    }

}
