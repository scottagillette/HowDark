package com.redshift.ShadowDarkCalculator.actions.spells;

import com.redshift.ShadowDarkCalculator.actions.DamageType;
import com.redshift.ShadowDarkCalculator.conditions.FearCondition;
import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.dice.RollModifier;
import com.redshift.ShadowDarkCalculator.encounter.Encounter;
import com.redshift.ShadowDarkCalculator.targets.MultiTargetSelector;
import com.redshift.ShadowDarkCalculator.targets.multi.UndeadTargetSelector;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.*;

/**
 * Tier 1, priest
 * Duration: Instant
 * Range: Near
 *
 * You rebuke undead creatures, forcing them to flee. You must present a holy symbol to
 * cast this spell. Undead creatures within near of you must make a CHA check vs. your
 * spell casting check. If a creature fails by 10+ points and is equal to or less than
 * your level, it is destroyed. Otherwise, on a fail, it flees from you for 5 rounds.
 */

@Slf4j
public class TurnUndead extends MultipleTargetSpell {

    public TurnUndead() {
        super("Turn Undead", 11, RollModifier.WISDOM, D4);
    }

    @Override
    public boolean canPerform(Creature actor, List<Creature> enemies, List<Creature> allies) {
        final boolean canPerform = super.canPerform(actor, enemies, allies);
        final boolean hasTarget = !new TurnUndeadTargetSelector().getTargets(enemies, enemies.size()).isEmpty();
        return (canPerform && hasTarget);
    }

    @Override
    public List<Creature> getTargets(Creature actor, List<Creature> enemies, List<Creature> allies) {
        return new TurnUndeadTargetSelector().getTargets(enemies, enemies.size());
    }

    @Override
    public void performCriticalSpell(Creature actor, List<Creature> targets, Encounter encounter, int spellCheckRoll) {
        targets.forEach(target -> {
            int roll = target.getStats().charismaRoll();

            if (roll < spellCheckRoll + spellCheckBonus) {
                if (spellCheckRoll - roll >= 10) {
                    if (actor.getLevel() >= target.getLevel()) {
                        log.info("{} hits a spell on {} with {} and is destroyed!", actor.getName(), target.getName(), name);
                        target.takeDamage(target.getCurrentHitPoints(), new DamageType().addMagical()); // Destroyed!
                    } else {
                        target.addCondition(new FearCondition(10)); // Critical success 10 rounds!
                        log.info("{} hits a spell on {} with {} and feared!", actor.getName(), target.getName(), name);
                    }
                } else {
                    target.addCondition(new FearCondition()); // Just feared
                    log.info("{} hits a spell on {} with {} and feared!", actor.getName(), target.getName(), name);
                }
            } else {
                log.info("{} has their spell {} resisted by {}", actor.getName(), name, target.getName());
            }
        });
    }

    @Override
    public void performSpell(Creature actor, List<Creature> targets, Encounter encounter, int spellCheckRoll) {
        targets.forEach(target -> {
            int save = target.getStats().charismaRoll();

            if (save < spellCheckRoll) {
                if (spellCheckRoll - save >= 10) {
                    if (actor.getLevel() >= target.getLevel()) {
                        log.info("{} hits a spell on {} with {} and is destroyed!", actor.getName(), target.getName(), name);
                        target.takeDamage(target.getCurrentHitPoints(), new DamageType().addMagical()); // Destroyed!
                    } else {
                        target.addCondition(new FearCondition(5)); // Just feared for 5 rounds
                        log.info("{} hits a spell on {} with {} and feared!", actor.getName(), target.getName(), name);
                    }
                } else {
                    target.addCondition(new FearCondition()); // Just feared
                    log.info("{} hits a spell on {} with {} and feared!", actor.getName(), target.getName(), name);
                }
            } else {
                log.info("{} has their spell {} resisted by {}", actor.getName(), name, target.getName());
            }
        });
    }

    /**
     * Selects undead creatures that are not already feared.
     */

    private static class TurnUndeadTargetSelector implements MultiTargetSelector {

        @Override
        public List<Creature> getTargets(List<Creature> targetOptions, int maxTargets) {
            final List<Creature> possibleTargets = new UndeadTargetSelector().getTargets(targetOptions, maxTargets);

            final ArrayList<Creature> actualTargets = new ArrayList<>(possibleTargets.stream()
                    .filter(creature -> !creature.hasCondition(FearCondition.class.getName()))
                    .toList());

            if (actualTargets.isEmpty()) {
                return actualTargets; // Return empty list.
            } else {
                Collections.shuffle(actualTargets);
                return actualTargets.subList(0, Math.min(actualTargets.size(), maxTargets));
            }
        }
    }

}
