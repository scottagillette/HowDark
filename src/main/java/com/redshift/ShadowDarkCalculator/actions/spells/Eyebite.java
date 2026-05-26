package com.redshift.ShadowDarkCalculator.actions.spells;

import com.redshift.ShadowDarkCalculator.conditions.BlindedCondition;
import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.dice.RollModifier;
import com.redshift.ShadowDarkCalculator.encounter.Encounter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D4;

/**
 * Tier 1, witch
 * Duration: Instant
 * Range: Near
 *
 * One creature you target takes 1d4 damage, and it can't see you until the end of its next turn.
 */

@Slf4j
public class Eyebite extends SingleTargetDamageSpell {

    public Eyebite() {
        super("Eyebite", 11, RollModifier.CHARISMA, D4, false);
    }

    @Override
    public void performCriticalSpell(Creature actor, List<Creature> targets, Encounter encounter, int spellCheckRoll) {
        super.performCriticalSpell(actor, targets, encounter, spellCheckRoll);
        final Creature target = targets.getFirst();
        log.info("{} is blinded from {}", target.getName(), name);
        target.addCondition(new BlindedCondition(1)); //TODO: Blinded for just casting creature
    }

    @Override
    public void performSpell(Creature actor, List<Creature> targets, Encounter encounter, int spellCheckRoll) {
        super.performSpell(actor, targets, encounter, spellCheckRoll);
        final Creature target = targets.getFirst();
        log.info("{} is blinded from {}", target.getName(), name);
        target.addCondition(new BlindedCondition(1)); //TODO: Blinded for just casting creature
    }

}
