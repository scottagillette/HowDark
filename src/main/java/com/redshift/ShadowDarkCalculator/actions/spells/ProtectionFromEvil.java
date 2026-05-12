package com.redshift.ShadowDarkCalculator.actions.spells;

import com.redshift.ShadowDarkCalculator.conditions.ProtectionFromEvilCondition;
import com.redshift.ShadowDarkCalculator.conditions.SpellFocusCondition;
import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.dice.RollModifier;
import com.redshift.ShadowDarkCalculator.dice.RollOutcome;
import com.redshift.ShadowDarkCalculator.encounter.Encounter;
import com.redshift.ShadowDarkCalculator.targets.single.ProtectionFromEvilTargetSelector;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * Tier 1, priest, wizard
 * Duration: Focus
 * Range: Close
 *
 * For the spell’s duration, chaotic beings have disadvantage on attack rolls and hostile
 * spell casting checks against the target.
 * TODO: These beings also can’t possess, compel, or beguile it.
 * TODO: When cast on an already-possessed target, the possessing entity makes a CHA
 * TODO: check vs. the last spell casting check. On a failure, the entity is expelled.
 */

@Slf4j
public class ProtectionFromEvil extends Spell {

    public ProtectionFromEvil() {
        super("Protection from Evil", 11, RollModifier.WISDOM);
    }

    @Override
    public boolean canPerform(Creature actor, List<Creature> enemies, List<Creature> allies) {
        final boolean canPerform = super.canPerform(actor, enemies, allies);
        final boolean hasTarget = new ProtectionFromEvilTargetSelector().get(allies) != null;
        final boolean hasFocus = actor.hasCondition(SpellFocusCondition.class.getName());

        return canPerform && hasTarget && !hasFocus;
    }

    @Override
    public void perform(Creature actor, List<Creature> enemies, List<Creature> allies, Encounter encounter) {
        final Creature target = new ProtectionFromEvilTargetSelector().get(allies);

        final int spellCheckModifier = actor.getStats().getWisdomModifier(); // Always uses Wisdom modifier!

        // See if they pass the spell check!
        final int spellCheckRoll = getSpellCheckRoll(actor, List.of(), spellCheckModifier);

        final boolean criticalSuccess = spellCheckRoll == RollOutcome.CRITICAL_SUCCESS;
        final boolean criticalFailure = spellCheckRoll == RollOutcome.CRITICAL_FAILURE;

        if (criticalFailure) {
            lost = true; // Failed spell check!
            log.info("{} critically MISSES the spell check on {}", actor.getName(), name);
        } else if (criticalSuccess) {
            actor.addCondition(new SpellFocusCondition(
                    11,
                    RollModifier.WISDOM,
                    spellCheckAdvantage,
                    spellCheckBonus,
                    new RemoveProtectionFromEvilCondition(target)
            ));
            target.addCondition(new ProtectionFromEvilCondition());
            log.info("{} critically succeeds casting {} on {}", actor.getName(), name, target.getName());
        } else if (spellCheckRoll + spellCheckModifier + spellCheckBonus >= difficultyClass) {
            actor.addCondition(new SpellFocusCondition(
                    11,
                    RollModifier.WISDOM,
                    spellCheckAdvantage,
                    spellCheckBonus,
                    new RemoveProtectionFromEvilCondition(target)
            ));
            target.addCondition(new ProtectionFromEvilCondition());
            log.info("{} casts {} on {}", actor.getName(), name, target.getName());
        } else {
            lost = true; // Failed spell check!
            log.info("{} MISSES the spell check with a {}", actor.getName(), name);
        }
    }


    /**
     * Runnable to remove the Protection from Evil Condition on spell focus loss.
     */

    private static class RemoveProtectionFromEvilCondition implements Runnable {

        private final Creature creature;

        private RemoveProtectionFromEvilCondition(Creature creature) {
            this.creature = creature;
        }

        @Override
        public void run() {
            log.info("{} is no longer protected from evil!", creature.getName());
            creature.removeCondition(ProtectionFromEvilCondition.class.getName());
        }
    }

}
