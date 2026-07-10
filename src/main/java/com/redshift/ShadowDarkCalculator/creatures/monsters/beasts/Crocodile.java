package com.redshift.ShadowDarkCalculator.creatures.monsters.beasts;

import com.redshift.ShadowDarkCalculator.actions.PerformAllActions;
import com.redshift.ShadowDarkCalculator.actions.weapons.Weapon;
import com.redshift.ShadowDarkCalculator.creatures.CreatureLabel;
import com.redshift.ShadowDarkCalculator.creatures.Stats;
import com.redshift.ShadowDarkCalculator.creatures.monsters.Monster;
import com.redshift.ShadowDarkCalculator.dice.RollModifier;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.*;

/**
 * Fat, scaly reptiles with stumpy legs and long, thrashing tails.
 * AC 14, HP 20, ATK 2 bite +3 (1d8), MV near (swim)
 * S +3, D +1, C +2, I -2, W +1, Ch -2, AL N, LV 4
 */

public class Crocodile extends Monster {

    public Crocodile(String name) {
        super(
                name,
                4,
                new Stats(16,12,14,6,12,6),
                14,
                D8.roll() + D8.roll() + D8.roll() + D8.roll() + 2,
                new PerformAllActions(
                    new Weapon("Bite", D8, RollModifier.STRENGTH).addPiercing(),
                    new Weapon("Bite", D8, RollModifier.STRENGTH).addPiercing()
                )
        );
        getLabels().add(CreatureLabel.FRONT_LINE);
        getLabels().add(CreatureLabel.NEUTRAL);
    }

}
