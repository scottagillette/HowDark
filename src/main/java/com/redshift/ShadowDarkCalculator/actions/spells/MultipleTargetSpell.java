package com.redshift.ShadowDarkCalculator.actions.spells;

import com.redshift.ShadowDarkCalculator.dice.Dice;
import com.redshift.ShadowDarkCalculator.dice.RollModifier;

/**
 * A spell that can affect multiple targets but has a custom effect.
 */

public abstract class MultipleTargetSpell extends Spell {

    protected final Dice totalTargets;

    public MultipleTargetSpell(String name, int difficultyClass, RollModifier rollModifier, Dice totalTargets) {
        super(name, difficultyClass, rollModifier);
        this.totalTargets = totalTargets;
    }

}
