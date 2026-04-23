package com.redshift.ShadowDarkCalculator.creatures.goblinoid;

import com.redshift.ShadowDarkCalculator.actions.weapons.WeaponBuilder;
import com.redshift.ShadowDarkCalculator.creatures.CreatureLabel;
import com.redshift.ShadowDarkCalculator.creatures.Monster;
import com.redshift.ShadowDarkCalculator.creatures.Stats;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D8;

public class GoblinBoss extends Monster {

    public GoblinBoss(String name) {
        super(
                name,
                4,
                new Stats(14,12,14,9,10,12),
                14,
                D8.roll() + D8.roll() + D8.roll() + D8.roll() + 2,
                WeaponBuilder.SPEAR_STR.build().addAttackRollBonus(1)
        );
        getLabels().add(CreatureLabel.FRONT_LINE);
        getLabels().add(CreatureLabel.HUMANOID);
    }

}