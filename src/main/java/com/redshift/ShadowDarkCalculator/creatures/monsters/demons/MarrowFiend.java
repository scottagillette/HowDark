package com.redshift.ShadowDarkCalculator.creatures.monsters.demons;

import com.redshift.ShadowDarkCalculator.actions.PerformAllActions;
import com.redshift.ShadowDarkCalculator.actions.PerformOneAction;
import com.redshift.ShadowDarkCalculator.actions.misc.Devour;
import com.redshift.ShadowDarkCalculator.actions.weapons.Weapon;
import com.redshift.ShadowDarkCalculator.creatures.CreatureLabel;
import com.redshift.ShadowDarkCalculator.creatures.monsters.Monster;
import com.redshift.ShadowDarkCalculator.creatures.Stats;
import com.redshift.ShadowDarkCalculator.dice.MultipleDice;
import com.redshift.ShadowDarkCalculator.dice.RollModifier;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.*;

/**
 * A wolfish hulk of razor bone and sinewy muscle. Black marrow tree sap drips from its jaws and the gaps
 * in its bony plating.
 * AC 15, HP 39, ATK 2 claws +7 (1d10) and 1 sap gout (near line) +5 (2d6 + sap), MV near (climb)
 * S +4, D +4, C +3, I +2, W +3, Ch +3, AL C, LV 8
 * Devour. Use turn to devour a humanoid body to regain 3d8 HP.
 * Sap. DC 15 DEX check or stuck in place. Repeat check on turn to escape. // TODO: Sap not implemented
 */

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
                                new Weapon("Claw", new MultipleDice(D10), RollModifier.STRENGTH, false).addAttackRollBonus(3),
                                new Weapon("Claw", new MultipleDice(D10), RollModifier.STRENGTH, false).addAttackRollBonus(3)
                        ).setPriority(1),
                        new Devour().setPriority(4)
                )
        );
        getLabels().add(CreatureLabel.FRONT_LINE);
    }
}
