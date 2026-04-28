package com.redshift.ShadowDarkCalculator.creatures.monsters.humanoid;

import com.redshift.ShadowDarkCalculator.actions.PerformOneAction;
import com.redshift.ShadowDarkCalculator.actions.spells.SingleTargetDamageSpell;
import com.redshift.ShadowDarkCalculator.actions.spells.Spell;
import com.redshift.ShadowDarkCalculator.actions.weapons.WeaponBuilder;
import com.redshift.ShadowDarkCalculator.conditions.DazedAndConfusedCondition;
import com.redshift.ShadowDarkCalculator.conditions.DisadvantagedCondition;
import com.redshift.ShadowDarkCalculator.conditions.SpellFocusCondition;
import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.creatures.CreatureLabel;
import com.redshift.ShadowDarkCalculator.creatures.Stats;
import com.redshift.ShadowDarkCalculator.creatures.monsters.Monster;
import com.redshift.ShadowDarkCalculator.dice.RollModifier;
import com.redshift.ShadowDarkCalculator.dice.RollOutcome;
import com.redshift.ShadowDarkCalculator.encounter.Encounter;
import com.redshift.ShadowDarkCalculator.targets.RandomTargetSelector;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D4;

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
                3,
                new PerformOneAction(
                        WeaponBuilder.DAGGER_DEX.build().setPriority(1),
                        new Beguile().setPriority(2),
                        new MagicBolt().setPriority(2)
                )
        );
        getLabels().add(CreatureLabel.BACKLINE);
        getLabels().add(CreatureLabel.HUMANOID);
        getLabels().add(CreatureLabel.WIZARD);
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
            final Creature target = new RandomTargetSelector().get(enemies);
            final boolean hasFocusAlready = actor.hasCondition(SpellFocusCondition.class.getName());
            // TODO: Could avoid anyone that is already Dazed and Confused...
            return !lost && !hasFocusAlready && target != null;
        }

        @Override
        public void perform(Creature actor, List<Creature> enemies, List<Creature> allies, Encounter encounter) {
            final Creature target = new RandomTargetSelector().get(enemies);

            boolean disadvantage = actor.hasCondition(DisadvantagedCondition.class.getName());
            actor.removeCondition(DisadvantagedCondition.class.getName());

            final int spellCheckRoll = getSpellCheckRoll(disadvantage);

            final boolean criticalSuccess = spellCheckRoll == RollOutcome.CRITICAL_SUCCESS;
            final boolean criticalFailure = spellCheckRoll == RollOutcome.CRITICAL_FAILURE;

            final int spellCheckModifier = actor.getStats().getIntelligenceModifier(); // Always uses INT!

            if (target.getLevel() <= 2) { // Only affects level 2 and below!
                if (criticalFailure) {
                    lost = true; // Failed spell check!
                    log.info("{} critically MISSES the spell check on {}", actor.getName(), getName());
                } else if (criticalSuccess) {
                    log.info("{} critically casts {} on {}", actor.getName(), getName(), target.getName());
                    actor.addCondition(new SpellFocusCondition(
                            11,
                            RollModifier.INTELLIGENCE,
                            spellCheckAdvantage,
                            spellCheckBonus,
                            new RemoveDazedAndConfusedCondition(target)
                    ));
                    target.addCondition(new DazedAndConfusedCondition()); // Until focus lost or attacked
                } else if (spellCheckRoll + spellCheckModifier + spellCheckBonus >= difficultyClass) {
                    log.info("{} casts {} on {}", actor.getName(), getName(), target.getName());
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
                    log.info("{} MISSES the spell check with a {}", actor.getName(), getName());
                }
            } else {
                log.info("{} casts {} on {} with no effect!", actor.getName(), getName(), target.getName());
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
            log.info("{} is no longer dazed and confused.", creature.getName());
            creature.removeCondition(DazedAndConfusedCondition.class.getName());
        }
    }
}
