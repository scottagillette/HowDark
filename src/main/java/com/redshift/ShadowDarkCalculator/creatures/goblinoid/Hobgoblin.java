package com.redshift.ShadowDarkCalculator.creatures.goblinoid;

import com.redshift.ShadowDarkCalculator.actions.weapons.WeaponBuilder;
import com.redshift.ShadowDarkCalculator.creatures.Label;
import com.redshift.ShadowDarkCalculator.creatures.Monster;
import com.redshift.ShadowDarkCalculator.creatures.Stats;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D8;

public class Hobgoblin extends Monster {

    public Hobgoblin() {
        super(
                "Hobgoblin",
                2,
                new Stats(16,10,12,14,12,12),
                15,
                D8.roll() + D8.roll() + 1,
                WeaponBuilder.LONGSWORD.build()
                // Skip longbow
        );
        getLabels().add(Label.FRONT_LINE);
    }
}
