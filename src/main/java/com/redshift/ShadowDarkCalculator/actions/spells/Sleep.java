package com.redshift.ShadowDarkCalculator.actions.spells;

import com.redshift.ShadowDarkCalculator.dice.RollModifier;
import com.redshift.ShadowDarkCalculator.conditions.SleepingCondition;
import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.encounter.Encounter;
import com.redshift.ShadowDarkCalculator.targets.multi.AliveAwakeNotUndeadTargetSelector;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D4;
import static java.lang.Math.min;

/**
 * Tier 1, wizard
 * Duration: Instant
 * Range: Near
 *
 * You weave a lulling spell that fills a near-sized cube extending from you. Living
 * creatures in the area of effect fall into a deep sleep if they are LV 2 or less.
 * Vigorous shaking or being injured wakes them.
 */

@Slf4j
public class Sleep extends MultipleTargetSpell {

    public Sleep() {
        // Total targets D4 affected... rules say near size cube from player.
        super("Sleep", 11, RollModifier.INTELLIGENCE, D4);
    }

    @Override
    public boolean canPerform(Creature actor, List<Creature> enemies, List<Creature> allies) {
        final boolean canPerform = super.canPerform(actor, enemies, allies);
        final boolean hasTarget = !new AliveAwakeNotUndeadTargetSelector().getTargets(enemies, enemies.size()).isEmpty();
        return (canPerform && hasTarget);
    }

    @Override
    public List<Creature> getTargets(Creature actor, List<Creature> enemies, List<Creature> allies) {
        final int numberOfTargets = min(enemies.size(), totalTargets.roll());
        return new AliveAwakeNotUndeadTargetSelector().getTargets(enemies, numberOfTargets);
    }

    @Override
    public void performCriticalSpell(Creature actor, List<Creature> targets, Encounter encounter, int spellCheckRoll) {
        targets.forEach(target -> {
            if (target.getLevel() <= 4) { // Critical Success Double Level Cap!
                target.addCondition(new SleepingCondition());
                log.info("{} critically hits a spell on {} with a {}", actor.getName(), target.getName(), name);
            } else {
                log.info("{} critically hits a spell on {} with a {} but doesn't affect the creature.", actor.getName(), target.getName(), name);
                lost = true; // Doesn't affect at least one creature... stop casting Sleep!
            }
        });
    }

    @Override
    public void performSpell(Creature actor, List<Creature> targets, Encounter encounter, int spellCheckRoll) {
        targets.forEach(target -> {
            if (target.getLevel() <= 2) {
                target.addCondition(new SleepingCondition());
                log.info("{} hits a spell on {} with a {}", actor.getName(), target.getName(), name);
            } else {
                log.info("{} hits a spell on {} with a {} but doesn't affect the creature.", actor.getName(), target.getName(), name);
                lost = true; // Doesn't affect at least one creature... stop casting Sleep!
            }
        });
    }

}
