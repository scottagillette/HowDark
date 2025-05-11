package com.redshift.ShadowDarkCalculator.actions.spells;

import com.redshift.ShadowDarkCalculator.conditions.DisadvantagedCondition;
import com.redshift.ShadowDarkCalculator.dice.RollModifier;
import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.dice.RollOutcome;
import com.redshift.ShadowDarkCalculator.targets.HealTargetSelector;
import com.redshift.ShadowDarkCalculator.targets.SingleTargetSelector;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D6;

@Slf4j
public class CureWounds extends SingleTargetBenificialSpell {

    public CureWounds() {
        super("Cure Wounds", 11, RollModifier.WISDOM);
    }

    @Override
    public boolean canPerform(Creature actor, List<Creature> enemies, List<Creature> allies) {
        if (lost) return false;

        final SingleTargetSelector selector = new HealTargetSelector();
        return selector.get(allies) != null;
    }

    @Override
    public void perform(Creature actor, List<Creature> enemies, List<Creature> allies) {
        final SingleTargetSelector selector = new HealTargetSelector();
        final Creature target = selector.get(allies); // Shouldn't get null since Spell.canPerform() returned true

        boolean disadvantage = actor.hasCondition(DisadvantagedCondition.class.getName());
        actor.removeCondition(DisadvantagedCondition.class.getName());

        // See if they pass the spell check!
        final int spellCheckRoll = getSpellCheckRoll(disadvantage);

        final boolean criticalSuccess = spellCheckRoll == RollOutcome.CRITICAL_SUCCESS;
        final boolean criticalFailure = spellCheckRoll == RollOutcome.CRITICAL_FAILURE;

        final int spellCheckModifier = actor.getStats().getWisdomModifier(); // Always uses Wisdom!

        if (criticalFailure) {
            lost = true; // Failed spell check!
            log.info(actor.getName() + " critically MISSES the spell check on " + name);
        } else if (criticalSuccess) {
            int hitPoints = D6.roll() + D6.roll();
            log.info(actor.getName() + " critically heals on " + target.getName() + " for " + hitPoints + " with a " + name);
            target.healDamage(hitPoints);
        } else if (spellCheckRoll + spellCheckModifier >= difficultyClass) {
            int hitPoints = D6.roll();
            log.info(actor.getName() + " heals on " + target.getName() + " for " + hitPoints + " with a " + name);
            target.healDamage(hitPoints);
        } else {
            lost = true; // Failed spell check!
            log.info(actor.getName() + " MISSES the spell check with a " + name);
        }

    }

}
