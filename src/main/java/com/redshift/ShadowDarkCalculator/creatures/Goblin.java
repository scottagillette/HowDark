package com.redshift.ShadowDarkCalculator.creatures;

import com.redshift.ShadowDarkCalculator.actions.PerformOneAction;
import com.redshift.ShadowDarkCalculator.actions.weapons.WeaponBuilder;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D8;

public class Goblin extends BaseCreature {

    public Goblin(String name) {
        super(
                name,
                1,
                new Stats(10,12,12,9,9,7),
                12,
                D8.roll() + 1,
                new PerformOneAction(WeaponBuilder.DAGGER_DEX.build(), WeaponBuilder.SHORTBOW.build())
        );
    }

}
