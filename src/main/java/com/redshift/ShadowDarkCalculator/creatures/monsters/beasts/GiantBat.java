package com.redshift.ShadowDarkCalculator.creatures.monsters.beasts;

import com.redshift.ShadowDarkCalculator.actions.PerformAllActions;
import com.redshift.ShadowDarkCalculator.actions.weapons.Weapon;
import com.redshift.ShadowDarkCalculator.creatures.CreatureLabel;
import com.redshift.ShadowDarkCalculator.creatures.Stats;
import com.redshift.ShadowDarkCalculator.creatures.monsters.Monster;
import com.redshift.ShadowDarkCalculator.dice.RollModifier;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D6;
import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D8;

/**
 * Leathery, eagle-sized mammal with a taste for flesh.
 * AC 12, HP 9, ATK 1 bite +2 (1d6), MV near (fly)
 * S -1, D +2, C +0, I -3, W +1, Ch -3, AL N, LV 2
 */

public class GiantBat extends Monster {

    public GiantBat(String name) {
        super(
                name,
                2,
                new Stats(8,14,10,4,12,4),
                12,
                D8.roll() + D8.roll(),
                new Weapon("Bite", D6, RollModifier.DEXTERITY).addPiercing()
        );
        getLabels().add(CreatureLabel.FRONT_LINE);
        getLabels().add(CreatureLabel.NEUTRAL);
    }

}
