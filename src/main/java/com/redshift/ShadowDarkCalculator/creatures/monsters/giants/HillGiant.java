package com.redshift.ShadowDarkCalculator.creatures.monsters.giants;

import com.redshift.ShadowDarkCalculator.actions.PerformAllActions;
import com.redshift.ShadowDarkCalculator.actions.PerformOneAction;
import com.redshift.ShadowDarkCalculator.actions.weapons.Weapon;
import com.redshift.ShadowDarkCalculator.creatures.CreatureLabel;
import com.redshift.ShadowDarkCalculator.creatures.monsters.Monster;
import com.redshift.ShadowDarkCalculator.creatures.Stats;
import com.redshift.ShadowDarkCalculator.dice.MultipleDice;
import com.redshift.ShadowDarkCalculator.dice.RollModifier;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.*;

/**
 * Fleshy hulks with leathery skin and broad, sloping foreheads. Cruel, boorish, and dim-witted.
 * AC 11 (leather), HP 34, ATK 2 great club +6 (2d8) or 1 boulder (far) +6 (2d10), MV double near
 * S +4, D +0, C +3, I -2, W -2, Ch -2, AL C, LV 7
 */

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
                                new Weapon("Great Club", new MultipleDice(D8, D8), RollModifier.STRENGTH).addCrushing().addAttackRollBonus(2),
                                new Weapon("Great Club", new MultipleDice(D8, D8), RollModifier.STRENGTH).addCrushing().addAttackRollBonus(2)
                        ),
                        new Weapon("Boulder", D10, RollModifier.STRENGTH).addCrushing().addAttackRollBonus(2)
                )
        );
        getLabels().add(CreatureLabel.FRONT_LINE);
    }

}
