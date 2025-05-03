package com.redshift.ShadowDarkCalculator.actions.spells;

import com.redshift.ShadowDarkCalculator.dice.RollModifier;

/**
 * A spell that can affect a single targets but has a beneficial effect.... see subclasses.
 */

public abstract class SingleTargetBenificialSpell extends Spell {

    public SingleTargetBenificialSpell(String name, int difficultyClass, RollModifier rollModifier) {
        super(name, difficultyClass, rollModifier);
    }

}
