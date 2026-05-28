package com.redshift.ShadowDarkCalculator.actions.spells;

import com.redshift.ShadowDarkCalculator.dice.Dice;
import com.redshift.ShadowDarkCalculator.dice.MultipleDice;
import com.redshift.ShadowDarkCalculator.dice.RollModifier;
import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.encounter.Encounter;
import com.redshift.ShadowDarkCalculator.targets.single.HealTargetSelector;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D6;

/**
 * Tier 1, priest
 * Duration: Instant
 * Range: Close
 *
 * Your touch restores ebbing life. Roll a number of d6s equal to 1 + half your
 * level (rounded down). One target you touch regains that many hit points.
 */

@Slf4j
public class CureWounds extends Spell {

    public CureWounds() {
        super("Cure Wounds", 11, RollModifier.WISDOM);
    }

    @Override
    public boolean canPerform(Creature actor, List<Creature> enemies, List<Creature> allies) {
        final boolean canPerform = super.canPerform(actor, enemies, allies);
        final boolean hasTarget = new HealTargetSelector().get(allies) != null;
        return (canPerform && hasTarget);
    }

    @Override
    public List<Creature> getTargets(Creature actor, List<Creature> enemies, List<Creature> allies) {
        final List<Creature> targets = new ArrayList<>();
        final Creature target = new HealTargetSelector().get(allies);
        if (target != null) targets.add(target);
        return targets;
    }

    @Override
    public void performCriticalSpell(Creature actor, List<Creature> targets, Encounter encounter, int spellCheckRoll) {
        final Creature target = targets.getFirst(); // Always uses single target.

        // Healing dice is 1d6 plus 1d6 dice per caster level divided by 2 rounded down.
        final Dice dice = new MultipleDice(D6, actor.getLevel() + (actor.getLevel() / 2));

        int hitPoints = dice.roll() + dice.roll();
        log.info("{} critically heals on {} for {} with a {}", actor.getName(), target.getName(), hitPoints, name);
        target.healDamage(hitPoints);
    }

    @Override
    public void performSpell(Creature actor, List<Creature> targets, Encounter encounter, int spellCheckRoll) {
        final Creature target = targets.getFirst(); // Always uses single target.

        // Healing dice is 1d6 plus 1d6 dice per caster level divided by 2 rounded down.
        final Dice dice = new MultipleDice(D6, actor.getLevel() + (actor.getLevel() / 2));

        int hitPoints = dice.roll();
        log.info("{} heals on {} for {} with a {}", actor.getName(), target.getName(), hitPoints, name);
        target.healDamage(hitPoints);
    }

}
