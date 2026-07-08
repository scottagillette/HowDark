package com.redshift.ShadowDarkCalculator.creatures.monsters.humanoid;

import com.redshift.ShadowDarkCalculator.actions.DamageType;
import com.redshift.ShadowDarkCalculator.actions.PerformAllActions;
import com.redshift.ShadowDarkCalculator.actions.PerformOneAction;
import com.redshift.ShadowDarkCalculator.actions.spells.MultipleTargetDamageSpell;
import com.redshift.ShadowDarkCalculator.actions.spells.Spell;
import com.redshift.ShadowDarkCalculator.actions.weapons.Weapon;
import com.redshift.ShadowDarkCalculator.actions.weapons.WeaponBuilder;
import com.redshift.ShadowDarkCalculator.conditions.*;
import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.creatures.CreatureLabel;
import com.redshift.ShadowDarkCalculator.creatures.Stats;
import com.redshift.ShadowDarkCalculator.creatures.monsters.Monster;
import com.redshift.ShadowDarkCalculator.dice.MultipleDice;
import com.redshift.ShadowDarkCalculator.dice.RollModifier;
import com.redshift.ShadowDarkCalculator.encounter.Encounter;
import com.redshift.ShadowDarkCalculator.targets.SingleTargetSelector;
import com.redshift.ShadowDarkCalculator.targets.multi.AliveAwakeTargetSelector;
import com.redshift.ShadowDarkCalculator.targets.single.FocusFireTargetSelector;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.*;

/**
 * A wizened magic-user crackling with arcane power.
 * AC 12, HP 44, ATK 2 spell +7, MV near
 * S -1, D +2, C -1, I +4, W +2, Ch +1, AL L, LV 10
 * Death Bolt (INT Spell). DC 15. One target of LV 9 or less within near DC 15 CON or go to 0 HP.
 * Enervate (INT Spell). DC 14. Focus. One target within near is stupefied for the duration.
 * Fireblast (INT Spell). DC 14. 4d6 damage to all within a near- sized cube within far.
 * TODO: Float (INT Spell). Self. DC 14. Fly double near for 5 rounds.
 * Mithralskin (INT Spell). Self. DC 14. AC becomes 18 for 5 rounds.
 * TODO: Void Step (INT Spell). Self and up to 4 willing targets. DC 15. Teleport up to 100 miles.
 */

@Slf4j
public class ArchMage extends Monster {

    public ArchMage(String name) {
        super(
                name,
                10,
                new Stats(8, 14, 8, 18, 14, 12),
                12,
                D8.roll() + D8.roll() + D8.roll() + D8.roll() + D8.roll() +
                        D8.roll() + D8.roll() + D8.roll() + D8.roll() + D8.roll() - 1,
                new PerformAllActions(), // Added post construction
                new FocusFireTargetSelector() // Evil mages!
        );
        getLabels().add(CreatureLabel.BACKLINE);
        getLabels().add(CreatureLabel.HUMANOID);
        getLabels().add(CreatureLabel.WIZARD);
        getLabels().add(CreatureLabel.LAWFUL);

        final Spell deathBolt = new DeathBolt();
        deathBolt.setPriority(5);

        final Spell enervate = new Enervate();
        enervate.setPriority(3);

        final Spell fireblast = new Fireblast();
        fireblast.setPriority(5);

        final Spell mithralskin = new Mithralskin();
        mithralskin.setPriority(10);

        final Weapon staff = WeaponBuilder.STAFF.build();
        staff.setPriority(1);

        final PerformOneAction group1 = new PerformOneAction(staff, deathBolt, enervate, mithralskin, fireblast);
        final PerformOneAction group2 = new PerformOneAction(staff, deathBolt, enervate, mithralskin, fireblast);

        final PerformAllActions performAllActions = (PerformAllActions) getAction();
        performAllActions.addAction(group1);
        performAllActions.addAction(group2);
    }

