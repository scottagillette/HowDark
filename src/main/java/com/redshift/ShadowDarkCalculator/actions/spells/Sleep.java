package com.redshift.ShadowDarkCalculator.actions.spells;

import com.redshift.ShadowDarkCalculator.conditions.DisadvantagedCondition;
import com.redshift.ShadowDarkCalculator.dice.RollModifier;
import com.redshift.ShadowDarkCalculator.conditions.SleepingCondition;
import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.dice.RollOutcome;
import com.redshift.ShadowDarkCalculator.targets.LivingTargetSelector;
import com.redshift.ShadowDarkCalculator.targets.MultiTargetSelector;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D4;
import static java.lang.Math.min;

@Slf4j
public class Sleep extends MultiTargetSpell {

    public Sleep() {
        super("Sleep", 11, RollModifier.INTELLIGENCE, D4);
    }

    @Override
    public boolean canPerform(Creature actor, List<Creature> enemies, List<Creature> allies) {
        if (lost) return false;

        final MultiTargetSelector selector = new LivingTargetSelector();
        return !selector.getTargets(enemies, enemies.size()).isEmpty();
    }

    @Override
    public void perform(Creature actor, List<Creature> enemies, List<Creature> allies) {
        final MultiTargetSelector selector = new LivingTargetSelector();
        final List<Creature> livingCreatures = selector.getTargets(enemies, enemies.size());

        // Number of targets...
        final int numberOfTargets = min(livingCreatures.size(), totalTargets.roll());

        // Get list of targets...
        List<Creature> targets = new ArrayList<>(livingCreatures);
        Collections.shuffle(targets);
        targets = targets.subList(0, numberOfTargets);

        boolean disadvantage = actor.hasCondition(DisadvantagedCondition.class.getName());
        actor.removeCondition(DisadvantagedCondition.class.getName());

        // See if they pass the spell check!
        final int spellCheckRoll = getSpellCheckRoll(disadvantage);

        final boolean criticalSuccess = spellCheckRoll == RollOutcome.CRITICAL_SUCCESS;
        final boolean criticalFailure = spellCheckRoll == RollOutcome.CRITICAL_FAILURE;

        int spellCheckModifier = actor.getStats().getIntelligenceModifier();

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
                }
            });
        } else if (spellCheckRoll + spellCheckModifier >= difficultyClass) {
            targets.forEach(target -> {
                if (target.getLevel() <= 2) {
                    target.addCondition(new SleepingCondition());
                    log.info("{} hits a spell on {} with a {}", actor.getName(), target.getName(), getName());
                } else {
                    log.info("{} hits a spell on {} with a {} but doesn't affect the creature.", actor.getName(), target.getName(), getName());
                }
            });
        } else {
            lost = true; // Failed spell check!
            log.info("{} MISSES the spell check with a {}", actor.getName(), getName());
        }
    }
}
