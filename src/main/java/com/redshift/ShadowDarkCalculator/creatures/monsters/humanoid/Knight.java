package com.redshift.ShadowDarkCalculator.creatures.monsters.humanoid;

import com.redshift.ShadowDarkCalculator.actions.PerformAllActions;
import com.redshift.ShadowDarkCalculator.actions.weapons.WeaponBuilder;
import com.redshift.ShadowDarkCalculator.creatures.CreatureLabel;
import com.redshift.ShadowDarkCalculator.creatures.Stats;
import com.redshift.ShadowDarkCalculator.creatures.monsters.Monster;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D8;

/**
 * A warrior in shining plate mail and the surcoat of a knightly order.
 * AC 17 (plate mail + shield), HP 14, ATK 2 bastard sword +3 (1d8), MV near
 * S +3, D +0, C +1, I +0, W +0, Ch +1, AL L, LV 3
 * Oath. 3/day, ADV on a roll made in service of knight's order.
 */

public class Knight extends Monster {

    public Knight(String name) {
        super(
                name,
                3,
                new Stats(16, 10, 12, 10,10, 12),
                17,
                D8.roll() + D8.roll() + D8.roll() + 1,
                new PerformAllActions(
                        WeaponBuilder.BASTARD_SWORD_1H.build(),
                        WeaponBuilder.BASTARD_SWORD_1H.build()

                )
        );
        getLabels().add(CreatureLabel.FRONT_LINE);
        getLabels().add(CreatureLabel.HUMANOID);
    }
}
