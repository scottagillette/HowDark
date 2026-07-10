package com.redshift.ShadowDarkCalculator.creatures.monsters.animated;

import com.redshift.ShadowDarkCalculator.actions.PerformAllActions;
import com.redshift.ShadowDarkCalculator.actions.weapons.WeaponBuilder;
import com.redshift.ShadowDarkCalculator.creatures.CreatureLabel;
import com.redshift.ShadowDarkCalculator.creatures.Stats;
import com.redshift.ShadowDarkCalculator.creatures.monsters.Monster;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D8;

/**
 * An old suit of armor magically animated by a vengeful spirit.
 * AC 15, HP 11, ATK 1 longsword +3 (1d8), MV near
 * S +3, D -1, C +2, I -1, W +1, Ch +0, AL C, LV 2
 * TODO: Statue. When standing still, looks exactly like a suit of armor.
 */

public class AnimatedArmor  extends Monster {

    public AnimatedArmor(String name) {
        super(
                name,
                2,
                new Stats(16, 8, 14, 8,12, 10),
                15,
                D8.roll() + D8.roll() + 2,
                WeaponBuilder.LONGSWORD.build()
        );
        getLabels().add(CreatureLabel.FRONT_LINE);
        getLabels().add(CreatureLabel.CHAOTIC);

        setWillFlee(false); // No in rules but seems like they would not flee... what morale!
    }

}
