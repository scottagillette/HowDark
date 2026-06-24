package com.redshift.ShadowDarkCalculator.creatures.monsters.monstrosities;

import com.redshift.ShadowDarkCalculator.actions.PerformAllActions;
import com.redshift.ShadowDarkCalculator.actions.weapons.Weapon;
import com.redshift.ShadowDarkCalculator.creatures.CreatureLabel;
import com.redshift.ShadowDarkCalculator.creatures.Stats;
import com.redshift.ShadowDarkCalculator.creatures.monsters.Monster;
import com.redshift.ShadowDarkCalculator.dice.MultipleDice;
import com.redshift.ShadowDarkCalculator.dice.RollModifier;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.*;

/**
 * A hulking, shark-sized lizard with a steely, arrow-shaped carapace and a massive gullet.
 * AC 17, HP 40, ATK 3 bite +5 (2d6) or 1 leap, MV near (burrow)
 * S +5, D +1, C +4, I -3, W +1, Ch -2, AL N, LV 8
 * TODO: Leap. Jump up to near in height and double near in distance, then make 2 bite attacks.
 */

public class Bulette extends Monster {

    public Bulette(String name) {
        super(
                name,
                8,
                new Stats(20,12,18,4,12,6),
                17,
                D8.roll() + D8.roll() + D8.roll() + D8.roll() +
                        D8.roll() + D8.roll() + D8.roll() + D8.roll() + 4,
                new PerformAllActions(
                        new Weapon("Bite", new MultipleDice(D6, D6), RollModifier.STRENGTH).addPiercing(),
                        new Weapon("Bite", new MultipleDice(D6, D6), RollModifier.STRENGTH).addPiercing(),
                        new Weapon("Bite", new MultipleDice(D6, D6), RollModifier.STRENGTH).addPiercing()
                )
        );
        getLabels().add(CreatureLabel.FRONT_LINE);
        getLabels().add(CreatureLabel.NEUTRAL);
    }

}
