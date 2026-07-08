package com.redshift.ShadowDarkCalculator.actions.spells;

import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.dice.RollModifier;
import com.redshift.ShadowDarkCalculator.encounter.Encounter;

import java.util.List;

/**
 * Tier 1, priest, wizard
 * Duration: 1 hour real time
 * Range: Close
 *
 * One object you touch glows with bright, heatless light, illuminating out to a near
 * distance for 1 hour of real time.
 */

public class Light extends Spell {

    public Light() {
        super("Light", 11, RollModifier.INTELLIGENCE);
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
