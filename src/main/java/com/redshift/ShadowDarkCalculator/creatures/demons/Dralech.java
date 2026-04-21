package com.redshift.ShadowDarkCalculator.creatures.demons;

import com.redshift.ShadowDarkCalculator.actions.PerformAllActions;
import com.redshift.ShadowDarkCalculator.actions.weapons.Weapon;
import com.redshift.ShadowDarkCalculator.creatures.Label;
import com.redshift.ShadowDarkCalculator.creatures.Monster;
import com.redshift.ShadowDarkCalculator.creatures.Stats;
import com.redshift.ShadowDarkCalculator.dice.MultipleDice;
import com.redshift.ShadowDarkCalculator.dice.RollModifier;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D6;
import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D8;

public class Dralech extends Monster {

    public Dralech(String name) {
        super(
                name,
                6,
                new Stats(18,12,14,10,10,12),
                13,
                D8.roll() + D8.roll() + D8.roll() + D8.roll() + D8.roll() + D8.roll(),
                new PerformAllActions(
                        new Weapon("Bone Axe", new MultipleDice(D6, D6), RollModifier.STRENGTH).addAttackRollBonus(1),
                        new Weapon("Bone Axe", new MultipleDice(D6, D6), RollModifier.STRENGTH).addAttackRollBonus(1),
                        new Weapon("Bone Axe", new MultipleDice(D6, D6), RollModifier.STRENGTH).addAttackRollBonus(1)
                )
        );
        getLabels().add(Label.FRONT_LINE);
    }

}
