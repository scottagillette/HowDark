package com.redshift.ShadowDarkCalculator.actions.spells;

import com.redshift.ShadowDarkCalculator.conditions.DisadvantagedCondition;
import com.redshift.ShadowDarkCalculator.dice.Dice;
import com.redshift.ShadowDarkCalculator.dice.MultipleDice;
import com.redshift.ShadowDarkCalculator.dice.RollModifier;
import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.dice.RollOutcome;
import com.redshift.ShadowDarkCalculator.encounter.Encounter;
import com.redshift.ShadowDarkCalculator.targets.HealTargetSelector;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D6;

/**
 * Tier 1, priest
 * Duration: Instant
 * Range: Close
 *
 * Your touch restores ebbing life. Roll a number of d6s equal to 1 + half your
 * level (rounded down). One target you touch regains that many hit points.
 */

@Slf4j
public class CureWounds extends Spell {

    public CureWounds() {
        super("Cure Wounds", 11, RollModifier.WISDOM);
    }

    @Override
    public boolean canPerform(Creature actor, List<Creature> enemies, List<Creature> allies) {
        return !lost && new HealTargetSelector().get(allies) != null;
    }

    @Override
    public void perform(Creature actor, List<Creature> enemies, List<Creature> allies, Encounter encounter) {
        final Creature target = new HealTargetSelector().get(allies); // Shouldn't get null since Spell.canPerform() returned true

        boolean disadvantage = actor.hasCondition(DisadvantagedCondition.class.getName());
        actor.removeCondition(DisadvantagedCondition.class.getName());

        // See if they pass the spell check!
        final int spellCheckRoll = getSpellCheckRoll(disadvantage);

        final boolean criticalSuccess = spellCheckRoll == RollOutcome.CRITICAL_SUCCESS;
        final boolean criticalFailure = spellCheckRoll == RollOutcome.CRITICAL_FAILURE;

        final int spellCheckModifier = actor.getStats().getWisdomModifier(); // Always uses Wisdom modifier!

        // Healing dice is 1d6 plus 1d6 dice per caster level divided by 2 rounded down.
        final Dice dice = new MultipleDice(D6, actor.getLevel() + (actor.getLevel() / 2));

        if (criticalFailure) {
            lost = true; // Failed spell check!
            log.info("{} critically MISSES the spell check on {}", actor.getName(), getName());
        } else if (criticalSuccess) {
            int hitPoints = dice.roll() + dice.roll();
            log.info("{} critically heals on {} for {} with a {}", actor.getName(), target.getName(), hitPoints, getName());
            target.healDamage(hitPoints);
        } else if (spellCheckRoll + spellCheckModifier + spellCheckBonus >= difficultyClass) {
            int hitPoints = dice.roll();
            log.info("{} heals on {} for {} with a {}", actor.getName(), target.getName(), hitPoints, getName());
            target.healDamage(hitPoints);
        } else {
            lost = true; // Failed spell check!
            log.info("{} MISSES the spell check with a {}", actor.getName(), getName());
        }
    }

}
