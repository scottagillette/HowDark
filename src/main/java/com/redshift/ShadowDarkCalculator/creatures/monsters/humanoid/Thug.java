package com.redshift.ShadowDarkCalculator.creatures.monsters.humanoid;

import com.redshift.ShadowDarkCalculator.actions.PerformOneAction;
import com.redshift.ShadowDarkCalculator.actions.weapons.WeaponBuilder;
import com.redshift.ShadowDarkCalculator.creatures.CreatureLabel;
import com.redshift.ShadowDarkCalculator.creatures.Stats;
import com.redshift.ShadowDarkCalculator.creatures.monsters.Monster;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D8;

/**
 * A bruised and boorish ruffian.
 * AC 13 (leather + shield), HP 4, ATK 1 shortsword +1 (1d6), MV near,
 * S +1, D +0, C +0, I -1, W +1, Ch -1, AL C, LV 1
 */

public class Thug extends Monster {

    public Thug(String name) {
        super(
                name,
                1,
                new Stats(12, 10, 10, 8,12, 8),
                13,
                D8.roll(),
                WeaponBuilder.SHORT_SWORD.build()
        );
        getLabels().add(CreatureLabel.FRONT_LINE);
        getLabels().add(CreatureLabel.HUMANOID);
        getLabels().add(CreatureLabel.CHAOTIC);
    }

}