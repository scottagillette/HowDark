package com.redshift.ShadowDarkCalculator.actions.spells;

import com.redshift.ShadowDarkCalculator.actions.DamageType;
import com.redshift.ShadowDarkCalculator.conditions.DisadvantagedCondition;
import com.redshift.ShadowDarkCalculator.conditions.FearCondition;
import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.dice.RollModifier;
import com.redshift.ShadowDarkCalculator.dice.RollOutcome;
import com.redshift.ShadowDarkCalculator.encounter.Encounter;
import com.redshift.ShadowDarkCalculator.targets.UndeadTargetSelector;
import lombok.extern.slf4j.Slf4j;

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
public class TurnUndead extends MultiTargetSpell {

    public TurnUndead() {
        super("Turn Undead", 11, RollModifier.WISDOM, D4);
    }

    @Override
    public boolean canPerform(Creature actor, List<Creature> enemies, List<Creature> allies) {
        final List<Creature> undeadEnemies = new UndeadTargetSelector().getTargets(enemies, enemies.size());
        return (!lost && !undeadEnemies.isEmpty());
    }

    @Override
    public void perform(Creature actor, List<Creature> enemies, List<Creature> allies, Encounter encounter) {
        final List<Creature> targets = new UndeadTargetSelector().getTargets(enemies, enemies.size()); // Turn Undead can affect all near enemies.

        boolean disadvantage = actor.hasCondition(DisadvantagedCondition.class.getName());
        actor.removeCondition(DisadvantagedCondition.class.getName());

        // See if they pass the spell check!
        final int spellCheckRoll = getSpellCheckRoll(disadvantage);

        final boolean criticalSuccess = spellCheckRoll == RollOutcome.CRITICAL_SUCCESS;
        final boolean criticalFailure = spellCheckRoll == RollOutcome.CRITICAL_FAILURE;

        int spellCheckModifier = actor.getStats().getWisdomModifier(); // Always uses Wisdom modifier!

        if (criticalFailure) {
            lost = true; // Failed spell check!
            log.info("{} critically MISSES the spell check on {}", actor.getName(), getName());
        } else if (criticalSuccess || spellCheckRoll + spellCheckModifier + spellCheckBonus >= difficultyClass) {
            targets.forEach(target -> {

                int save = target.getStats().charismaSave();

                if (save < spellCheckRoll + spellCheckModifier + spellCheckBonus) {
                    if ((spellCheckRoll + spellCheckModifier) - save >= 10) {
                        if (actor.getLevel() >= target.getLevel()) {
                            log.info("{} hits a spell on {} with {} and is destroyed!", actor.getName(), target.getName(), getName());
                            target.takeDamage(999, new DamageType().addMagical()); // Destroyed!
                        } else {
                            target.addCondition(new FearCondition()); // Just feared
                            log.info("{} hits a spell on {} with {} and feared!", actor.getName(), target.getName(), getName());
                        }
                    } else {
                        target.addCondition(new FearCondition()); // Just feared
                        log.info("{} hits a spell on {} with {} and feared!", actor.getName(), target.getName(), getName());
                    }
                } else {
                    log.info("{} has their spell {} resisted by {}", actor.getName(), getName(), target.getName());
                }

            });
        } else {
            lost = true; // Failed spell check!
            log.info("{} MISSES the spell check with a {}", actor.getName(), getName());
        }
    }
}
