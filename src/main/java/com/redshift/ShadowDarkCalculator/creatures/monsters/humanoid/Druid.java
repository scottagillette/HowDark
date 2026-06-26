package com.redshift.ShadowDarkCalculator.creatures.monsters.humanoid;

import com.redshift.ShadowDarkCalculator.actions.PerformAllActions;
import com.redshift.ShadowDarkCalculator.actions.PerformOneAction;
import com.redshift.ShadowDarkCalculator.actions.spells.SingleTargetDamageSpell;
import com.redshift.ShadowDarkCalculator.actions.spells.Spell;
import com.redshift.ShadowDarkCalculator.actions.weapons.Weapon;
import com.redshift.ShadowDarkCalculator.actions.weapons.WeaponBuilder;
import com.redshift.ShadowDarkCalculator.conditions.HolyWeaponCondition;
import com.redshift.ShadowDarkCalculator.conditions.MageArmorCondition;
import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.creatures.CreatureLabel;
import com.redshift.ShadowDarkCalculator.creatures.Stats;
import com.redshift.ShadowDarkCalculator.creatures.monsters.Monster;
import com.redshift.ShadowDarkCalculator.creatures.monsters.beasts.BrownBear;
import com.redshift.ShadowDarkCalculator.dice.MultipleDice;
import com.redshift.ShadowDarkCalculator.dice.RollModifier;
import com.redshift.ShadowDarkCalculator.encounter.Encounter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.*;

/**
 * A wizard of the wilds holding a knotted staff and wearing a mossy cloak
 * of deep viridian.
 * AC 11, HP 31, ATK 1 staff +0 (1d4) or 2 spell +5, MV near
 * S +0, D +1, C +0, I +4, W +3, Ch +0, AL N, LV 7
 * Barkskin (INT Spell). Self. DC 13. AC becomes 15 for 5 rounds.
 * Conjure Flames (INT Spell). DC 12. One target in far takes 2d6 damage.
 * Imbue (INT Spell). Self. DC 13. Staff becomes a +3 magic weapon for 10 rounds.
 * Summon Bear (INT Spell). DC 14. Summon a loyal brown bear that appears within
 * near. It stays for 5 rounds.
 * TODO: Thunderclap (INT Spell). DC 13. Fills a near-sized cube extending from druid.
 * TODO: Creatures within are thrown 2d20 feet in a random direction.
 */

@Slf4j
public class Druid extends Monster {

    public Druid(String name) {
        super(
                name,
                7,
                new Stats(10, 12, 10, 18,16, 10),
                11,
                D8.roll() + D8.roll() + D8.roll() + D8.roll() +
                        D8.roll() + D8.roll() + D8.roll(),
                new PerformAllActions()
        );
        getLabels().add(CreatureLabel.FRONT_LINE);
        getLabels().add(CreatureLabel.HUMANOID);
        getLabels().add(CreatureLabel.LAWFUL);

        final Spell barkskin = new Barkskin();
        barkskin.setPriority(5);

        final Spell conjureFlames = new ConjureFlames();
        conjureFlames.setPriority(5);

        final Spell imbue = new Imbue();
        imbue.setPriority(5);

        final Spell summonBear = new SummonBear();
        summonBear.setPriority(5);

        final Weapon staff = WeaponBuilder.STAFF.build();
        staff.setPriority(1);

        final PerformOneAction group1 = new PerformOneAction(staff, barkskin, conjureFlames, imbue, summonBear);
        final PerformOneAction group2 = new PerformOneAction(staff, barkskin, conjureFlames, imbue, summonBear);

        final PerformAllActions performAllActions = (PerformAllActions) getAction();
        performAllActions.addAction(group1);
        performAllActions.addAction(group2);
    }


    /**
     * Barkskin (INT Spell). Self. DC 13. AC becomes 15 for 5 rounds.
     */

    private static class Barkskin extends Spell {

        public Barkskin() {
            super("Barkskin", 13, RollModifier.INTELLIGENCE);
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
            actor.addCondition(new MageArmorCondition(10, 15)); // Double duration!
            log.info("{} critically casts {} for 15 AC for extended rounds!!", actor.getName(), name);
        }

        @Override
        public void performSpell(Creature actor, List<Creature> targets, Encounter encounter, int spellCheckRoll) {
            actor.addCondition(new MageArmorCondition(5, 15));
            log.info("{} casts {} for 15 AC.", actor.getName(), name);
        }

    }

    /**
     * Conjure Flames (INT Spell). DC 12. One target in far takes 2d6 damage.
     */

    private static class ConjureFlames extends SingleTargetDamageSpell {

        public ConjureFlames() {
            super("Conjure Flames", 12, RollModifier.INTELLIGENCE, new MultipleDice(D6, D6), false);
        }

    }

    /**
     * Imbue (INT Spell). Self. DC 13. Staff becomes a +3 magic weapon for 10 rounds.
     */

    private static class Imbue extends Spell {

        private Imbue() {
            super("Imbue", 13, RollModifier.INTELLIGENCE);
        }

        @Override
        public boolean canPerform(Creature actor, List<Creature> enemies, List<Creature> allies) {
            final boolean canPerform = super.canPerform(actor, enemies, allies);
            final boolean hasImbue = actor.hasCondition(HolyWeaponCondition.class.getName());
            return (canPerform && !hasImbue);
        }

        @Override
        public List<Creature> getTargets(Creature actor, List<Creature> enemies, List<Creature> allies) {
            return List.of(actor);
        }

        @Override
        public void performCriticalSpell(Creature actor, List<Creature> targets, Encounter encounter, int spellCheckRoll) {
            final Creature target = targets.getFirst(); // Always uses single target.
            target.addCondition(new HolyWeaponCondition(20, 3));
            log.info("{} critically succeeds casting {}", actor.getName(), name);
        }

        @Override
        public void performSpell(Creature actor, List<Creature> targets, Encounter encounter, int spellCheckRoll) {
            final Creature target = targets.getFirst(); // Always uses single target.
            target.addCondition(new HolyWeaponCondition(10, 3));
            log.info("{} casts {}", actor.getName(), name);
        }

    }

    /**
     * Summon Bear (INT Spell). DC 14. Summon a loyal brown bear that appears within
     * near. It stays for 5 rounds.
     */

    private static class SummonBear extends Spell {

        private int count = 1;

        private SummonBear() {
            super("SummonBear", 14, RollModifier.INTELLIGENCE);
        }

        @Override
        public List<Creature> getTargets(Creature actor, List<Creature> enemies, List<Creature> allies) {
            return List.of(actor);
        }

        @Override
        public void performCriticalSpell(Creature actor, List<Creature> targets, Encounter encounter, int spellCheckRoll) {
            // TODO: Disapear after 10 rounds!
            log.info("{} summons a brown bear!", actor.getName());
            encounter.addFriendlyCreature(actor, new BrownBear("Brown Bear " + count++));
        }

        @Override
        public void performSpell(Creature actor, List<Creature> targets, Encounter encounter, int spellCheckRoll) {
            // TODO: Disapear after 5 rounds!
            log.info("{} summons a brown bear!", actor.getName());
            encounter.addFriendlyCreature(actor, new BrownBear("Brown Bear " + count++));
        }
    }

}
