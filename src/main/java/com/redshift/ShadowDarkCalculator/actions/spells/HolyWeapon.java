package com.redshift.ShadowDarkCalculator.actions.spells;

import com.redshift.ShadowDarkCalculator.conditions.HolyWeaponCondition;
import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.dice.RollModifier;
import com.redshift.ShadowDarkCalculator.dice.RollOutcome;
import com.redshift.ShadowDarkCalculator.encounter.Encounter;
import com.redshift.ShadowDarkCalculator.targets.single.HolyWeaponTargetSelector;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * Tier 1, priest
 * Duration: 5 rounds
 * Range: Close
 *
 * One weapon you touch is imbued with a sacred blessing. The weapon becomes magical
 * and has +1 to attack and damage rolls for the duration.
 */

@Slf4j
public class HolyWeapon extends Spell {

    public HolyWeapon() {
        super("Holy Weapon", 11, RollModifier.WISDOM);
    }

    @Override
    public boolean canPerform(Creature actor, List<Creature> enemies, List<Creature> allies) {
        final boolean canPerform = super.canPerform(actor, enemies, allies);
        final boolean hasTarget = new HolyWeaponTargetSelector().get(allies) != null;

        return (canPerform && hasTarget);
    }

    @Override
    public void perform(Creature actor, List<Creature> enemies, List<Creature> allies, Encounter encounter) {
        final Creature target = new HolyWeaponTargetSelector().get(allies);

        final int spellCheckModifier = actor.getStats().getWisdomModifier(); // Always uses Wisdom modifier!

        // See if they pass the spell check!
        final int spellCheckRoll = getSpellCheckRoll(actor, List.of(), spellCheckModifier);

        final boolean criticalSuccess = spellCheckRoll == RollOutcome.CRITICAL_SUCCESS;
        final boolean criticalFailure = spellCheckRoll == RollOutcome.CRITICAL_FAILURE;

        if (criticalFailure) {
            lost = true; // Failed spell check!
            log.info("{} critically MISSES the spell check on {}", actor.getName(), name);
        } else if (criticalSuccess) {
            target.addCondition(new HolyWeaponCondition(10));
            log.info("{} critically succeeds casting {} on {}", actor.getName(), name, target.getName());
        } else if (spellCheckRoll + spellCheckModifier + spellCheckBonus >= difficultyClass) {
            target.addCondition(new HolyWeaponCondition());
            log.info("{} casts {} on {}", actor.getName(), name, target.getName());
        } else {
            lost = true; // Failed spell check!
            log.info("{} MISSES the spell check with a {}", actor.getName(), name);
        }
    }
}
