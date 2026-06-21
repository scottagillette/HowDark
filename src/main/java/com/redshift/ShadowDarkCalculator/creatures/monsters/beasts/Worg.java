package com.redshift.ShadowDarkCalculator.creatures.monsters.beasts;

import com.redshift.ShadowDarkCalculator.actions.weapons.Weapon;
import com.redshift.ShadowDarkCalculator.creatures.CreatureLabel;
import com.redshift.ShadowDarkCalculator.creatures.Stats;
import com.redshift.ShadowDarkCalculator.creatures.monsters.Monster;
import com.redshift.ShadowDarkCalculator.dice.RollModifier;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D6;
import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D8;

/**
 * Bat-faced wolves that speak Goblin and often serve as war mounts for goblinkind.
 * AC 11, HP 14, ATK 1 bite +3 (1d6), MV double near
 * S +2, D +1, C +1, I -2, W +1, Ch -2, AL C, LV 3
 */

public class Worg extends Monster {

    public Worg(String name) {
        super(
                name,
                3,
                new Stats(14,12,12,6,12,6),
                11,
                D8.roll() + D8.roll() + D8.roll() + 1,
                new Weapon("Bite", D6, RollModifier.STRENGTH).addPiercing().addAttackRollBonus(1)
        );
        getLabels().add(CreatureLabel.CHAOTIC);
        getLabels().add(CreatureLabel.FRONT_LINE);
    }

}
