package com.redshift.ShadowDarkCalculator.actions.spells;

import com.redshift.ShadowDarkCalculator.conditions.DisadvantagedCondition;
import com.redshift.ShadowDarkCalculator.conditions.HolyWeaponCondition;
import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.dice.RollModifier;
import com.redshift.ShadowDarkCalculator.dice.RollOutcome;
import com.redshift.ShadowDarkCalculator.encounter.CombatSimulator;
import com.redshift.ShadowDarkCalculator.targets.HolyWeaponTargetSelector;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * One weapon you touch is imbued with a sacred blessing. The weapon becomes magical and has +1 to attack and damage
 * rolls for the duration.
 */

@Slf4j
public class HolyWeapon extends Spell {

    public HolyWeapon() {
        super("Holy Weapon", 11, RollModifier.WISDOM);
    }

    @Override
    public boolean canPerform(Creature actor, List<Creature> enemies, List<Creature> allies) {
        // Need an ally that does not have a holy weapon.
        final Creature target = new HolyWeaponTargetSelector().get(allies);
        return (!lost && target != null);
    }

    @Override
    public void perform(Creature actor, List<Creature> enemies, List<Creature> allies, CombatSimulator simulator) {
        final Creature target = new HolyWeaponTargetSelector().get(allies);

        boolean disadvantage = actor.hasCondition(DisadvantagedCondition.class.getName());
        actor.removeCondition(DisadvantagedCondition.class.getName());

        // See if they pass the spell check!
        final int spellCheckRoll = getSpellCheckRoll(disadvantage);

        final boolean criticalSuccess = spellCheckRoll == RollOutcome.CRITICAL_SUCCESS;
        final boolean criticalFailure = spellCheckRoll == RollOutcome.CRITICAL_FAILURE;

        final int spellCheckModifier = actor.getStats().getWisdomModifier(); // Always uses Wisdom modifier!

        if (criticalFailure) {
            lost = true; // Failed spell check!
            log.info("{} critically MISSES the spell check on {}", actor.getName(), getName());
        } else if (criticalSuccess) {
            target.addCondition(new HolyWeaponCondition(10));
            log.info("{} critically succeeds casting {} on {}", actor.getName(), getName(), target.getName());
        } else if (spellCheckRoll + spellCheckModifier + spellCheckBonus >= difficultyClass) {
            target.addCondition(new HolyWeaponCondition());
            log.info("{} casts {} on {}", actor.getName(), getName(), target.getName());
        } else {
            lost = true; // Failed spell check!
            log.info("{} MISSES the spell check with a {}", actor.getName(), getName());
        }
    }
}
