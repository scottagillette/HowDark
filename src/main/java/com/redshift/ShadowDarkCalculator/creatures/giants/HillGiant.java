package com.redshift.ShadowDarkCalculator.creatures.giants;

import com.redshift.ShadowDarkCalculator.actions.PerformAllActions;
import com.redshift.ShadowDarkCalculator.actions.PerformOneAction;
import com.redshift.ShadowDarkCalculator.actions.weapons.Weapon;
import com.redshift.ShadowDarkCalculator.creatures.Label;
import com.redshift.ShadowDarkCalculator.creatures.Monster;
import com.redshift.ShadowDarkCalculator.creatures.Stats;
import com.redshift.ShadowDarkCalculator.dice.MultipleDice;
import com.redshift.ShadowDarkCalculator.dice.RollModifier;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.*;

public class HillGiant extends Monster {

    public HillGiant(String name) {
        super(
                name,
                7,
                new Stats(18, 10, 17, 7, 7, 7),
                11,
                D8.roll() + D8.roll() + D8.roll() + D8.roll() + D8.roll() + D8.roll() + D8.roll() + 3,
                new PerformOneAction(
                        new PerformAllActions(
                                new Weapon("Great Club", new MultipleDice(D8, D8), RollModifier.STRENGTH, 2),
                                new Weapon("Great Club", new MultipleDice(D8, D8), RollModifier.STRENGTH, 2)
                        ),
                        new Weapon("Boulder", D10, RollModifier.STRENGTH, 2)
                )
        );
        getLabels().add(Label.BRUTE);
    }

}
