package com.redshift.ShadowDarkCalculator.creatures.humanoid;

import com.redshift.ShadowDarkCalculator.actions.PerformOneAction;
import com.redshift.ShadowDarkCalculator.actions.weapons.WeaponBuilder;
import com.redshift.ShadowDarkCalculator.creatures.CreatureLabel;
import com.redshift.ShadowDarkCalculator.creatures.Monster;
import com.redshift.ShadowDarkCalculator.creatures.Stats;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D8;

public class Bittermold extends Monster {

    public Bittermold(String name) {
        super(
                name,
                1,
                new Stats(12,14,12,10,10,10),
                12,
                D8.roll() + 1,
                new PerformOneAction(WeaponBuilder.SHORTSWORD.build())
        );
        getLabels().add(CreatureLabel.FRONT_LINE);
    }

}
