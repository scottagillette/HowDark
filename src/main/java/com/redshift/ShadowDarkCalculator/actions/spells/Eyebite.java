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
    public void perform(Creature actor, List<Creature> enemies, List<Creature> allies, Encounter encounter) {
        final Creature target = actor.getSingleTargetSelector().get(enemies);

        if (target == null) {
            log.info("{} is skipping their turn... no target!", actor.getName());
        } else {
            final boolean spellHits = performSingleTargetSpellAttack(actor, target, this, difficultyClass, damageDice, rollModifier);

            if (spellHits) {
                log.info("{} is blinded from {}", target.getName(), name);
                target.addCondition(new BlindedCondition(1)); //TODO: Blinded for just casting creature
            }
        }
    }
}
