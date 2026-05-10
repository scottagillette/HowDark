package com.redshift.ShadowDarkCalculator.actions.spells;

import com.redshift.ShadowDarkCalculator.conditions.ShieldOfFaithCondition;
import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.dice.RollModifier;
import com.redshift.ShadowDarkCalculator.dice.RollOutcome;
import com.redshift.ShadowDarkCalculator.encounter.Encounter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * Tier 1, priest
 * Duration: 5 rounds
 * Range: Self
 *
 * A protective force wrought of your holy conviction surrounds you. You gain a +2 bonus
 * to your armor class for the duration.
 */

@Slf4j
public class ShieldOfFaith extends Spell {

    public ShieldOfFaith() {
        super("Shield Of Faith", 11, RollModifier.WISDOM);
    }

    @Override
    public boolean canPerform(Creature actor, List<Creature> enemies, List<Creature> allies) {
        final boolean canPerform = super.canPerform(actor, enemies, allies);
        final boolean hasShieldOfFaith = actor.hasCondition(ShieldOfFaithCondition.class.getName());
        return canPerform && !hasShieldOfFaith;
    }

    @Override
    public void perform(Creature actor, List<Creature> enemies, List<Creature> allies, Encounter encounter) {
        final int spellCheckModifier = actor.getStats().getWisdomModifier(); // Always uses Wisdom modifier!

        final int d20Roll = getSpellCheckRoll(actor, spellCheckModifier);

        final boolean criticalSuccess = d20Roll == RollOutcome.CRITICAL_SUCCESS;
        final boolean criticalFailure = d20Roll == RollOutcome.CRITICAL_FAILURE;

        if (criticalFailure) {
            lost = true;
            log.info("{} critically MISSES the spell check on {}", actor.getName(), name);
        } else if (criticalSuccess) {
            actor.addCondition(new ShieldOfFaithCondition(4)); // Double AC for critical success
            log.info("{} critically adds 4 AC on {} with a {}", actor.getName(), actor.getName(), name);
        } else if (d20Roll + spellCheckModifier + spellCheckBonus >= difficultyClass) {
            actor.addCondition(new ShieldOfFaithCondition());
            log.info("{} adds 2 AC on {} with a {}", actor.getName(), actor.getName(), name);
        } else {
            lost = true;
            log.info("{} MISSES the spell check with a {}", actor.getName(), name);
        }

    }

}
