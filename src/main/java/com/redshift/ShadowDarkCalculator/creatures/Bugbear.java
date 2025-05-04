package com.redshift.ShadowDarkCalculator.creatures;

import com.redshift.ShadowDarkCalculator.actions.PerformAllAction;
import com.redshift.ShadowDarkCalculator.actions.weapons.WeaponBuilder;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D8;

public class Bugbear extends BaseCreature {

    public Bugbear(String name) {
        super(
                name,
                3,
                new Stats(17, 10, 12, 9, 10, 7),
                13,
                D8.roll() + D8.roll() + 1,
                new PerformAllAction(WeaponBuilder.MACE.build(), WeaponBuilder.MACE.build())
        );
    }
}
