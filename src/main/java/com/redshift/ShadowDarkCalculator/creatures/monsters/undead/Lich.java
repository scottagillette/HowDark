package com.redshift.ShadowDarkCalculator.creatures.monsters.undead;

import com.redshift.ShadowDarkCalculator.actions.DamageType;
import com.redshift.ShadowDarkCalculator.actions.PerformAllActions;
import com.redshift.ShadowDarkCalculator.actions.PerformOneAction;
import com.redshift.ShadowDarkCalculator.actions.spells.MultipleTargetDamageSpell;
import com.redshift.ShadowDarkCalculator.actions.spells.Spell;
import com.redshift.ShadowDarkCalculator.actions.weapons.Weapon;
import com.redshift.ShadowDarkCalculator.conditions.ParalyzedCondition;
import com.redshift.ShadowDarkCalculator.conditions.SpellResilience;
import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.creatures.CreatureLabel;
import com.redshift.ShadowDarkCalculator.creatures.Stats;
import com.redshift.ShadowDarkCalculator.creatures.monsters.UndeadMonster;
import com.redshift.ShadowDarkCalculator.dice.MultipleDice;
import com.redshift.ShadowDarkCalculator.dice.RollModifier;
import com.redshift.ShadowDarkCalculator.encounter.Encounter;
import com.redshift.ShadowDarkCalculator.resistance.NonMagicalImmunity;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.redshift.ShadowDarkCalculator.dice.DifficultyClass.HARD;
import static com.redshift.ShadowDarkCalculator.dice.SingleDie.*;

/**
 * A wizard who has completed a necromantic ritual to become a mighty, undead sorcerer.
 * Its withered body is draped in moldering, silk robes, and red marshlights burn in its
 * eyes.
 * AC 16, HP 62, ATK 2 touch +6 (2d8 + paralysis) and 2 spell +7, MV near
 * S +3, D +1, C +4, I +4, W +3, Ch +3, AL C, LV 13
 * Supreme Undead. Immune to morale checks. Only damaged by magical sources.
 * TODO: Phylactery. Can't be killed while spirit vessel (an object) is intact.
 * Paralysis. DC 15 CON or paralyzed 1d4 rounds.
 * TODO: Flight (INT Spell). Self. DC 13. Fly double near for 5 rounds.
 * Null (INT Spell). Self. DC 14. Hostile spells targeting lich are DC 18 to cast. Lasts 1d4 rounds.
 * TODO: Shadow Leap (INT Spell). Self. DC 14. Teleport up to 100 miles.
 * Sigil of Doom (INT Spell). DC 15. One target of LV 9 or less within near DC 15 CON or
 * go to 0 HP.
 * Wither (INT Spell). DC 14. 4d8 damage to enemies within a near-sized cube centered on lich.
 */

@Slf4j
public class Lich extends UndeadMonster {

    private final NonMagicalImmunity nonMagicalImmunity = new NonMagicalImmunity();

    public Lich(String name) {
        super(
                name,
                13,
                new Stats(17,12,18,18,17,17),
                16,
                D8.roll() + D8.roll() + D8.roll() + D8.roll() + D8.roll() +
                        D8.roll() + D8.roll() + D8.roll() + D8.roll() + D8.roll() +
                        D8.roll() + D8.roll() + D8.roll() + 4,
                new PerformAllActions(
                        new ParalyzingTouch(),
                        new ParalyzingTouch(),
                        new PerformOneAction( // Perform one of each; prioritize Null first.
                                new Wither().setPriority(1),
                                new Null().setPriority(5)
                        ),
                        new SigilOfDoom()
                )
        );
        getLabels().add(CreatureLabel.CASTER);
        getLabels().add(CreatureLabel.HUMANOID);
        getLabels().add(CreatureLabel.CHAOTIC);
    }

    @Override
    public void takeDamage(int amount, DamageType damageType) {
        // Only magical damage!
        final int damage = nonMagicalImmunity.calculateDamage(this, amount, damageType);
        if (damage != 0) {
            super.takeDamage(damage, damageType);
        }
    }

    /**
     * Paralysis. DC 15 CON or paralyzed 1d4 rounds.
     */

    private static class ParalyzingTouch extends Weapon {

        public ParalyzingTouch() {
            super("Paralyzing Touch", new MultipleDice(D8, D8), RollModifier.STRENGTH);
            addMagical().addSlashing().addAttackRollBonus(3);
        }

