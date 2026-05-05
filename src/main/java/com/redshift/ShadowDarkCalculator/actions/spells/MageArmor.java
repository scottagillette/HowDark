package com.redshift.ShadowDarkCalculator.actions.spells;

import com.redshift.ShadowDarkCalculator.conditions.DisadvantagedCondition;
import com.redshift.ShadowDarkCalculator.conditions.MageArmorCondition;
import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.dice.RollModifier;
import com.redshift.ShadowDarkCalculator.dice.RollOutcome;
import com.redshift.ShadowDarkCalculator.encounter.Encounter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * Tier 1, wizard
 * Duration: 10 rounds
 * Range: Self
 *
 * An invisible layer of magical force protects your vitals. Your armor class becomes 14
 * (18 on a critical spell casting roll)
 */

@Slf4j
public class MageArmor extends Spell {

    public MageArmor() {
        super("Mage Armor", 11, RollModifier.INTELLIGENCE);
    }

    @Override
    public boolean canPerform(Creature actor, List<Creature> enemies, List<Creature> allies) {
        // Don't perform if you already have the mage armor condition!
        boolean canPerform = super.canPerform(actor, enemies, allies);
        return canPerform && !actor.hasCondition(MageArmorCondition.class.getName());
    }

    @Override
    public void perform(Creature actor, List<Creature> enemies, List<Creature> allies, Encounter encounter) {
        final boolean disadvantage = actor.hasCondition(DisadvantagedCondition.class.getName());
        actor.removeCondition(DisadvantagedCondition.class.getName());

        // See if they pass the spell check!
        final int spellCheckRoll = getSpellCheckRoll(disadvantage);

        final boolean criticalSuccess = spellCheckRoll == RollOutcome.CRITICAL_SUCCESS;
        final boolean criticalFailure = spellCheckRoll == RollOutcome.CRITICAL_FAILURE;

        final int spellCheckModifier = actor.getStats().getWisdomModifier(); // Always uses Wisdom modifier!

        if (criticalFailure) {
            lost = true;
            log.info("{} critically MISSES the spell check on {}", actor.getName(), getName());
        } else if (criticalSuccess) {
            actor.addCondition(new MageArmorCondition(10, 18));
            log.info("{} critically casts {} for 18 AC!", actor.getName(), getName());
        } else if (spellCheckRoll + spellCheckModifier + spellCheckBonus >= difficultyClass) {
            actor.addCondition(new MageArmorCondition(10, 14));
            log.info("{} casts {} for 14 AC.", actor.getName(), getName());
        } else {
            lost = true;
            log.info("{} MISSES the spell check with a {}", actor.getName(), getName());
        }
    }

}
