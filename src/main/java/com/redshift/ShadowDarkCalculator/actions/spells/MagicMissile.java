package com.redshift.ShadowDarkCalculator.actions.spells;

import com.redshift.ShadowDarkCalculator.dice.RollModifier;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D4;

/**
 * Tier 1, wizard
 * Duration: Instant
 * Range: Far
 *
 * You have advantage on your check to cast this spell. A glowing bolt of force streaks
 * from your open hand, dealing 1d4 damage to one target.
 */

public class MagicMissile extends SingleTargetDamageSpell {

    public MagicMissile() {
        super("Magic Missile", 11, RollModifier.INTELLIGENCE, D4, true);
    }

}
