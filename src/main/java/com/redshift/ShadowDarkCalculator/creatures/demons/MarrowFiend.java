package com.redshift.ShadowDarkCalculator.creatures.demons;

import com.redshift.ShadowDarkCalculator.actions.PerformAllActions;
import com.redshift.ShadowDarkCalculator.actions.PerformOneAction;
import com.redshift.ShadowDarkCalculator.actions.misc.Devour;
import com.redshift.ShadowDarkCalculator.actions.weapons.Weapon;
import com.redshift.ShadowDarkCalculator.creatures.CreatureLabel;
import com.redshift.ShadowDarkCalculator.creatures.Monster;
import com.redshift.ShadowDarkCalculator.creatures.Stats;
import com.redshift.ShadowDarkCalculator.dice.MultipleDice;
import com.redshift.ShadowDarkCalculator.dice.RollModifier;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.*;

public class MarrowFiend extends Monster {

    public MarrowFiend(String name) {
        super(
                name,
                8,
                new Stats(18,18,16,14,16,16),
                15,
                D8.roll() + D8.roll() + D8.roll() + D8.roll() + D8.roll() + D8.roll() + D8.roll() + D8.roll(),
                new PerformOneAction(
                        new PerformAllActions(
                                new Weapon("Claw", new MultipleDice(D10), RollModifier.STRENGTH).addAttackRollBonus(3),
                                new Weapon("Claw", new MultipleDice(D10), RollModifier.STRENGTH).addAttackRollBonus(3)
                        ).setPriority(1),
                        new Devour().setPriority(4)
                )
        );
        getLabels().add(CreatureLabel.FRONT_LINE);
    }
}
