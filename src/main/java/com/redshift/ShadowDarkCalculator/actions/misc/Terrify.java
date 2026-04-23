package com.redshift.ShadowDarkCalculator.actions.misc;

import com.redshift.ShadowDarkCalculator.actions.Action;
import com.redshift.ShadowDarkCalculator.actions.BaseAction;
import com.redshift.ShadowDarkCalculator.conditions.ParalyzedCondition;
import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.encounter.Encounter;
import com.redshift.ShadowDarkCalculator.targets.LivingTargetSelector;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D4;

/**
 * One target in near DC 15 CHA or paralyzed 1d4 rounds.
 */

@Slf4j
public class Terrify extends BaseAction implements Action {

    public Terrify() {
        super("Terrify");
    }

    @Override
    public boolean canPerform(Creature actor, List<Creature> enemies, List<Creature> allies) {
        // Not dead, unconscious or undead targets.
        final List<Creature> targets = new LivingTargetSelector().getTargets(enemies, enemies.size());
        return !targets.isEmpty();
    }

    @Override
    public boolean isMagicalWeapon() {
        return false;
    }

    @Override
    public void perform(Creature actor, List<Creature> enemies, List<Creature> allies, Encounter encounter) {
        // One target in near DC 15 CHA or paralyzed 1d4 rounds.

        final List<Creature> targets = new LivingTargetSelector().getTargets(enemies, enemies.size());

        final Creature target = targets.getFirst();
        if (target.getStats().charismaSave(15)) {
            log.info("{} resists the Terrify effect from {}.", target.getName(), actor.getName());
        } else {
            log.info("{} is terrified and is paralyzed by {}.", target.getName(), actor.getName());
            target.addCondition(new ParalyzedCondition(D4.roll()));
        }

    }

}
