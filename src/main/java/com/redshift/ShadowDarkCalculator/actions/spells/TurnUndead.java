package com.redshift.ShadowDarkCalculator.actions.spells;

import com.redshift.ShadowDarkCalculator.conditions.DisadvantagedCondition;
import com.redshift.ShadowDarkCalculator.conditions.FearCondition;
import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.dice.RollModifier;
import com.redshift.ShadowDarkCalculator.dice.RollOutcome;
import com.redshift.ShadowDarkCalculator.targets.MultiTargetSelector;
import com.redshift.ShadowDarkCalculator.targets.UndeadTargetSelector;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.*;

@Slf4j
public class TurnUndead extends MultiTargetSpell {

    public TurnUndead() {
        super("Turn Undead", 11, RollModifier.WISDOM, D4);
    }

    @Override
    public boolean canPerform(Creature actor, List<Creature> enemies, List<Creature> allies) {
        final MultiTargetSelector selector = new UndeadTargetSelector();
        return (!lost && !selector.getTargets(enemies, enemies.size()).isEmpty());
    }

    @Override
    public void perform(Creature actor, List<Creature> enemies, List<Creature> allies) {
        final MultiTargetSelector selector = new UndeadTargetSelector();
        final List<Creature> targets = selector.getTargets(enemies, enemies.size()); // Turn Undead can affect all near enemies.

        boolean disadvantage = actor.hasCondition(DisadvantagedCondition.class.getName());
        actor.removeCondition(DisadvantagedCondition.class.getName());

        // See if they pass the spell check!
        final int spellCheckRoll = getSpellCheckRoll(disadvantage);

        final boolean criticalSuccess = spellCheckRoll == RollOutcome.CRITICAL_SUCCESS;
        final boolean criticalFailure = spellCheckRoll == RollOutcome.CRITICAL_FAILURE;

        int spellCheckModifier = actor.getStats().getWisdomModifier();

        if (criticalFailure) {
            lost = true; // Failed spell check!
            log.info("{} critically MISSES the spell check on {}", actor.getName(), getName());
        } else if (criticalSuccess || spellCheckRoll + spellCheckModifier >= difficultyClass) {
            targets.forEach(target -> {
                int save = target.getStats().charismaSave();

                if (save < spellCheckRoll + spellCheckModifier) {
                    if ((spellCheckRoll + spellCheckModifier) - save >= 10) {
                        if (actor.getLevel() >= target.getLevel()) {
                            log.info("{} hits a spell on {} with a {} and is destroyed!", actor.getName(), target.getName(), getName());
                            target.takeDamage(999, false, true, false, false); // Destroyed!
                        } else {
                            target.addCondition(new FearCondition(D5.roll())); // Just feared
                            log.info("{} hits a spell on {} with a {} and feared!", actor.getName(), target.getName(), getName());
                        }
                    } else {
                        target.addCondition(new FearCondition(D5.roll())); // Just feared
                        log.info("{} hits a spell on {} with a {} and feared!", actor.getName(), target.getName(), getName());
                    }
                } else {
                    log.info("{} has their spell resisted by {} with a {}", actor.getName(), target.getName(), getName());
                }
            });
        } else {
            lost = true; // Failed spell check!
            log.info("{} MISSES the spell check with a {}", actor.getName(), getName());
        }
    }
}
