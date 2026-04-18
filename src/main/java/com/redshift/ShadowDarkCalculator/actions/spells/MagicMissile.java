package com.redshift.ShadowDarkCalculator.actions.spells;

import com.redshift.ShadowDarkCalculator.dice.RollModifier;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D4;

/**
 * Magic missile D4 damage; and always has Advantage for the spell check.
 */

public class MagicMissile extends SingleTargetDamageSpell {

    public MagicMissile() {
        super("Magic Missile", 11, RollModifier.INTELLIGENCE, D4, true);
    }

}
