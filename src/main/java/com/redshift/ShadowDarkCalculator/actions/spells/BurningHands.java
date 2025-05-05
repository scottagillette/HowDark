package com.redshift.ShadowDarkCalculator.actions.spells;

import com.redshift.ShadowDarkCalculator.dice.RollModifier;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D4;
import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D6;

public class BurningHands extends MultiTargetDamageSpell {

    public BurningHands() {
        super("Burning Hands", 11, RollModifier.INTELLIGENCE, D6, D4, true, false);
    }

}
