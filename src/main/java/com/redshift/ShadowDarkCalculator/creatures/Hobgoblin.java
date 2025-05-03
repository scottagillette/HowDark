package com.redshift.ShadowDarkCalculator.creatures;

import com.redshift.ShadowDarkCalculator.actions.PerformOneAction;
import com.redshift.ShadowDarkCalculator.actions.weapons.SimpleWeapon;

import java.util.List;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D8;

public class Hobgoblin extends BaseCreature {

    public Hobgoblin() {
        super(
                "Hobgoblin",
                2,
                new Stats(16,10,12,14,12,12),
                15,
                D8.roll() + D8.roll() + 1,
                SimpleWeapon.LONGSWORD.build()
                // Skip longbow
        );
    }
}
