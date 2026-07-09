package com.redshift.ShadowDarkCalculator.creatures.monsters.beasts;

import com.redshift.ShadowDarkCalculator.actions.PerformAllActions;
import com.redshift.ShadowDarkCalculator.actions.weapons.Weapon;
import com.redshift.ShadowDarkCalculator.creatures.CreatureLabel;
import com.redshift.ShadowDarkCalculator.creatures.Stats;
import com.redshift.ShadowDarkCalculator.creatures.monsters.Monster;
import com.redshift.ShadowDarkCalculator.dice.RollModifier;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D8;

/**
 * A massive wolf with spines of black bone along its brow ridge and back.
 * AC 12, HP 19, ATK 2 bite +4 (1d8), MV double near
 * S +3, D +2, C +1, I -1, W +1, Ch +0, AL N, LV 4
 * TODO: Pack Hunter. Deals +1 damage while an ally is close.
 */

public class DireWolf extends Monster {

    public DireWolf(String name) {
        super(
                name,
                4,
                new Stats(16, 14, 12, 8, 12, 10),
                12,
                D8.roll() + D8.roll() + D8.roll() + D8.roll() + 1,
                new PerformAllActions(
                        new Weapon("Bite", D8, RollModifier.STRENGTH).addPiercing(),
                        new Weapon("Bite", D8, RollModifier.STRENGTH).addPiercing()
                )
        );
        getLabels().add(CreatureLabel.NEUTRAL);
    }

}
