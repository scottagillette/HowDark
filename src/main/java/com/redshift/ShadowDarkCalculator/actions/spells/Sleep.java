package com.redshift.ShadowDarkCalculator.actions.spells;

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

        // See if they pass the spell check!
        final int spellCheckRoll = getSpellCheckRoll();

        final boolean criticalSuccess = spellCheckRoll == RollOutcome.CRITICAL_SUCCESS;
        final boolean criticalFailure = spellCheckRoll == RollOutcome.CRITICAL_FAILURE;

        int spellCheckModifier = actor.getStats().getIntelligenceModifier();

        if (criticalFailure) {
            lost = true; // Failed spell check!
            log.info(actor.getName() + " critically MISSES the spell check on " + getName());
        } else if (criticalSuccess) {
            targets.forEach(target -> {
                if (target.getLevel() <= 2) {
                    target.addCondition(new SleepingCondition());
                    log.info(actor.getName() + " critically hits a spell on " + target.getName() + " with a " + getName());
                } else {
                    log.info(actor.getName() + " critically hits a spell on " + target.getName() + " with a " + getName() + " but doesn't affect the creature.");
                }
            });
        } else if (spellCheckRoll + spellCheckModifier >= difficultyClass) {
            targets.forEach(target -> {
                if (target.getLevel() <= 2) {
                    target.addCondition(new SleepingCondition());
                    log.info(actor.getName() + " hits a spell on " + target.getName() + " with a " + getName());
                } else {
                    log.info(actor.getName() + " hits a spell on " + target.getName() + " with a " + getName() + " but doesn't affect the creature.");
                }
            });
        } else {
            lost = true; // Failed spell check!
            log.info(actor.getName() + " MISSES the spell check with a " + getName());
        }
    }
}