    /**
     * Death Bolt (INT Spell). DC 15. One target of LV 9 or less within near DC 15 CON or go to 0 HP.
     */

    private static class DeathBolt extends Spell {

        private DeathBolt()  {
            super("Death Bolt", 15, RollModifier.INTELLIGENCE);
        }

        @Override
        public boolean canPerform(Creature actor, List<Creature> enemies, List<Creature> allies) {
            final boolean canPerform = super.canPerform(actor, enemies, allies);
            final boolean hasTarget = !getTargets(actor, enemies, allies).isEmpty();
            return canPerform && hasTarget;
        }

        @Override
        public List<Creature> getTargets(Creature actor, List<Creature> enemies, List<Creature> allies) {
            final List<Creature> targets = new ArrayList<>();
            final Creature target = actor.getSingleTargetSelector().get(enemies);
            if (target != null) targets.add(target);
            return targets;
        }

        @Override
        public void performCriticalSpell(Creature actor, List<Creature> targets, Encounter encounter, int spellCheckRoll) {
            final Creature target = targets.getFirst(); // Always uses single target.
            if (target.getLevel() <= 18) { // Double 1 numeric value!
                log.info("{} critically succeeds on the spell {} and {} falls to the ground!", actor.getName(), name, target.getName());
                final DamageType damageType = new DamageType();
                damageType.addMagical();
                target.takeDamage(target.getCurrentHitPoints(), damageType);
            } else {
                log.info("{} casts {} but doesn't affect the creature.", actor.getName(), name);
                lost = true; // Doesn't affect the creature... stop casting Death Bolt!
            }
        }

        @Override
        public void performSpell(Creature actor, List<Creature> targets, Encounter encounter, int spellCheckRoll) {
            final Creature target = targets.getFirst(); // Always uses single target.
            if (target.getLevel() <= 9) { // Double 1 numeric value!
                log.info("{} succeeds on the spell {} and {} falls to the ground!", actor.getName(), name, target.getName());
                final DamageType damageType = new DamageType();
                damageType.addMagical();
                target.takeDamage(target.getCurrentHitPoints(), damageType);
            } else {
                log.info("{} casts {} but doesn't affect the creature.", actor.getName(), name);
                lost = true; // Doesn't affect the creature... stop casting Death Bolt!
            }
        }

    }

    /**
     * Mithralskin (INT Spell). Self. DC 14. AC becomes 18 for 5 rounds.
     */

    private static class Mithralskin extends Spell {

        public Mithralskin() {
            super("Mithralskin", 14, RollModifier.INTELLIGENCE);
        }

        @Override
        public boolean canPerform(Creature actor, List<Creature> enemies, List<Creature> allies) {
            boolean canPerform = super.canPerform(actor, enemies, allies);
            // Don't perform if you already have the mage armor condition!
            return canPerform && !actor.hasCondition(MageArmorCondition.class.getName());
        }

        @Override
        public List<Creature> getTargets(Creature actor, List<Creature> enemies, List<Creature> allies) {
            return List.of(actor);
        }

        @Override
        public void performCriticalSpell(Creature actor, List<Creature> targets, Encounter encounter, int spellCheckRoll) {
            actor.addCondition(new MageArmorCondition(10, 18)); // Double duration!
            log.info("{} critically casts {} for 18 AC for extended rounds!!", actor.getName(), name);
        }

        @Override
        public void performSpell(Creature actor, List<Creature> targets, Encounter encounter, int spellCheckRoll) {
            actor.addCondition(new MageArmorCondition(5, 18));
            log.info("{} casts {} for 18 AC.", actor.getName(), name);
        }

    }

    /**
     * Fireblast (INT Spell). DC 14. 4d6 damage to all within a near-sized cube within far.
     */

    private static class Fireblast extends MultipleTargetDamageSpell {

