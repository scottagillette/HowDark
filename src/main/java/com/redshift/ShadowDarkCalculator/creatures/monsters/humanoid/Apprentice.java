package com.redshift.ShadowDarkCalculator.creatures.monsters.humanoid;

import com.redshift.ShadowDarkCalculator.actions.PerformOneAction;
import com.redshift.ShadowDarkCalculator.actions.spells.SingleTargetDamageSpell;
import com.redshift.ShadowDarkCalculator.actions.spells.Spell;
import com.redshift.ShadowDarkCalculator.actions.weapons.WeaponBuilder;
import com.redshift.ShadowDarkCalculator.conditions.DazedAndConfusedCondition;
import com.redshift.ShadowDarkCalculator.conditions.SpellFocusCondition;
import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.creatures.CreatureLabel;
import com.redshift.ShadowDarkCalculator.creatures.Stats;
import com.redshift.ShadowDarkCalculator.creatures.monsters.Monster;
import com.redshift.ShadowDarkCalculator.dice.RollModifier;
import com.redshift.ShadowDarkCalculator.dice.RollOutcome;
import com.redshift.ShadowDarkCalculator.encounter.Encounter;
import com.redshift.ShadowDarkCalculator.targets.single.RandomTargetSelector;
import lombok.extern.slf4j.Slf4j;

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

    private static class Beguile extends Spell {

        private Beguile() {
            super("Beguile", 11, RollModifier.INTELLIGENCE);
        }

        @Override
        public boolean canPerform(Creature actor, List<Creature> enemies, List<Creature> allies) {
            final boolean canPerform = super.canPerform(actor, enemies, allies);
            final Creature target = new RandomTargetSelector().get(enemies);
            final boolean hasTarget = target != null && !target.hasCondition(DazedAndConfusedCondition.class.getName());
            final boolean hasFocus = actor.hasCondition(SpellFocusCondition.class.getName());

            return canPerform && hasTarget && !hasFocus;
        }

        @Override
        public void perform(Creature actor, List<Creature> enemies, List<Creature> allies, Encounter encounter) {
            final Creature target = new RandomTargetSelector().get(enemies);

            final int spellCheckModifier = actor.getStats().getIntelligenceModifier(); // Always uses INT!

            final int d20Roll = getSpellCheckRoll(actor, List.of(target), spellCheckModifier);

            final boolean criticalSuccess = d20Roll == RollOutcome.CRITICAL_SUCCESS;
            final boolean criticalFailure = d20Roll == RollOutcome.CRITICAL_FAILURE;

            if (target.getLevel() <= 2) { // Only affects level 2 and below!
                if (criticalFailure) {
                    lost = true; // Failed spell check!
                    log.info("{} critically MISSES the spell check on {}", actor.getName(), name);
                } else if (criticalSuccess) {
                    log.info("{} critically casts {} on {}", actor.getName(), name, target.getName());
                    actor.addCondition(new SpellFocusCondition(
                            11,
                            RollModifier.INTELLIGENCE,
                            spellCheckAdvantage,
                            spellCheckBonus,
                            new RemoveDazedAndConfusedCondition(target)
                    ));
                    target.addCondition(new DazedAndConfusedCondition()); // Until focus lost or attacked
                } else if (d20Roll + spellCheckModifier + spellCheckBonus >= difficultyClass) {
                    log.info("{} casts {} on {}", actor.getName(), name, target.getName());
                    actor.addCondition(new SpellFocusCondition(
                            11,
                            RollModifier.INTELLIGENCE,
                            spellCheckAdvantage,
                            spellCheckBonus,
                            new RemoveDazedAndConfusedCondition(target)
                    ));
                    target.addCondition(new DazedAndConfusedCondition()); //  Until focus lost or attacked
                } else {
                    lost = true; // Failed spell check!
                    log.info("{} MISSES the spell check with a {}", actor.getName(), name);
                }
            } else {
                log.info("{} casts {} on {} with no effect!", actor.getName(), name, target.getName());
            }
        }
    }

    /**
     * Runnable to remove the Stupefied Condition on spell focus loss.
     */

    private static class RemoveDazedAndConfusedCondition implements Runnable {

        private final Creature creature;

        private RemoveDazedAndConfusedCondition(Creature creature) {
            this.creature = creature;
        }

        @Override
        public void run() {
            if (creature.hasCondition(DazedAndConfusedCondition.class.getName())) {
                log.info("{} is no longer stupefied!", creature.getName());
                creature.removeCondition(DazedAndConfusedCondition.class.getName());
            }
        }
    }
}
