package com.redshift.ShadowDarkCalculator.actions.spells;

import com.redshift.ShadowDarkCalculator.dice.RollModifier;
import com.redshift.ShadowDarkCalculator.conditions.SleepingCondition;
import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.dice.RollOutcome;
import com.redshift.ShadowDarkCalculator.encounter.Encounter;
import com.redshift.ShadowDarkCalculator.targets.multi.AliveAwakeNotUndeadTargetSelector;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
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
public class Sleep extends MultiTargetSpell {

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
    public void perform(Creature actor, List<Creature> enemies, List<Creature> allies, Encounter encounter) {
        final List<Creature> potentialTargets = new AliveAwakeNotUndeadTargetSelector().getTargets(enemies, enemies.size());

        // Number of targets...
        final int numberOfTargets = min(potentialTargets.size(), totalTargets.roll());

        // Get list of targets...
        List<Creature> targets = new ArrayList<>(potentialTargets);
        Collections.shuffle(targets);
        targets = targets.subList(0, numberOfTargets);

        int spellCheckModifier = actor.getStats().getIntelligenceModifier(); // Always uses INT modifier!

        // See if they pass the spell check!
        final int d20Roll = getSpellCheckRoll(actor, targets, spellCheckModifier);

        final boolean criticalSuccess = d20Roll == RollOutcome.CRITICAL_SUCCESS;
        final boolean criticalFailure = d20Roll == RollOutcome.CRITICAL_FAILURE;

        if (criticalFailure) {
            lost = true; // Failed spell check!
            log.info("{} critically MISSES the spell check on {}", actor.getName(), name);
        } else if (criticalSuccess) {
            targets.forEach(target -> {
                if (target.getLevel() <= 2) {
                    target.addCondition(new SleepingCondition());
                    log.info("{} critically hits a spell on {} with a {}", actor.getName(), target.getName(), name);
                } else {
                    log.info("{} critically hits a spell on {} with a {} but doesn't affect the creature.", actor.getName(), target.getName(), name);
                    lost = true; // Doesn't affect at least one creature... stop casting Sleep!
                }
            });
        } else if (d20Roll + spellCheckModifier + spellCheckBonus >= difficultyClass) {
            targets.forEach(target -> {
                if (target.getLevel() <= 2) {
                    target.addCondition(new SleepingCondition());
                    log.info("{} hits a spell on {} with a {}", actor.getName(), target.getName(), name);
                } else {
                    log.info("{} hits a spell on {} with a {} but doesn't affect the creature.", actor.getName(), target.getName(), name);
                    lost = true; // Doesn't affect at least one creature... stop casting Sleep!
                }
            });
        } else {
            lost = true; // Failed spell check!
            log.info("{} MISSES the spell check with a {}", actor.getName(), name);
        }
    }
}
