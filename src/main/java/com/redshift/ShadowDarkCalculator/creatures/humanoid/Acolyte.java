package com.redshift.ShadowDarkCalculator.creatures.humanoid;

import com.redshift.ShadowDarkCalculator.actions.PerformAllActions;
import com.redshift.ShadowDarkCalculator.actions.spells.SingleTargetSpell;
import com.redshift.ShadowDarkCalculator.actions.weapons.WeaponBuilder;
import com.redshift.ShadowDarkCalculator.conditions.DisadvantagedCondition;
import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.creatures.Monster;
import com.redshift.ShadowDarkCalculator.creatures.Stats;
import com.redshift.ShadowDarkCalculator.dice.RollModifier;
import com.redshift.ShadowDarkCalculator.dice.RollOutcome;
import com.redshift.ShadowDarkCalculator.targets.HealTargetSelector;
import com.redshift.ShadowDarkCalculator.targets.SingleTargetSelector;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D4;
import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D8;

@Slf4j
public class Acolyte extends Monster {

    public Acolyte(String name) {
        super(
                name,
                1,
                new Stats(12, 8, 10, 8,14, 10),
                12,
                D8.roll(),
                new PerformAllActions(WeaponBuilder.MACE.build(), new HealingTouch().setPriority(4))
        );
    }

    private static class HealingTouch extends SingleTargetSpell {

        public HealingTouch() {
            super("Healing Touch", 11, RollModifier.WISDOM);
        }

        @Override
        public boolean canPerform(Creature actor, List<Creature> enemies, List<Creature> allies) {
            final SingleTargetSelector selector = new HealTargetSelector();
            return !lost && selector.get(allies) != null;
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
                log.info("{} critically MISSES the spell check on {}", actor.getName(), name);
            } else if (criticalSuccess) {
                int hitPoints = D4.roll() + D4.roll();
                log.info("{} critically heals on {} for {} with a {}", actor.getName(), target.getName(), hitPoints, name);
                target.healDamage(hitPoints);
            } else if (spellCheckRoll + spellCheckModifier >= difficultyClass) {
                int hitPoints = D4.roll();
                log.info("{} heals on {} for {} with a {}", actor.getName(), target.getName(), hitPoints, name);
                target.healDamage(hitPoints);
            } else {
                lost = true; // Failed spell check!
                log.info("{} MISSES the spell check with a {}", actor.getName(), name);
            }
        }
    }
}
