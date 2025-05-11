package com.redshift.ShadowDarkCalculator.actions.spells;

import com.redshift.ShadowDarkCalculator.dice.RollModifier;

public abstract class SingleTargetSpell extends Spell {

    public SingleTargetSpell(String name, int difficultyClass, RollModifier rollModifier) {
        super(name, difficultyClass, rollModifier);
    }

    public SingleTargetSpell(String name, int difficultyClass, RollModifier rollModifier, int priority) {
        super(name, difficultyClass, rollModifier, priority);
    }

}
