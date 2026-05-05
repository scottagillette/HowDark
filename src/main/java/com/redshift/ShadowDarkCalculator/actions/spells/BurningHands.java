package com.redshift.ShadowDarkCalculator.actions.spells;

import com.redshift.ShadowDarkCalculator.dice.RollModifier;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D4;
import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D6;

/**
 * Tier 1, wizard
 * Duration: Instant
 * Range: Close
 *
 * You spread your fingers with thumbs touching, unleashing a circle of flame that
 * roars out to a close area around where you stand. Creatures within the area of
 * effect take 1d6 damage, and flammable objects catch fire.
 */

public class BurningHands extends MultiTargetDamageSpell {

    public BurningHands() {
        super("Burning Hands", 11, RollModifier.INTELLIGENCE, D6, D4);
        damageType.addFire();
    }

}
