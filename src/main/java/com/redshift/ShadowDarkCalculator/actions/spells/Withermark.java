package com.redshift.ShadowDarkCalculator.actions.spells;

import com.redshift.ShadowDarkCalculator.dice.RollModifier;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D4;

/**
 * You fling a dark rune of necrotic energy at a target in range. The target takes 1d4 damage.
 * This damage increases to 2d4 when you reach 5th level. Undead creatures are unharmed by this spell.
 */

public class Withermark extends SingleTargetDamageSpell {

    public Withermark() {
        super("Withermark", 11, RollModifier.CHARISMA, D4, false);
    }

}
