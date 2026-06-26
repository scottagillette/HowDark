package com.redshift.ShadowDarkCalculator.creatures.monsters.giants;

import com.redshift.ShadowDarkCalculator.actions.PerformAllActions;
import com.redshift.ShadowDarkCalculator.actions.PerformOneAction;
import com.redshift.ShadowDarkCalculator.actions.weapons.Weapon;
import com.redshift.ShadowDarkCalculator.creatures.CreatureLabel;
import com.redshift.ShadowDarkCalculator.creatures.Stats;
import com.redshift.ShadowDarkCalculator.creatures.monsters.Monster;
import com.redshift.ShadowDarkCalculator.dice.MultipleDice;
import com.redshift.ShadowDarkCalculator.dice.RollModifier;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.*;

/**
 * Reclusive, one-eyed giants towering 20' high. They live simply on remote farmlands.
 * AC 11 (leather), HP 38, ATK 2 greatclub +7 (2d8) or 1 rock (far) +5 (1d12), MV double near
 * S +5, D +0, C +2, I -1, W -2, Ch +0, AL C, LV 8
 */

public class Cyclops extends Monster {

    public Cyclops(String name) {
        super(
                name,
                8,
                new Stats(20, 10, 14, 8, 6, 10),
                11,
                D8.roll() + D8.roll() + D8.roll() + D8.roll() +
                        D8.roll() + D8.roll() + D8.roll() + D8.roll() + 2,
                new PerformOneAction(
                        new PerformAllActions(
                                new Weapon("Great Club", new MultipleDice(D8, D8), RollModifier.STRENGTH).addCrushing().addAttackRollBonus(2),
                                new Weapon("Great Club", new MultipleDice(D8, D8), RollModifier.STRENGTH).addCrushing().addAttackRollBonus(2)
                        ),
                        new Weapon("Rock", D12, RollModifier.STRENGTH).addCrushing()
                )
        );
        getLabels().add(CreatureLabel.FRONT_LINE);
        getLabels().add(CreatureLabel.CHAOTIC);
    }
}
