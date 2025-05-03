package com.redshift.ShadowDarkCalculator.actions.spells;

import com.redshift.ShadowDarkCalculator.dice.RollModifier;
import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.dice.RollOutcome;
import com.redshift.ShadowDarkCalculator.targets.HealTargetSelector;
import com.redshift.ShadowDarkCalculator.targets.SingleTargetSelector;

import java.util.List;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D20;
import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D6;

public class CureWounds extends SingleTargetBenificialSpell {

    public CureWounds() {
        super("Cure Wounds", 11, RollModifier.WISDOM);
    }

    @Override
    public boolean canPerform(Creature actor, List<Creature> enemies, List<Creature> allies) {
        if (lost) return false;

        final SingleTargetSelector selector = new HealTargetSelector();
        return selector.getTarget(allies) != null;
    }

    @Override
    public void perform(Creature actor, List<Creature> enemies, List<Creature> allies) {
        final SingleTargetSelector selector = new HealTargetSelector();
        final Creature target = selector.getTarget(allies); // Shouldn't get null since Spell.canPerform() returned true

        // See if they pass the spell check!
        final int spellCheckRoll = getSpellCheckRoll();

        final boolean criticalSuccess = spellCheckRoll == RollOutcome.CRITICAL_SUCCESS;
        final boolean criticalFailure = spellCheckRoll == RollOutcome.CRITICAL_FAILURE;

        final int spellCheckModifier = actor.getStats().getWisdomModifier(); // Always uses Wisdom!

        if (criticalFailure) {
            lost = true; // Failed spell check!
            System.out.println(actor.getName() + " critically MISSES the spell check on " + name);
        } else if (criticalSuccess) {
            int hitPoints = D6.roll() + D6.roll();
            target.healDamage(hitPoints);
            System.out.println(actor.getName() + " critically heals on " + target.getName() + " for " + hitPoints + " with a " + name);
        } else if (spellCheckRoll + spellCheckModifier >= difficultyClass) {
            int hitPoints = D6.roll();
            target.healDamage(hitPoints);
            System.out.println(actor.getName() + " heals on " + target.getName() + " for " + hitPoints + " with a " + name);
        } else {
            lost = true; // Failed spell check!
            System.out.println(actor.getName() + " MISSES the spell check with a " + name);
        }

    }

}