        public Fireblast() {
            super("Fireblast", 14, RollModifier.INTELLIGENCE, new MultipleDice(D6, D6, D6, D6), D4);
            damageType.addFire();
        }

    }

    /**
     * Enervate (INT Spell). DC 14. Focus. One target within near is stupefied for the duration.
     */

    private static class Enervate extends Spell {

        public Enervate() {
            super("Enervate", 14, RollModifier.INTELLIGENCE);
        }

        @Override
        public boolean canPerform(Creature actor, List<Creature> enemies, List<Creature> allies) {
            final boolean canPerform = super.canPerform(actor, enemies, allies);
            final boolean hasTarget = new EnervateTargetSelector().get(enemies) != null;
            final boolean hasFocus = actor.hasCondition(SpellFocusCondition.class.getName());
            return (canPerform && hasTarget && !hasFocus);
        }

        @Override
        public List<Creature> getTargets(Creature actor, List<Creature> enemies, List<Creature> allies) {
            final List<Creature> targets = new ArrayList<>();
            final Creature target = new EnervateTargetSelector().get(enemies);
            if (target != null) targets.add(target);
            return targets;
        }

        @Override
        public void performCriticalSpell(Creature actor, List<Creature> targets, Encounter encounter, int spellCheckRoll) {
            final Creature target = targets.getFirst(); // Always uses single target.

            log.info("{} critically succeeds on the spell {} and {} is stupefied!", actor.getName(), name, target.getName());
            target.addCondition(new StupefiedCondition());
            actor.addCondition(new SpellFocusCondition(
                    14,
                    RollModifier.CHARISMA,
                    spellCheckWithAdvantage,
                    spellCheckBonus,
                    new RemoveStupefiedCondition(target)
            ));
        }

        @Override
        public void performSpell(Creature actor, List<Creature> targets, Encounter encounter, int spellCheckRoll) {
            final Creature target = targets.getFirst(); // Always uses single target.

            log.info("{} succeeds on the spell {} and {} is stupefied!", actor.getName(), name, target.getName());
            target.addCondition(new StupefiedCondition());
            actor.addCondition(new SpellFocusCondition(
                    14,
                    RollModifier.CHARISMA,
                    spellCheckWithAdvantage,
                    spellCheckBonus,
                    new RemoveStupefiedCondition(target)
            ));
        }

    }

    /**
     * A target selector for the Enervate spell.
     */

    private static class EnervateTargetSelector implements SingleTargetSelector {

        @Override
        public Creature get(List<Creature> targetOptions) {
            final List<Creature> aliveTargets = new AliveAwakeTargetSelector().getTargets(targetOptions, targetOptions.size());

            if (aliveTargets.isEmpty()) {
                return null; // No Targets.
            } else {
                final List<Creature> actualTargets = new java.util.ArrayList<>(aliveTargets.stream()
                        .filter(creature -> !creature.hasCondition(StupefiedCondition.class.getName()))
                        .filter(creature -> !creature.hasCondition(SleepingCondition.class.getName()))
                        .filter(creature -> !creature.hasCondition(ParalyzedCondition.class.getName()))
                        .filter(creature -> !creature.hasCondition(FearCondition.class.getName()))
                        .toList());

                if (actualTargets.isEmpty()) {
                    return null; // No Targets!
                } else {
                    Collections.shuffle(actualTargets);
                    return actualTargets.getFirst();
                }
            }
        }

    }

    /**
     * Runnable to remove the Stupefied condition on spell focus loss.
     */

    private static class RemoveStupefiedCondition implements Runnable {

        private final Creature creature;

        private RemoveStupefiedCondition(Creature creature) {
            this.creature = creature;
        }

        @Override
        public void run() {
            if (creature.hasCondition(StupefiedCondition.class.getName())) {
                log.info("{} is no longer stupefied!", creature.getName());
                creature.removeCondition(StupefiedCondition.class.getName());
            }
        }
    }
}