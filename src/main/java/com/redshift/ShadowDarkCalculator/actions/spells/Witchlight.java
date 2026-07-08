package com.redshift.ShadowDarkCalculator.actions.spells;

import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.dice.RollModifier;
import com.redshift.ShadowDarkCalculator.encounter.Encounter;

import java.util.List;

/**
 * Tier 1, witch
 * Duration: Focus
 * Range: Near
 *
 * You summon a floating marsh light that bobs in the air and casts light out to
 * a close radius around it. The light can change colors and take on vague shapes.
 * It can float up to a near distance on your turn.
 */

public class Witchlight extends Spell {

    public Witchlight() {
        super("Witchlight", 11, RollModifier.CHARISMA);
        lost = true; // This spell just takes up space.
    }

    @Override
    public List<Creature> getTargets(Creature actor, List<Creature> enemies, List<Creature> allies) {
        throw new IllegalStateException("Spell should not be cast.");
    }

    @Override
    public void performCriticalSpell(Creature actor, List<Creature> targets, Encounter encounter, int spellCheckRoll) {
        throw new IllegalStateException("Spell should not be cast.");
    }

    @Override
    public void performSpell(Creature actor, List<Creature> targets, Encounter encounter, int spellCheckRoll) {
        throw new IllegalStateException("Spell should not be cast.");
    }
}
