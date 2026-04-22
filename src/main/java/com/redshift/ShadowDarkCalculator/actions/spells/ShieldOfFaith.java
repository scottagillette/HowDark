package com.redshift.ShadowDarkCalculator.actions.spells;

import com.redshift.ShadowDarkCalculator.conditions.DisadvantagedCondition;
import com.redshift.ShadowDarkCalculator.conditions.ShieldOfFaithCondition;
import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.dice.RollModifier;
import com.redshift.ShadowDarkCalculator.dice.RollOutcome;
import com.redshift.ShadowDarkCalculator.encounter.CombatSimulator;
import com.redshift.ShadowDarkCalculator.targets.RandomTargetSelector;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * A protective force wrought of your holy conviction surrounds you. You gain a +2 bonus to your armor class for
 * the duration (5 rounds).
 */

@Slf4j
public class ShieldOfFaith extends Spell {

    public ShieldOfFaith() {
        super("Shield Of Faith", 11, RollModifier.WISDOM);
    }

    @Override
    public void perform(Creature actor, List<Creature> enemies, List<Creature> allies, CombatSimulator simulator) {
        // Note you can always cast this on yourself if everyone else is unconscious!
        final Creature target = new RandomTargetSelector().get(allies); // Randomly choose an ally TODO: What about dead or unconscious?

        boolean disadvantage = actor.hasCondition(DisadvantagedCondition.class.getName());
        actor.removeCondition(DisadvantagedCondition.class.getName());

        // See if they pass the spell check!
        final int spellCheckRoll = getSpellCheckRoll(disadvantage);

        final boolean criticalSuccess = spellCheckRoll == RollOutcome.CRITICAL_SUCCESS;
        final boolean criticalFailure = spellCheckRoll == RollOutcome.CRITICAL_FAILURE;

        final int spellCheckModifier = actor.getStats().getWisdomModifier(); // Always uses Wisdom modifier!

        if (criticalFailure) {
            log.info("{} critically MISSES the spell check on {}", actor.getName(), getName());
        } else if (criticalSuccess) {
            target.addCondition(new ShieldOfFaithCondition(4)); // Double AC for critical success
            log.info("{} critically adds 4 AC on {} with a {}", actor.getName(), target.getName(), getName());
        } else if (spellCheckRoll + spellCheckModifier + spellCheckBonus >= difficultyClass) {
            target.addCondition(new ShieldOfFaithCondition());
            log.info("{} adds 2 AC on {} with a {}", actor.getName(), target.getName(), getName());
        } else {
            log.info("{} MISSES the spell check with a {}", actor.getName(), getName());
        }

        lost = true; // Always lost... cast only one per battle per character...
    }
}
