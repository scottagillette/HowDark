package com.redshift.ShadowDarkCalculator.actions.spells;

import com.redshift.ShadowDarkCalculator.actions.DamageType;
import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.dice.RollModifier;
import com.redshift.ShadowDarkCalculator.dice.RollOutcome;
import com.redshift.ShadowDarkCalculator.encounter.Encounter;
import com.redshift.ShadowDarkCalculator.targets.multi.AliveAwakeNotUndeadTargetSelector;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D4;

/**
 * Tier 1, necromancer
 * Duration: Instant
 * Range: Far
 *
 * You fling a dark rune of necrotic energy at a target in range. The target takes 1d4 damage.
 * This damage increases to 2d4 when you reach 5th level. Undead creatures are unharmed by
 * this spell.
 */

@Slf4j
public class Withermark extends Spell {

    public Withermark() {
        super("Withermark", 11, RollModifier.CHARISMA);
    }

    @Override
    public boolean canPerform(Creature actor, List<Creature> enemies, List<Creature> allies) {
        final boolean canPerform = super.canPerform(actor, enemies, allies);
        final boolean hasTarget = !new AliveAwakeNotUndeadTargetSelector().getTargets(enemies, enemies.size()).isEmpty();
        return (canPerform && hasTarget);
    }

    @Override
    public void perform(Creature actor, List<Creature> enemies, List<Creature> allies, Encounter encounter) {
        final List<Creature> livingCreatures = new AliveAwakeNotUndeadTargetSelector().getTargets(enemies, enemies.size());
        final Creature target = actor.getSingleTargetSelector().get(livingCreatures);

        int spellCheckModifier = 0;

        if (rollModifier.equals(RollModifier.INTELLIGENCE)) {
            spellCheckModifier = actor.getStats().getIntelligenceModifier();
        } else if (rollModifier.equals(RollModifier.WISDOM)) {
            spellCheckModifier = actor.getStats().getWisdomModifier();
        } else if (rollModifier.equals(RollModifier.CHARISMA)) {
            spellCheckModifier = actor.getStats().getCharismaModifier();
        }

        final int d20Roll = getSpellCheckRoll(actor, List.of(target), spellCheckModifier);

        final boolean criticalSuccess = d20Roll == RollOutcome.CRITICAL_SUCCESS;
        final boolean criticalFailure = d20Roll == RollOutcome.CRITICAL_FAILURE;

        if (criticalFailure) {
            lost = true; // Failed spell check!
            log.info("{} critically MISSES the spell check with a {}", actor.getName(), name);
        } else if (criticalSuccess) {
            int damage = D4.roll() + D4.roll();
            log.info("{} critically hits a spell on {} with a {} for {} damage", actor.getName(), target.getName(), name, damage);
            target.takeDamage(damage, new DamageType().addMagical());
        } else if (d20Roll + spellCheckModifier + spellCheckBonus >= difficultyClass) {
            int damage = D4.roll();
            log.info("{} hits a spell on {} with a {} for {} damage", actor.getName(), target.getName(), name, damage);
            target.takeDamage(damage, new DamageType().addMagical());
        } else {
            lost = true; // Failed spell check!
            log.info("{} MISSES the spell check with a {}", actor.getName(), name);
        }
    }
}
