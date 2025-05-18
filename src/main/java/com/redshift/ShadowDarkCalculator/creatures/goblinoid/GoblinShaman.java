package com.redshift.ShadowDarkCalculator.creatures.goblinoid;

import com.redshift.ShadowDarkCalculator.actions.PerformOneAction;
import com.redshift.ShadowDarkCalculator.actions.spells.SingleTargetDamageSpell;
import com.redshift.ShadowDarkCalculator.actions.spells.SingleTargetSpell;
import com.redshift.ShadowDarkCalculator.actions.weapons.WeaponBuilder;
import com.redshift.ShadowDarkCalculator.conditions.DisadvantagedCondition;
import com.redshift.ShadowDarkCalculator.conditions.StupefiedCondition;
import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.creatures.Label;
import com.redshift.ShadowDarkCalculator.creatures.Monster;
import com.redshift.ShadowDarkCalculator.creatures.Stats;
import com.redshift.ShadowDarkCalculator.dice.MultipleDice;
import com.redshift.ShadowDarkCalculator.dice.RollModifier;
import com.redshift.ShadowDarkCalculator.dice.RollOutcome;
import com.redshift.ShadowDarkCalculator.targets.SingleTargetSelector;
import com.redshift.ShadowDarkCalculator.targets.WizardTargetSelector;

import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D4;
import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D8;

@Slf4j
public class GoblinShaman extends Monster {

    public GoblinShaman(String name) {
        super(
                name,
                4,
                new Stats(10, 12, 12, 9, 14, 12),
                12,
                D8.roll() + D8.roll() + D8.roll() + D8.roll() + 1,
                new PerformOneAction(
                        WeaponBuilder.STAFF.build(),
                        new BugBrain().addSpellCheckBonus(1).setPriority(2),  // Spells +3 check... 2 from WIS 1 bonus.
                        new StinkBomb().addSpellCheckBonus(1).setPriority(4)) // Spells +3 check... 2 from WIS 1 bonus.
        );
        getLabels().add(Label.CASTER);
    }

    public static class BugBrain extends SingleTargetSpell {

        public BugBrain() {
            super("BugBrain", 13, RollModifier.WISDOM);
        }

        @Override
        public boolean canPerform(Creature actor, List<Creature> enemies, List<Creature> allies) {
            final SingleTargetSelector selector = new WizardTargetSelector();
            final Creature wizard = selector.get(enemies);
            return (!lost && wizard != null);
        }

        @Override
        public void perform(Creature actor, List<Creature> enemies, List<Creature> allies) {
            final SingleTargetSelector selector = new WizardTargetSelector();
            final Creature target = selector.get(enemies);

            boolean disadvantage = actor.hasCondition(DisadvantagedCondition.class.getName());
            actor.removeCondition(DisadvantagedCondition.class.getName());

            final int spellCheckRoll = getSpellCheckRoll(disadvantage);

            final boolean criticalSuccess = spellCheckRoll == RollOutcome.CRITICAL_SUCCESS;
            final boolean criticalFailure = spellCheckRoll == RollOutcome.CRITICAL_FAILURE;

            int spellCheckModifier = 0;

            if (rollModifier.equals(RollModifier.INTELLIGENCE)) {
                spellCheckModifier = actor.getStats().getIntelligenceModifier();
            } else {
                spellCheckModifier = actor.getStats().getWisdomModifier();
            }

            if (criticalFailure) {
                lost = true; // Failed spell check!
                log.info("{} critically MISSES the spell check with a {}", actor.getName(), getName());
            } else if (criticalSuccess) {
                lost = true; // Use bug brain Once
                log.info("{} critically hits a spell on {} with a {}", actor.getName(), target.getName(), getName());
                target.addCondition(new StupefiedCondition(D4.roll() + D4.roll()));
            } else if (spellCheckRoll + spellCheckModifier >= difficultyClass) {
                lost = true; // Use bug brain Once
                log.info("{} hits a spell on {} with a {}", actor.getName(), target.getName(), getName());
                target.addCondition(new StupefiedCondition(D4.roll()));
            } else {
                lost = true; // Failed spell check!
                log.info("{} MISSES the spell check with a {}", actor.getName(), getName());
            }
        }
    }

    public static class StinkBomb extends SingleTargetDamageSpell {

        public StinkBomb() {
            super("Stink Bomb", 12, RollModifier.WISDOM, new MultipleDice(D4, D4), false);
        }

        @Override
        public void perform(Creature actor, List<Creature> enemies, List<Creature> allies) {
            final Creature target = actor.getSingleTargetSelector().get(enemies);

            if (target == null) {
                log.info("{} is skipping their turn... no target!", actor.getName());
            } else {
                performSingleTargetSpellAttack(actor, target, this, difficultyClass, damageDice, rollModifier);

                if (!target.getStats().constitutionSave(12)) {
                    log.info("{} is disadvantaged on their next attack/check!", target.getName());
                    target.addCondition(new DisadvantagedCondition());
                } else {
                    log.info("{} SAVES and is NOT disadvantaged!", target.getName());
                }
            }
        }
    }

}