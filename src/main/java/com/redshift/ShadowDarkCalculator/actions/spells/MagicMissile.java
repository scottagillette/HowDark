package com.redshift.ShadowDarkCalculator.actions.spells;

import com.redshift.ShadowDarkCalculator.dice.RollModifier;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D4;

public class MagicMissile extends SingleTargetDamageSpell {

    public MagicMissile() {
        super("Magic Missile", 11, RollModifier.INTELLIGENCE, D4, true);
    }

}
