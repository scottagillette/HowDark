package com.redshift.ShadowDarkCalculator.creatures.monsters.beasts;

import com.redshift.ShadowDarkCalculator.actions.PerformAllActions;
import com.redshift.ShadowDarkCalculator.actions.weapons.Weapon;
import com.redshift.ShadowDarkCalculator.creatures.Stats;
import com.redshift.ShadowDarkCalculator.creatures.monsters.Monster;
import com.redshift.ShadowDarkCalculator.dice.RollModifier;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D6;
import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D8;

/**
 * An enormous, mottled serpent that can swallow a cow whole.
 * AC 12, HP 23, ATK 2 bite +4 (1d6) and 1 constrict (near), MV near (climb)
 * S +3, D +2, C +1, I -2, W +0, Ch -2, AL N, LV 5
 * Constrict. Contested STR to hold target immobile for one round. // TODO: Not implemented
 */

public class GiantSnake extends Monster {

    public GiantSnake(String name) {
        super(
                name,
                5,
                new Stats(16,14,12,6,10,6),
                12,
                D8.roll() + D8.roll() + D8.roll() + D8.roll() + D8.roll() + 1,
                new PerformAllActions(
                        new Weapon("Bite", D6, RollModifier.STRENGTH).addPiercing().addAttackRollBonus(1),
                        new Weapon("Bite", D6, RollModifier.STRENGTH).addPiercing().addAttackRollBonus(1)
                )
        );

    }
}
