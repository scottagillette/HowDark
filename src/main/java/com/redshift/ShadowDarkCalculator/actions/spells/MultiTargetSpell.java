package com.redshift.ShadowDarkCalculator.actions.spells;

import com.redshift.ShadowDarkCalculator.dice.RollModifier;
import com.redshift.ShadowDarkCalculator.dice.Dice;

/**
 * A spell that can affect multiple targets but has a very specific effect.... sleep, etc... see subclasses.
 */

public abstract class MultiTargetSpell extends Spell {

    protected final Dice totalTargets;

    public MultiTargetSpell(String name, int difficultyClass, RollModifier rollModifier, Dice totalTargets) {
        super(name, difficultyClass, rollModifier);
        this.totalTargets = totalTargets;
    }

}
