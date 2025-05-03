package com.redshift.ShadowDarkCalculator.creatures;

import com.redshift.ShadowDarkCalculator.actions.PerformOneAction;
import com.redshift.ShadowDarkCalculator.actions.weapons.SimpleWeapon;

import java.util.List;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D8;

public class Goblin extends BaseCreature {

    public Goblin(String name) {
        super(
                name,
                1,
                new Stats(10,12,12,9,9,7),
                12,
                D8.roll() + 1,
                new PerformOneAction(SimpleWeapon.DAGGER_DEX.build(), SimpleWeapon.SHORTBOW.build())
        );
    }

}
