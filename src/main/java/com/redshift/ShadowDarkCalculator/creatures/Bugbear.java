package com.redshift.ShadowDarkCalculator.creatures;

import com.redshift.ShadowDarkCalculator.actions.PerformAllAction;
import com.redshift.ShadowDarkCalculator.actions.weapons.SimpleWeapon;

import java.util.List;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D8;

public class Bugbear extends BaseCreature {

    public Bugbear(String name) {
        super(
                name,
                3,
                new Stats(17, 10, 12, 9, 10, 7),
                13,
                D8.roll() + D8.roll() + 1,
                new PerformAllAction(SimpleWeapon.MACE.build(), SimpleWeapon.MACE.build())
        );
    }
}