        @Override
        protected boolean performSingleTargetAttack(Creature actor, Creature target) {
            final boolean attackHits = super.performSingleTargetAttack(actor, target);

            if (attackHits & target.getCurrentHitPoints() != 0) {
                if (!target.isUnconscious() && !target.hasCondition(ParalyzedCondition.class.getName())) {
                    if (target.getStats().constitutionSave(HARD.getDc())) {
                        log.info("{} SAVES and is NOT paralyzed.", target.getName());
                    } else {
                        final int rounds = D4.roll();
                        log.info("{} is paralyzed for {} rounds!", target.getName(), rounds);
                        target.addCondition(new ParalyzedCondition(rounds));
                    }
                }
            }

            return attackHits;
        }
    }


    /**
     * Wither (INT Spell). DC 14. 4d8 damage to enemies within a near-sized cube centered on lich.
     */

    private static class Wither extends MultipleTargetDamageSpell {

        private Wither() {
            super("Wither", 14, RollModifier.INTELLIGENCE, new MultipleDice(D8, D8, D8, D8), D4);
            addSpellCheckBonus(3); // +7, 4 from int, +3
        }

    }

    /**
     * Sigil of Doom (INT Spell). DC 15. One target of LV 9 or less within near DC 15 CON or go to 0 HP.
     */

    private static class SigilOfDoom extends Spell {

        private SigilOfDoom() {
            super("Sigil of Doom", 15, RollModifier.INTELLIGENCE);
            addSpellCheckBonus(3); // +7, 4 from int, +3
        }

        @Override
        public boolean canPerform(Creature actor, List<Creature> enemies, List<Creature> allies) {
            final boolean canPerform = super.canPerform(actor, enemies, allies);
            final boolean hasTarget = actor.getSingleTargetSelector().get(enemies) != null;
            return canPerform && hasTarget;
        }

        @Override
        public List<Creature> getTargets(Creature actor, List<Creature> enemies, List<Creature> allies) {
            final Creature target = actor.getSingleTargetSelector().get(enemies);
            return (target == null) ? List.of() : List.of(target);
        }

        @Override
        public void performCriticalSpell(Creature actor, List<Creature> targets, Encounter encounter, int spellCheckRoll) {
            final Creature target = targets.getFirst();
            if (target.getLevel() <= 18) { // Increase the level for critical!
                log.info("{} critically casts {} on {}", actor.getName(), name, target.getName());
                final DamageType damageType = new DamageType();
                damageType.addMagical();
                target.takeDamage(target.getCurrentHitPoints(), damageType);
            } else {
                log.info("{} casts {} but doesn't affect the creature.", actor.getName(), name);
                lost = true; // Doesn't affect the creature... stop casting Sigil of Doom!
            }
        }

        @Override
        public void performSpell(Creature actor, List<Creature> targets, Encounter encounter, int spellCheckRoll) {
            final Creature target = targets.getFirst();
            if (target.getLevel() <= 9) {
                log.info("{} casts {} on {}", actor.getName(), name, target.getName());
                final DamageType damageType = new DamageType();
                damageType.addMagical();
                target.takeDamage(target.getCurrentHitPoints(), damageType);
            } else {
                log.info("{} casts {} but doesn't affect the creature.", actor.getName(), name);
                lost = true; // Doesn't affect the creature... stop casting Beguile!
            }
        }
    }

    /**
     * Null (INT Spell). Self. DC 14. Hostile spells targeting lich are DC 18 to cast. Lasts 1d4 rounds.
     */

    private static class Null extends Spell {

        private Null() {
            super("Null", 14, RollModifier.INTELLIGENCE);
            addSpellCheckBonus(3); // +7, 4 from int, +3
        }

        @Override
        public boolean canPerform(Creature actor, List<Creature> enemies, List<Creature> allies) {
            final boolean canPerform = super.canPerform(actor, enemies, allies);
            final boolean hasAlready = actor.hasCondition(SpellResilience.class.getName());
            return canPerform && !hasAlready;
        }

        @Override
        public List<Creature> getTargets(Creature actor, List<Creature> enemies, List<Creature> allies) {
            return List.of(actor);
        }

        @Override
        public void performCriticalSpell(Creature actor, List<Creature> targets, Encounter encounter, int spellCheckRoll) {
            final Creature target = targets.getFirst(); // Self
            log.info("{} critically casts {}", actor.getName(), name);
            target.addCondition(new SpellResilience(18, D8.roll())); // Increase duration!
            lost = true; // Just case this once.
        }

        @Override
        public void performSpell(Creature actor, List<Creature> targets, Encounter encounter, int spellCheckRoll) {
            final Creature target = targets.getFirst();
            log.info("{} casts {}", actor.getName(), name);
            target.addCondition(new SpellResilience(18, D4.roll()));
            lost = true; // Just case this once.
        }
    }
}
