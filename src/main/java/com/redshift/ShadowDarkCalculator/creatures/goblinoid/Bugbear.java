package com.redshift.ShadowDarkCalculator.creatures.goblinoid;

import com.redshift.ShadowDarkCalculator.actions.PerformAllAction;
import com.redshift.ShadowDarkCalculator.actions.weapons.WeaponBuilder;
import com.redshift.ShadowDarkCalculator.creatures.BaseCreature;
import com.redshift.ShadowDarkCalculator.creatures.Label;
import com.redshift.ShadowDarkCalculator.creatures.Monster;
import com.redshift.ShadowDarkCalculator.creatures.Stats;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D8;

public class Bugbear extends Monster {

    public Bugbear(String name) {
        super(
                name,
                3,
                new Stats(17, 10, 12, 9, 10, 7),
                13,
                D8.roll() + D8.roll() + 1,
                new PerformAllAction(WeaponBuilder.MACE.build(), WeaponBuilder.MACE.build())
        );
        getLabels().add(Label.BRUTE);
    }
}
