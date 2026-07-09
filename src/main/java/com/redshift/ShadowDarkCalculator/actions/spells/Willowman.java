package com.redshift.ShadowDarkCalculator.actions.spells;

import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.dice.RollModifier;
import com.redshift.ShadowDarkCalculator.encounter.Encounter;
import com.redshift.ShadowDarkCalculator.targets.multi.AliveAwakeTargetSelector;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

import static com.redshift.ShadowDarkCalculator.dice.DifficultyClass.HARD;

/**
 * Tier 1, witch
 * Duration: Instant
 * Range: Near
 *
 * You call upon the Willowman to appear in one creature's mind, filling it with
 * supernatural terror. Choose one creature of LV 2 or less within range. That
 * creature must immediately make a morale check. Even creatures that are not
 * normally subject to morale checks (such as undead) must do so.
 */

@Slf4j
public class Willowman extends Spell {

    public Willowman() {
        super("Willowman", 11, RollModifier.CHARISMA);
    }

    @Override
    public boolean canPerform(Creature actor, List<Creature> enemies, List<Creature> allies) {
        final boolean canPerform = super.canPerform(actor, enemies, allies);
        final boolean hasTarget = !new AliveAwakeTargetSelector().getTargets(enemies, 1).isEmpty();
        return (canPerform && hasTarget);
    }

    @Override
    public List<Creature> getTargets(Creature actor, List<Creature> enemies, List<Creature> allies) {
        final List<Creature> targets = new ArrayList<>();
        final Creature target = new AliveAwakeTargetSelector().getTargets(enemies, 1).getFirst();
        if (target != null) targets.add(target);
        return targets;
    }

    @Override
    public void performCriticalSpell(Creature actor, List<Creature> targets, Encounter encounter, int spellCheckRoll) {
        final Creature target = targets.getFirst(); // Always uses single target.
        if (target.getLevel() <= 4) { // Double one numeric value!
            if (target.getStats().wisdomSave(HARD.getDc())) {
                log.info("{} casts {} but {} succeds on a morale check.", actor.getName(), name, target.getName());
            } else {
                log.info("{} critically succeeds on the spell {} and {} fails a morale check!", actor.getName(), name, target.getName());
                target.flee();
            }
        } else {
            log.info("{} casts {} but doesn't affect the creature.", actor.getName(), name);
            lost = true; // Doesn't affect the creature... stop casting Willowman!
        }
    }

    @Override
    public void performSpell(Creature actor, List<Creature> targets, Encounter encounter, int spellCheckRoll) {
        final Creature target = targets.getFirst(); // Always uses single target.
        if (target.getLevel() <= 2) {
            if (target.getStats().wisdomSave(HARD.getDc())) {
                log.info("{} casts {} but {} succeds on a morale check.", actor.getName(), name, target.getName());
            } else {
                log.info("{} succeeds on the spell {} and {} fails a morale check!", actor.getName(), name, target.getName());
                target.flee();
            }
        } else {
            log.info("{} casts {} but doesn't affect the creature.", actor.getName(), name);
            lost = true; // Doesn't affect the creature... stop casting Willowman!
        }
    }
}
