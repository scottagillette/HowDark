package com.redshift.ShadowDarkCalculator.actions.spells;

import com.redshift.ShadowDarkCalculator.conditions.DisadvantagedCondition;
import com.redshift.ShadowDarkCalculator.conditions.ShieldOfFaithCondition;
import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.dice.RollModifier;
import com.redshift.ShadowDarkCalculator.dice.RollOutcome;
import com.redshift.ShadowDarkCalculator.targets.RandomTargetSelector;
import com.redshift.ShadowDarkCalculator.targets.SingleTargetSelector;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.*;

@Slf4j
public class ShieldOfFaith extends SingleTargetSpell {

    public ShieldOfFaith() {
        super("Shield Of Faith", 11, RollModifier.WISDOM);
    }

    @Override
    public void perform(Creature actor, List<Creature> enemies, List<Creature> allies) {
        // Note you can always cast this on yourself if everyone else is unconscious!
        final SingleTargetSelector selector = new RandomTargetSelector();
        final Creature target = selector.get(allies); // Randomly choose an ally TODO: What about dead or unconscious?

        boolean disadvantage = actor.hasCondition(DisadvantagedCondition.class.getName());
        actor.removeCondition(DisadvantagedCondition.class.getName());

        // See if they pass the spell check!
        final int spellCheckRoll = getSpellCheckRoll(disadvantage);

        final boolean criticalSuccess = spellCheckRoll == RollOutcome.CRITICAL_SUCCESS;
        final boolean criticalFailure = spellCheckRoll == RollOutcome.CRITICAL_FAILURE;

        final int spellCheckModifier = actor.getStats().getWisdomModifier(); // Always uses Wisdom!

        if (criticalFailure) {
            log.info("{} critically MISSES the spell check on {}", actor.getName(), name);
        } else if (criticalSuccess) {
            target.addCondition(new ShieldOfFaithCondition(D5.roll(), 4)); // Double AC for critical success
            log.info("{} critically adds 4 AC on {} with a {}", actor.getName(), target.getName(), name);
        } else if (spellCheckRoll + spellCheckModifier >= difficultyClass) {
            target.addCondition(new ShieldOfFaithCondition(D5.roll()));
            log.info("{} adds 2 AC on {} with a {}", actor.getName(), target.getName(), name);
        } else {
            log.info("{} MISSES the spell check with a {}", actor.getName(), name);
        }

        lost = true; // Always lost... cast only one per battle per character...
    }
}
