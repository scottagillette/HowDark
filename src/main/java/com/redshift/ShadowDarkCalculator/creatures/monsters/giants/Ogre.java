package com.redshift.ShadowDarkCalculator.creatures.monsters.giants;

import com.redshift.ShadowDarkCalculator.actions.PerformAllActions;
import com.redshift.ShadowDarkCalculator.actions.weapons.Weapon;
import com.redshift.ShadowDarkCalculator.creatures.CreatureLabel;
import com.redshift.ShadowDarkCalculator.creatures.monsters.Monster;
import com.redshift.ShadowDarkCalculator.creatures.Stats;
import com.redshift.ShadowDarkCalculator.dice.MultipleDice;
import com.redshift.ShadowDarkCalculator.dice.RollModifier;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D6;
import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D8;

/**
 * A massive, dim-witted brute with tusks and a heavy frame. Often lords over goblins or orcs.
 * AC 9, HP 30, ATK 2 great club +6 (2d6), MV near
 * S +4, D -1, C +3, I -2, W -2, Ch -2, AL C, LV 6
 */

public class Ogre extends Monster {

    public Ogre(String name) {
        super(
                name,
                6,
                new Stats(18,9,17,7,7,7),
                9,
                D8.roll() + D8.roll() + D8.roll() + D8.roll() + D8.roll() + D8.roll() + 3,
                new PerformAllActions(
                        new Weapon("Great Club", new MultipleDice(D6, D6), RollModifier.STRENGTH, false).addAttackRollBonus(2),
                        new Weapon("Great Club", new MultipleDice(D6, D6), RollModifier.STRENGTH, false).addAttackRollBonus(2)
                )
        );
        getLabels().add(CreatureLabel.FRONT_LINE);
    }

}
