package com.redshift.ShadowDarkCalculator.actions.spells;

import com.redshift.ShadowDarkCalculator.conditions.DisadvantagedCondition;
import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.dice.RollModifier;
import com.redshift.ShadowDarkCalculator.dice.RollOutcome;
import com.redshift.ShadowDarkCalculator.encounter.Encounter;
import com.redshift.ShadowDarkCalculator.targets.LivingTargetSelector;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D4;

/**
 * You fling a dark rune of necrotic energy at a target in range. The target takes 1d4 damage.
 * This damage increases to 2d4 when you reach 5th level. Undead creatures are unharmed by this spell.
 */

@Slf4j
public class Withermark extends Spell {

    public Withermark() {
        super("Withermark", 11, RollModifier.CHARISMA);
    }

    @Override
    public boolean canPerform(Creature actor, List<Creature> enemies, List<Creature> allies) {
        return (!lost && !new LivingTargetSelector().getTargets(enemies, enemies.size()).isEmpty());
    }

    @Override
    public void perform(Creature actor, List<Creature> enemies, List<Creature> allies, Encounter encounter) {
        final List<Creature> livingCreatures = new LivingTargetSelector().getTargets(enemies, enemies.size());

        final Creature target = actor.getSingleTargetSelector().get(livingCreatures);

        boolean disadvantage = actor.hasCondition(DisadvantagedCondition.class.getName());
        actor.removeCondition(DisadvantagedCondition.class.getName());

        final int spellCheckRoll = getSpellCheckRoll(disadvantage);

        final boolean criticalSuccess = spellCheckRoll == RollOutcome.CRITICAL_SUCCESS;
        final boolean criticalFailure = spellCheckRoll == RollOutcome.CRITICAL_FAILURE;

        int spellCheckModifier = 0;

        if (rollModifier.equals(RollModifier.INTELLIGENCE)) {
            spellCheckModifier = actor.getStats().getIntelligenceModifier();
        } else if (rollModifier.equals(RollModifier.WISDOM)) {
            spellCheckModifier = actor.getStats().getWisdomModifier();
        } else if (rollModifier.equals(RollModifier.CHARISMA)) {
            spellCheckModifier = actor.getStats().getCharismaModifier();
        }

        if (criticalFailure) {
            lost = true; // Failed spell check!
            log.info("{} critically MISSES the spell check with a {}", actor.getName(), getName());
        } else if (criticalSuccess) {
            int damage = D4.roll() + D4.roll();
            log.info("{} critically hits a spell on {} with a {} for {} damage", actor.getName(), target.getName(), getName(), damage);
            target.takeDamage(damage, false, true, false, false);
        } else if (spellCheckRoll + spellCheckModifier + spellCheckBonus >= difficultyClass) {
            int damage = D4.roll();
            log.info("{} hits a spell on {} with a {} for {} damage", actor.getName(), target.getName(), getName(), damage);
            target.takeDamage(damage, false, true, false, false);
        } else {
            lost = true; // Failed spell check!
            log.info("{} MISSES the spell check with a {}", actor.getName(), getName());
        }
    }
}
