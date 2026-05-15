package com.redshift.ShadowDarkCalculator.creatures.monsters.humanoid;

import com.redshift.ShadowDarkCalculator.actions.PerformOneAction;
import com.redshift.ShadowDarkCalculator.actions.spells.SingleTargetDamageSpell;
import com.redshift.ShadowDarkCalculator.actions.spells.Spell;
import com.redshift.ShadowDarkCalculator.actions.weapons.WeaponBuilder;
import com.redshift.ShadowDarkCalculator.conditions.MageArmorCondition;
import com.redshift.ShadowDarkCalculator.conditions.ParalyzedCondition;
import com.redshift.ShadowDarkCalculator.conditions.SpellFocusCondition;
import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.creatures.CreatureLabel;
import com.redshift.ShadowDarkCalculator.creatures.Stats;
import com.redshift.ShadowDarkCalculator.creatures.monsters.Monster;
import com.redshift.ShadowDarkCalculator.dice.MultipleDice;
import com.redshift.ShadowDarkCalculator.dice.RollModifier;
import com.redshift.ShadowDarkCalculator.dice.RollOutcome;
import com.redshift.ShadowDarkCalculator.encounter.Encounter;
import com.redshift.ShadowDarkCalculator.targets.SingleTargetSelector;
import com.redshift.ShadowDarkCalculator.targets.single.FocusFireTargetSelector;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.*;
import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D6;

/**
 * Trained wizards who are often members of a sorcerous order.
 * AC 11, HP 27, ATK 1 spell +5, MV near
 * S -1, D +1, C +0, I +3, W +1, Ch +0, AL L, LV 6
 *
 * Arcane Armor (INT Spell). Self. DC 12. AC 16 for 2d4 rounds.
 * Blast (INT Spell). DC 12. Far, one target. 2d6 damage.
 * TODO: Cancel (INT Spell). DC 13. End one spell affecting a target within near.
 * TODO: Levitate (INT Spell). DC 12. Close. Focus. Hover near for duration, vertical movement only.
 * Snare (INT Spell). DC 13. Focus. One humanoid target within near paralyzed for duration.
 */

@Slf4j
public class Mage extends Monster {

    public Mage(String name) {
        super(
                name,
                6,
                new Stats(8, 12, 10, 16,12, 10),
                11,
                D8.roll() + D8.roll() + D8.roll() + D8.roll() + D8.roll() + D8.roll(),
                new PerformOneAction(
                        WeaponBuilder.STAFF.build().setPriority(1),
                        new ArcaneArmor().setPriority(3),
                        new Blast().setPriority(5),
                        new Snare().setPriority(5)
                ),
                new FocusFireTargetSelector() // Evil mages!
        );
        getLabels().add(CreatureLabel.BACKLINE);
        getLabels().add(CreatureLabel.HUMANOID);
        getLabels().add(CreatureLabel.WIZARD);
        getLabels().add(CreatureLabel.NEUTRAL);
    }

    /**
     * Arcane Armor (INT Spell). Self. DC 12. AC 16 for 2d4 rounds.
     */

    private static class ArcaneArmor extends Spell {

        public ArcaneArmor() {
            super("Arcane Armor", 12, RollModifier.INTELLIGENCE);
        }

        @Override
        public boolean canPerform(Creature actor, List<Creature> enemies, List<Creature> allies) {
            boolean canPerform = super.canPerform(actor, enemies, allies);
            // Don't perform if you already have the mage armor condition!
            return canPerform && !actor.hasCondition(MageArmorCondition.class.getName());
        }

        @Override
        public void perform(Creature actor, List<Creature> enemies, List<Creature> allies, Encounter encounter) {
            final int spellCheckModifier = actor.getStats().getIntelligenceModifier(); // Always uses INT modifier!

            // See if they pass the spell check!
            final int spellCheckRoll = getSpellCheckRoll(actor, List.of(), spellCheckModifier);

            final boolean criticalSuccess = spellCheckRoll == RollOutcome.CRITICAL_SUCCESS;
            final boolean criticalFailure = spellCheckRoll == RollOutcome.CRITICAL_FAILURE;

            if (criticalFailure) {
                lost = true;
                log.info("{} critically MISSES the spell check on {}", actor.getName(), name);
            } else if (criticalSuccess) {
                actor.addCondition(new MageArmorCondition(D4.roll() + D4.roll() + D4.roll() + D4.roll(), 16));
                log.info("{} critically casts {} for 16 AC for extended rounds!!", actor.getName(), name);
            } else if (spellCheckRoll + spellCheckModifier + spellCheckBonus >= difficultyClass) {
                actor.addCondition(new MageArmorCondition(D4.roll() + D4.roll(), 16));
                log.info("{} casts {} for 16 AC.", actor.getName(), name);
            } else {
                lost = true;
                log.info("{} MISSES the spell check with a {}", actor.getName(), name);
            }
        }

    }

