package com.redshift.ShadowDarkCalculator.actions.spells;

import com.redshift.ShadowDarkCalculator.conditions.ShieldOfFaithCondition;
import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.dice.RollModifier;
import com.redshift.ShadowDarkCalculator.dice.RollOutcome;
import com.redshift.ShadowDarkCalculator.targets.RandomTargetSelector;
import com.redshift.ShadowDarkCalculator.targets.SingleTargetSelector;

import java.util.List;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.*;

public class ShieldOfFaith extends SingleTargetBenificialSpell {

    public ShieldOfFaith() {
        super("Shield Of Faith", 11, RollModifier.WISDOM);
    }

    @Override
    public void perform(Creature actor, List<Creature> enemies, List<Creature> allies) {
        // Note you can always cast this on yourself if everyone else is unconscious!
        final SingleTargetSelector selector = new RandomTargetSelector();
        final Creature target = selector.get(allies); // Randomly choose an ally TODO: What about dead or unconscious?

        // See if they pass the spell check!
        final int spellCheckRoll = getSpellCheckRoll();

        final boolean criticalSuccess = spellCheckRoll == RollOutcome.CRITICAL_SUCCESS;
        final boolean criticalFailure = spellCheckRoll == RollOutcome.CRITICAL_FAILURE;

        final int spellCheckModifier = actor.getStats().getWisdomModifier(); // Always uses Wisdom!

        if (criticalFailure) {
            System.out.println(actor.getName() + " critically MISSES the spell check on " + name);
        } else if (criticalSuccess) {
            target.addCondition(new ShieldOfFaithCondition(D5.roll(), 4)); // Double AC for critical success
            System.out.println(actor.getName() + " critically adds 4 AC on " + target.getName() + " with a " + name);
        } else if (spellCheckRoll + spellCheckModifier >= difficultyClass) {
            target.addCondition(new ShieldOfFaithCondition(D5.roll()));
            System.out.println(actor.getName() + " adds 2 AC on " + target.getName() + " with a " + name);
        } else {
            System.out.println(actor.getName() + " MISSES the spell check with a " + name);
        }

        lost = true; // Always lost... cast only one per battle per character...
    }
}
