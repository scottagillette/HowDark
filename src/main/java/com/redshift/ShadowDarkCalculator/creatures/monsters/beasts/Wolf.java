package com.redshift.ShadowDarkCalculator.creatures.monsters.beasts;

import com.redshift.ShadowDarkCalculator.actions.weapons.Weapon;
import com.redshift.ShadowDarkCalculator.creatures.Stats;
import com.redshift.ShadowDarkCalculator.creatures.monsters.Monster;
import com.redshift.ShadowDarkCalculator.dice.RollModifier;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D6;
import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D8;

/**
 * A giant canine with a gray pelt, yellow eyes, and dripping jaws.
 * AC 12, HP 10, ATK 1 bite +2 (1d6), MV double near
 * S +2, D +2, C +1, I -2, W +1, Ch +0, AL N, LV 2
 * Pack Hunter. Deals +1 damage while an ally is close. // TODO: Not implemented
 */

public class Wolf extends Monster {

    public Wolf(String name) {
        super(
                name,
                2,
                new Stats(14,114,12,6,12,10),
                12,
                D8.roll() + D8.roll() + 1,
                new Weapon("Bite", D6, RollModifier.STRENGTH).addPiercing()
        );
    }
}
