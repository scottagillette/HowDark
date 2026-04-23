package com.redshift.ShadowDarkCalculator.creatures.demons;

import com.redshift.ShadowDarkCalculator.actions.PerformAllActions;
import com.redshift.ShadowDarkCalculator.actions.PerformOneAction;
import com.redshift.ShadowDarkCalculator.actions.misc.Terrify;
import com.redshift.ShadowDarkCalculator.actions.misc.WakingFear;
import com.redshift.ShadowDarkCalculator.actions.weapons.Weapon;
import com.redshift.ShadowDarkCalculator.creatures.CreatureLabel;
import com.redshift.ShadowDarkCalculator.creatures.Monster;
import com.redshift.ShadowDarkCalculator.creatures.Stats;
import com.redshift.ShadowDarkCalculator.dice.MultipleDice;
import com.redshift.ShadowDarkCalculator.dice.RollModifier;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D10;
import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D8;

public class TheWillowMan extends Monster {

    public TheWillowMan(String name) {
        super(
                name,
                13,
                new Stats(20,24,16,18,18,20),
                17,
                D8.roll() + D8.roll() + D8.roll() + D8.roll() + D8.roll() +
                        D8.roll() + D8.roll() + D8.roll() + D8.roll() + D8.roll() +
                        D8.roll() + D8.roll() + D8.roll() + 3,
                new PerformOneAction(
                        new PerformAllActions(
                                new Weapon("Finger Needle", new MultipleDice(D10, D10), RollModifier.DEXTERITY).addAttackRollBonus(5),
                                new Weapon("Finger Needle", new MultipleDice(D10, D10), RollModifier.DEXTERITY).addAttackRollBonus(5),
                                new Weapon("Finger Needle", new MultipleDice(D10, D10), RollModifier.DEXTERITY).addAttackRollBonus(5),
                                new Terrify().setPriority(1)
                        ).setPriority(2),
                        new WakingFear().setPriority(1)
                )
        );
        getLabels().add(CreatureLabel.FRONT_LINE);
    }

}