    /**
     * Blast (INT Spell). DC 12. Far, one target. 2d6 damage.
     */

    private static class Blast extends SingleTargetDamageSpell {

        public Blast() {
            super("Blast", 12, RollModifier.INTELLIGENCE, new MultipleDice(D6, D6), false);
        }

    }

    /**
     * Snare (INT Spell). DC 13. Focus. One humanoid target within near paralyzed for duration.
     */

    private static class Snare extends Spell {

        private Snare() {
            super("Snare", 13, RollModifier.INTELLIGENCE);
        }

        @Override
        public boolean canPerform(Creature actor, List<Creature> enemies, List<Creature> allies) {
            final boolean canPerform = super.canPerform(actor, enemies, allies);
            final Creature target = new SnareTargetSelector().get(enemies);
            return canPerform && target != null;
        }

        @Override
        public void perform(Creature actor, List<Creature> enemies, List<Creature> allies, Encounter encounter) {
            final Creature target = new SnareTargetSelector().get(enemies);

            final int spellCheckModifier = actor.getStats().getIntelligenceModifier(); // Always uses INT modifier!

            // See if they pass the spell check!
            final int spellCheckRoll = getSpellCheckRoll(actor, List.of(), spellCheckModifier);

            final boolean criticalSuccess = spellCheckRoll == RollOutcome.CRITICAL_SUCCESS;
            final boolean criticalFailure = spellCheckRoll == RollOutcome.CRITICAL_FAILURE;

            if (criticalFailure) {
                lost = true;
                log.info("{} critically MISSES the spell check on {}", actor.getName(), name);
            } else if (criticalSuccess) {
                log.info("{} casts {} on {} and is paralyzed!", actor.getName(), name, target.getName());
                target.addCondition(new ParalyzedCondition(999));
                actor.addCondition(new SpellFocusCondition(
                        13,
                        RollModifier.INTELLIGENCE,
                        spellCheckAdvantage,
                        spellCheckBonus,
                        new RemoveParalyzedCondition(target)
                ));
            } else if (spellCheckRoll + spellCheckModifier + spellCheckBonus >= difficultyClass) {
                log.info("{} casts {} on {} and is paralyzed!", actor.getName(), name, target.getName());
                target.addCondition(new ParalyzedCondition(999));
                actor.addCondition(new SpellFocusCondition(
                        13,
                        RollModifier.INTELLIGENCE,
                        spellCheckAdvantage,
                        spellCheckBonus,
                        new RemoveParalyzedCondition(target)
                ));
            } else {
                lost = true;
                log.info("{} MISSES the spell check with a {}", actor.getName(), name);
            }
        }

    }

    private static class SnareTargetSelector implements SingleTargetSelector {

        @Override
        public Creature get(List<Creature> targetOptions) {
            final List<Creature> targets = new java.util.ArrayList<>(targetOptions
                    .stream()
                    .filter(creature -> creature.getLabels().contains(CreatureLabel.HUMANOID))
                    .filter(creature -> !creature.hasCondition(ParalyzedCondition.class.getName()))
                    .filter(creature -> !creature.isUnconscious())
                    .filter(creature -> !creature.isDead())
                    .toList());

            if (targets.isEmpty()) {
                return null;
            } else {
                Collections.shuffle(targets);
                return targets.getFirst();
            }
        }

    }

    /**
     * Runnable to remove the Paralyzed Condition on spell focus loss.
     */

    private static class RemoveParalyzedCondition implements Runnable {

        private final Creature creature;

        private RemoveParalyzedCondition(Creature creature) {
            this.creature = creature;
        }

        @Override
        public void run() {
            log.info("{} is no longer paralyzed!", creature.getName());
            creature.removeCondition(ParalyzedCondition.class.getName());
        }
    }
}
