package com.redshift.ShadowDarkCalculator.actions.spells;

import com.redshift.ShadowDarkCalculator.conditions.DisadvantagedCondition;
import com.redshift.ShadowDarkCalculator.dice.RollModifier;
import com.redshift.ShadowDarkCalculator.conditions.SleepingCondition;
import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.dice.RollOutcome;
import com.redshift.ShadowDarkCalculator.encounter.Encounter;
import com.redshift.ShadowDarkCalculator.targets.AliveAwakeNotUndeadTargetSelector;
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
        return (!lost && !new AliveAwakeNotUndeadTargetSelector().getTargets(enemies, enemies.size()).isEmpty());
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

        boolean disadvantage = actor.hasCondition(DisadvantagedCondition.class.getName());
        actor.removeCondition(DisadvantagedCondition.class.getName());

        // See if they pass the spell check!
        final int spellCheckRoll = getSpellCheckRoll(disadvantage);

        final boolean criticalSuccess = spellCheckRoll == RollOutcome.CRITICAL_SUCCESS;
        final boolean criticalFailure = spellCheckRoll == RollOutcome.CRITICAL_FAILURE;

        int spellCheckModifier = actor.getStats().getIntelligenceModifier(); // Always uses INT modifier!

        if (criticalFailure) {
            lost = true; // Failed spell check!
            log.info("{} critically MISSES the spell check on {}", actor.getName(), getName());
        } else if (criticalSuccess) {
            targets.forEach(target -> {
                if (target.getLevel() <= 2) {
                    target.addCondition(new SleepingCondition());
                    log.info("{} critically hits a spell on {} with a {}", actor.getName(), target.getName(), getName());
                } else {
                    log.info("{} critically hits a spell on {} with a {} but doesn't affect the creature.", actor.getName(), target.getName(), getName());
                    lost = true; // Doesn't affect at least one creature... stop casting Sleep!
                }
            });
        } else if (spellCheckRoll + spellCheckModifier + spellCheckBonus >= difficultyClass) {
            targets.forEach(target -> {
                if (target.getLevel() <= 2) {
                    target.addCondition(new SleepingCondition());
                    log.info("{} hits a spell on {} with a {}", actor.getName(), target.getName(), getName());
                } else {
                    log.info("{} hits a spell on {} with a {} but doesn't affect the creature.", actor.getName(), target.getName(), getName());
                    lost = true; // Doesn't affect at least one creature... stop casting Sleep!
                }
            });
        } else {
            lost = true; // Failed spell check!
            log.info("{} MISSES the spell check with a {}", actor.getName(), getName());
        }
    }
}
