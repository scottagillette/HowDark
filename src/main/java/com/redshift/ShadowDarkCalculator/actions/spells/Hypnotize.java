package com.redshift.ShadowDarkCalculator.actions.spells;

import com.redshift.ShadowDarkCalculator.conditions.StupefiedCondition;
import com.redshift.ShadowDarkCalculator.conditions.SpellFocusCondition;
import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.dice.RollModifier;
import com.redshift.ShadowDarkCalculator.dice.RollOutcome;
import com.redshift.ShadowDarkCalculator.encounter.Encounter;
import com.redshift.ShadowDarkCalculator.targets.SingleTargetSelector;
import com.redshift.ShadowDarkCalculator.targets.multi.AliveAwakeTargetSelector;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;

/**
 * Duration: Focus
 * Range: Near
 *
 * One creature of LV 3 or less that can see you is rendered stupefied. Breaking the creature's
 * line of sight to you allows it to make a DC 15 Charisma check. On a success, the spell ends.
 */

@Slf4j
public class Hypnotize extends Spell {

    public Hypnotize() {
        super("Hypnotize", 11, RollModifier.CHARISMA);
    }

    @Override
    public boolean canPerform(Creature actor, List<Creature> enemies, List<Creature> allies) {
        final boolean canPerform = super.canPerform(actor, enemies, allies);
        final boolean hasTarget = new HypnotizeTargetSelector().get(enemies) != null;
        final boolean hasFocus = actor.hasCondition(SpellFocusCondition.class.getName());

        return (canPerform && hasTarget && !hasFocus);
    }

    @Override
    public void perform(Creature actor, List<Creature> enemies, List<Creature> allies, Encounter encounter) {
        final Creature target = new HypnotizeTargetSelector().get(enemies);

        final int spellCheckModifier = actor.getStats().getCharismaModifier(); // Always uses Charisma!

        // See if they pass the spell check!
        final int d20Roll = getSpellCheckRoll(actor, List.of(target), spellCheckModifier);

        final boolean criticalSuccess = d20Roll == RollOutcome.CRITICAL_SUCCESS;
        final boolean criticalFailure = d20Roll == RollOutcome.CRITICAL_FAILURE;

        if (criticalFailure) {
            lost = true; // Failed spell check!
            log.info("{} critically MISSES the spell check on {}", actor.getName(), name);
        } else if (criticalSuccess) {
            if (target.getLevel() <= 3) {
                log.info("{} critically succeeds on the spell {} and {} is hypnotized!", actor.getName(), name, target.getName());
                target.addCondition(new StupefiedCondition());
                actor.addCondition(new SpellFocusCondition(
                        11,
                        RollModifier.CHARISMA,
                        spellCheckAdvantage,
                        spellCheckBonus,
                        new RemoveStupefiedCondition(target)
                ));
            } else {
                log.info("{} casts {} but {} is not affected", actor.getName(), name, target.getName());
            }
        } else if (d20Roll + spellCheckModifier + spellCheckBonus >= difficultyClass) {
            if (target.getLevel() <= 3) {
                log.info("{} succeeds on the spell {} and {} is hypnotized!", actor.getName(), name, target.getName());
                actor.addCondition(new SpellFocusCondition(
                        11,
                        RollModifier.CHARISMA,
                        spellCheckAdvantage,
                        spellCheckBonus,
                        new RemoveStupefiedCondition(target)
                ));
                target.addCondition(new StupefiedCondition());
            } else {
                log.info("{} casts {} but {} is not affected", actor.getName(), name, target.getName());
            }
        } else {
            lost = true; // Failed spell check!
            log.info("{} MISSES the spell check with a {}", actor.getName(), name);
        }
    }

    private static class HypnotizeTargetSelector implements SingleTargetSelector {

        @Override
        public Creature get(List<Creature> targetOptions) {
            final List<Creature> aliveTargets = new AliveAwakeTargetSelector().getTargets(targetOptions, targetOptions.size());

            if (aliveTargets.isEmpty()) {
                return null; // No Targets.
            } else {
                final List<Creature> actualTargets = new java.util.ArrayList<>(aliveTargets.stream()
                        .filter(creature -> !creature.hasCondition(StupefiedCondition.class.getName()))
                        .toList());

                if (actualTargets.isEmpty()) {
                    return null; // No Targets!
                } else {
                    Collections.shuffle(actualTargets);
                    return actualTargets.getFirst();
                }
            }
        }

    }

    /**
     * Runnable to remove the Dazed and Confused condition on spell focus loss.
     */

    private static class RemoveStupefiedCondition implements Runnable {

        private final Creature creature;

        private RemoveStupefiedCondition(Creature creature) {
            this.creature = creature;
        }

        @Override
        public void run() {
            if (creature.hasCondition(StupefiedCondition.class.getName())) {
                log.info("{} is no longer stupefied!", creature.getName());
                creature.removeCondition(StupefiedCondition.class.getName());
            }
        }
    }
}
