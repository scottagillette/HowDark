package com.redshift.ShadowDarkCalculator.creatures;

import com.redshift.ShadowDarkCalculator.actions.PerformOneAction;
import com.redshift.ShadowDarkCalculator.actions.weapons.WeaponBuilder;
import com.redshift.ShadowDarkCalculator.targets.RandomTargetSelector;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D8;

public class Skeleton extends BaseCreature {

    public Skeleton(String name) {
        super(
                name,
                2,
                true,
                true,
                new Stats(12,10,14,9,10,9),
                13,
                D8.roll() + D8.roll() + 2,
                new PerformOneAction(WeaponBuilder.SHORTSWORD.build(), WeaponBuilder.SHORTBOW.build()),
                new RandomTargetSelector()
        );
    }
}
