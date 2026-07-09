package com.redshift.ShadowDarkCalculator.creatures.monsters.goblinoid;

import com.redshift.ShadowDarkCalculator.actions.PerformOneAction;
import com.redshift.ShadowDarkCalculator.actions.spells.SingleTargetDamageSpell;
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
import com.redshift.ShadowDarkCalculator.encounter.Encounter;

import com.redshift.ShadowDarkCalculator.targets.multi.AliveAwakeNotUndeadTargetSelector;
import com.redshift.ShadowDarkCalculator.targets.multi.CreatureLabelTargetSelector;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.redshift.ShadowDarkCalculator.dice.DifficultyClass.NORMAL;
import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D4;
import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D8;

/**
 * A swaying, chanting goblin wearing necklaces of teeth and a robe of musty rat pelts.
 * AC 12 (leather), HP 19, ATK 1 staff +0 (1d4) or 1 spell +3, MV near
 * S +0, D +1, C +1, I +0, W +2, Ch +1, AL C, LV 4
 * TODO: Keen Senses. Can't be surprised.
 * Bug Brain (WIS Spell). DC 13. Near range, one target. Target's INT drops to 1 for 1d4 rounds.
 * TODO: Skitter (WIS Spell). DC 12. Self. Climb like a spider for 5 rounds.
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
     * Bug Brain (WIS Spell). DC 13. Near range, one target. Target's INT drops to 1 for 1d4 rounds.
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
        public List<Creature> getTargets(Creature actor, List<Creature> enemies, List<Creature> allies) {
            final List<Creature> candidateTargets = new AliveAwakeNotUndeadTargetSelector().getTargets(enemies, enemies.size());
            final List<Creature> targets = new CreatureLabelTargetSelector(CreatureLabel.WIZARD).getTargets(candidateTargets, candidateTargets.size());
            return List.of(actor.getSingleTargetSelector().get(targets));
        }

        @Override
        public void performCriticalSpell(Creature actor, List<Creature> targets, Encounter encounter, int spellCheckRoll) {
            final Creature target = targets.getFirst();
            lost = true; // Use bug brain Once
            log.info("{} critically hits a spell on {} with a {}", actor.getName(), target.getName(), name);
            target.addCondition(new BugBrainedCondition(D4.roll() + D4.roll()));
        }

        @Override
        public void performSpell(Creature actor, List<Creature> targets, Encounter encounter, int spellCheckRoll) {
            final Creature target = targets.getFirst();
            lost = true; // Use bug brain Once
            log.info("{} hits a spell on {} with a {}", actor.getName(), target.getName(), name);
            target.addCondition(new BugBrainedCondition(D4.roll()));
        }

    }

    /**
     * Stink Bomb (WIS Spell). DC 12. One target within far 2d4 damage and DC 12 CON or DISADV on next check/attack.
     */

    public static class StinkBomb extends SingleTargetDamageSpell {

        public StinkBomb() {
            super("Stink Bomb", 12, RollModifier.WISDOM, new MultipleDice(D4, D4), false);
        }

        @Override
        public void performCriticalSpell(Creature actor, List<Creature> targets, Encounter encounter, int spellCheckRoll) {
            super.performCriticalSpell(actor, targets, encounter, spellCheckRoll);
            final Creature target = targets.getFirst();
            performEffect(target);
        }

        @Override
        public void performSpell(Creature actor, List<Creature> targets, Encounter encounter, int spellCheckRoll) {
            super.performSpell(actor, targets, encounter, spellCheckRoll);
            final Creature target = targets.getFirst();
            performEffect(target);
        }

        private void performEffect(Creature target) {
            if (!target.isDead()) {
                if (target.getStats().constitutionSave(NORMAL.getDc())) {
                    log.info("{} SAVES and is NOT disadvantaged!", target.getName());
                } else {
                    log.info("{} is disadvantaged on their next attack/check!", target.getName());
                    target.addCondition(new DisadvantagedCondition());
                }
            }
        }

    }

}