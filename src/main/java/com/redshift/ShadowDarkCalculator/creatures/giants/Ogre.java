package com.redshift.ShadowDarkCalculator.creatures.giants;

import com.redshift.ShadowDarkCalculator.actions.PerformAllAction;
import com.redshift.ShadowDarkCalculator.actions.weapons.Weapon;
import com.redshift.ShadowDarkCalculator.creatures.Label;
import com.redshift.ShadowDarkCalculator.creatures.Monster;
import com.redshift.ShadowDarkCalculator.creatures.Stats;
import com.redshift.ShadowDarkCalculator.dice.MultipleDice;
import com.redshift.ShadowDarkCalculator.dice.RollModifier;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D6;
import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D8;

public class Ogre extends Monster {

    public Ogre(String name) {
        super(
                name,
                6,
                new Stats(18,9,17,7,7,7),
                9,
                D8.roll() + D8.roll() + D8.roll() + D8.roll() + D8.roll() + D8.roll() + 3,
                new PerformAllAction(
                        new Weapon("Great Club", new MultipleDice(D6, D6), RollModifier.STRENGTH, 2),
                        new Weapon("Great Club", new MultipleDice(D6, D6), RollModifier.STRENGTH, 2)
                )
        );
        getLabels().add(Label.BRUTE);
    }

}
