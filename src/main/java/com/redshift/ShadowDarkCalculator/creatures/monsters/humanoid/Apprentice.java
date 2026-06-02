package com.redshift.ShadowDarkCalculator.creatures.monsters.humanoid;

import com.redshift.ShadowDarkCalculator.actions.PerformOneAction;
import com.redshift.ShadowDarkCalculator.actions.spells.SingleTargetDamageSpell;
import com.redshift.ShadowDarkCalculator.actions.spells.Spell;
import com.redshift.ShadowDarkCalculator.actions.weapons.WeaponBuilder;
import com.redshift.ShadowDarkCalculator.conditions.ProtectionFromEvilCondition;
import com.redshift.ShadowDarkCalculator.conditions.StupefiedCondition;
import com.redshift.ShadowDarkCalculator.conditions.SpellFocusCondition;
import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.creatures.CreatureLabel;
import com.redshift.ShadowDarkCalculator.creatures.Stats;
import com.redshift.ShadowDarkCalculator.creatures.monsters.Monster;
import com.redshift.ShadowDarkCalculator.dice.RollModifier;
import com.redshift.ShadowDarkCalculator.encounter.Encounter;
import com.redshift.ShadowDarkCalculator.targets.SingleTargetSelector;
import com.redshift.ShadowDarkCalculator.targets.multi.AliveAwakeTargetSelector;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D4;
import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D8;

/**
 * A cloaked magician with a thin, freshly bound spell book.
 * AC 11, HP 3
 * ATK 1 dagger (close/near) +1 (1d4) or 1 spell +2, MV near
 * S -1, D +1, C -1, I +2, W +0, Ch +0, AL N, LV 1
 * Beguile (INT Spell). DC 11. Focus. One target in near of LV 2 or less is stupefied for the duration.
 * Magic Bolt (INT Spell). DC 11. 1d4 damage to one target within far.
 */

@Slf4j
public class Apprentice extends Monster {

    public Apprentice(String name) {
        super(
                name,
                1,
                new Stats(8, 12, 8, 14,10, 10),
                11,
                D8.roll() - 1,
                new PerformOneAction(
                        WeaponBuilder.DAGGER_DEX.build().setPriority(1),
                        new Beguile().setPriority(2),
                        new MagicBolt().setPriority(2)
                )
        );
        getLabels().add(CreatureLabel.BACKLINE);
        getLabels().add(CreatureLabel.HUMANOID);
        getLabels().add(CreatureLabel.WIZARD);
        getLabels().add(CreatureLabel.NEUTRAL);
    }

    private static class MagicBolt extends SingleTargetDamageSpell {

        private MagicBolt() {
            super("Magic Bolt", 11, RollModifier.INTELLIGENCE, D4, false);
        }

    }

    /**
     * Beguile (INT Spell). DC 11. Focus. One target in near of LV 2 or less is stupefied for the duration.
     */

    private static class Beguile extends Spell {

        private Beguile() {
            super("Beguile", 11, RollModifier.INTELLIGENCE);
        }

        @Override
        public boolean canPerform(Creature actor, List<Creature> enemies, List<Creature> allies) {
            final boolean canPerform = super.canPerform(actor, enemies, allies);
            final boolean hasTarget = new BeguileTargetSelector().get(enemies) != null;
            final boolean hasFocus = actor.hasCondition(SpellFocusCondition.class.getName());
            return canPerform && hasTarget && !hasFocus;
        }

        @Override
        public List<Creature> getTargets(Creature actor, List<Creature> enemies, List<Creature> allies) {
            return List.of(new BeguileTargetSelector().get(enemies));
        }

        @Override
        public void performCriticalSpell(Creature actor, List<Creature> targets, Encounter encounter, int spellCheckRoll) {
            final Creature target = targets.getFirst();
            if (target.getLevel() <= 4) { // Increase the level for critical!
                log.info("{} critically casts {} on {}", actor.getName(), name, target.getName());
                actor.addCondition(new SpellFocusCondition(
                        11,
                        RollModifier.INTELLIGENCE,
                        spellCheckAdvantage,
                        spellCheckBonus,
                        new RemoveStupefiedCondition(target)
                ));
                target.addCondition(new StupefiedCondition()); // Until focus lost or attacked
            } else {
                log.info("{} casts {} but doesn't affect the creature.", actor.getName(), name);
                lost = true; // Doesn't affect the creature... stop casting Beguile!
            }
        }

        @Override
        public void performSpell(Creature actor, List<Creature> targets, Encounter encounter, int spellCheckRoll) {
            final Creature target = targets.getFirst();
            if (target.getLevel() <=2) {
                log.info("{} casts {} on {}", actor.getName(), name, target.getName());
                actor.addCondition(new SpellFocusCondition(
                        11,
                        RollModifier.INTELLIGENCE,
                        spellCheckAdvantage,
                        spellCheckBonus,
                        new RemoveStupefiedCondition(target)
                ));
                target.addCondition(new StupefiedCondition()); //  Until focus lost or attacked
            } else {
                log.info("{} casts {} but doesn't affect the creature.", actor.getName(), name);
                lost = true; // Doesn't affect the creature... stop casting Beguile!
            }
        }

    }

    /**
     * A single target selector for creatures not already with the stupefied condition.
     */

    private static class BeguileTargetSelector implements SingleTargetSelector {

        @Override
        public Creature get(List<Creature> targetOptions) {
            final List<Creature> aliveTargets = new AliveAwakeTargetSelector().getTargets(targetOptions, targetOptions.size());

            if (aliveTargets.isEmpty()) {
                return null; // No Targets.
            } else {
                final List<Creature> actualTargets = new java.util.ArrayList<>(aliveTargets.stream()
                        .filter(creature -> !creature.hasCondition(StupefiedCondition.class.getName()))
                        .filter(creature -> !creature.hasCondition(ProtectionFromEvilCondition.class.getName())) // Prevents spell effect
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
     * Runnable to remove the Stupefied Condition on spell focus loss.
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
