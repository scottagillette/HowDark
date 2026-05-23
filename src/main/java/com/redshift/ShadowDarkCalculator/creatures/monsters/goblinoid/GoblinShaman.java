package com.redshift.ShadowDarkCalculator.creatures.monsters.goblinoid;

import com.redshift.ShadowDarkCalculator.actions.PerformOneAction;
import com.redshift.ShadowDarkCalculator.actions.spells.SingleTargetDamageWithEffectSpell;
import com.redshift.ShadowDarkCalculator.actions.spells.Spell;
import com.redshift.ShadowDarkCalculator.actions.weapons.WeaponBuilder;
import com.redshift.ShadowDarkCalculator.conditions.DisadvantagedCondition;
import com.redshift.ShadowDarkCalculator.conditions.BugBrainedCondition;
import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.creatures.CreatureLabel;
import com.redshift.ShadowDarkCalculator.creatures.monsters.Monster;
import com.redshift.ShadowDarkCalculator.creatures.Stats;
import com.redshift.ShadowDarkCalculator.dice.MultipleDice;
import com.redshift.ShadowDarkCalculator.dice.RollModifier;
import com.redshift.ShadowDarkCalculator.dice.RollOutcome;
import com.redshift.ShadowDarkCalculator.encounter.Encounter;

import com.redshift.ShadowDarkCalculator.targets.multi.AliveAwakeNotUndeadTargetSelector;
import com.redshift.ShadowDarkCalculator.targets.multi.CreatureLabelTargetSelector;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D4;
import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D8;

/**
 * A swaying, chanting goblin wearing necklaces of teeth and a robe of musty rat pelts.
 * AC 12 (leather), HP 19, ATK 1 staff +0 (1d4) or 1 spell +3, MV near
 * S +0, D +1, C +1, I +0, W +2, Ch +1, AL C, LV 4
 * Keen Senses. Can't be surprised.
 * Bug Brain (WIS Spell). DC 13. Near range, one target. Target's INT drops to 1 for 1d4 rounds.
 * Skitter (WIS Spell). DC 12. Self. Climb like a spider for 5 rounds. // TODO: Not implemented
 * Stink Bomb (WIS Spell). DC 12. One target within far 2d4 damage and DC 12 CON or DISADV on next check/attack.
 */

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
        getLabels().add(CreatureLabel.BACKLINE);
        getLabels().add(CreatureLabel.CASTER);
        getLabels().add(CreatureLabel.HUMANOID);
        getLabels().add(CreatureLabel.CHAOTIC);
    }

    /**
     * INT drops to 1 for 1d4 rounds
     */

    public static class BugBrain extends Spell {

        public BugBrain() {
            super("Bug Brain", 13, RollModifier.WISDOM);
        }

        @Override
        public boolean canPerform(Creature actor, List<Creature> enemies, List<Creature> allies) {
            final List<Creature> candidateTargets = new AliveAwakeNotUndeadTargetSelector().getTargets(enemies, enemies.size());

            final CreatureLabelTargetSelector wizardSelector = new CreatureLabelTargetSelector(CreatureLabel.WIZARD);
            final List<Creature> wizards = wizardSelector.getTargets(candidateTargets, candidateTargets.size());

            return (!lost && !wizards.isEmpty());
        }

        @Override
        public void perform(Creature actor, List<Creature> enemies, List<Creature> allies, Encounter encounter) {
            final List<Creature> candidateTargets = new AliveAwakeNotUndeadTargetSelector().getTargets(enemies, enemies.size());

            final CreatureLabelTargetSelector wizardSelector = new CreatureLabelTargetSelector(CreatureLabel.WIZARD);
            final List<Creature> wizards = wizardSelector.getTargets(candidateTargets, candidateTargets.size());

            final Creature target = wizards.getFirst();

            int spellCheckModifier = actor.getStats().getWisdomModifier();

            final int d20Roll = getSpellCheckRoll(actor, List.of(target), spellCheckModifier);

            final boolean criticalSuccess = d20Roll == RollOutcome.CRITICAL_SUCCESS;
            final boolean criticalFailure = d20Roll == RollOutcome.CRITICAL_FAILURE;

            if (criticalFailure) {
                lost = true; // Failed spell check!
                log.info("{} critically MISSES the spell check with a {}", actor.getName(), name);
            } else if (criticalSuccess) {
                lost = true; // Use bug brain Once
                log.info("{} critically hits a spell on {} with a {}", actor.getName(), target.getName(), name);
                target.addCondition(new BugBrainedCondition(D4.roll() + D4.roll()));
            } else if (d20Roll + spellCheckModifier + spellCheckBonus >= difficultyClass) {
                lost = true; // Use bug brain Once
                log.info("{} hits a spell on {} with a {}", actor.getName(), target.getName(), name);
                target.addCondition(new BugBrainedCondition(D4.roll()));
            } else {
                lost = true; // Failed spell check!
                log.info("{} MISSES the spell check with a {}", actor.getName(), name);
            }
        }
    }

    public static class StinkBomb extends SingleTargetDamageWithEffectSpell {

        public StinkBomb() {
            super("Stink Bomb", 12, RollModifier.WISDOM, new MultipleDice(D4, D4), false);
        }

        @Override
        public void performEffect(Creature actor, Creature target, List<Creature> allies, Encounter encounter) {
            if (!target.getStats().constitutionSave(12)) {
                log.info("{} is disadvantaged on their next attack/check!", target.getName());
                target.addCondition(new DisadvantagedCondition());
            } else {
                log.info("{} SAVES and is NOT disadvantaged!", target.getName());
            }
        }

    }

}