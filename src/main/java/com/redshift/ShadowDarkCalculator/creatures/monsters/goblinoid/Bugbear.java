package com.redshift.ShadowDarkCalculator.creatures.monsters.goblinoid;

import com.redshift.ShadowDarkCalculator.actions.PerformAllActions;
import com.redshift.ShadowDarkCalculator.actions.weapons.WeaponBuilder;
import com.redshift.ShadowDarkCalculator.creatures.CreatureLabel;
import com.redshift.ShadowDarkCalculator.creatures.monsters.Monster;
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
                new PerformAllActions(WeaponBuilder.MACE.build(), WeaponBuilder.MACE.build())
        );
        getLabels().add(CreatureLabel.FRONT_LINE);
        getLabels().add(CreatureLabel.HUMANOID);
    }
}
