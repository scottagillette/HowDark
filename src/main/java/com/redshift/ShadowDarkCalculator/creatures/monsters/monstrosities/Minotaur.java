package com.redshift.ShadowDarkCalculator.creatures.monsters.monstrosities;

import com.redshift.ShadowDarkCalculator.actions.PerformAllActions;
import com.redshift.ShadowDarkCalculator.actions.weapons.Weapon;
import com.redshift.ShadowDarkCalculator.creatures.CreatureLabel;
import com.redshift.ShadowDarkCalculator.creatures.Stats;
import com.redshift.ShadowDarkCalculator.creatures.monsters.Monster;
import com.redshift.ShadowDarkCalculator.dice.RollModifier;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.*;

/**
 * Ferocious bull-men with hooves and curved horns. They live in maze like tunnels.
 * AC 14 (chainmail), HP 34, ATK 2 greataxe +6 (1d10) and 1 horns +6 (1d12), MV near
 * S +4, D +1, C +3, I +1, W +2, Ch +1, AL C, LV 7
 * Charge. In place of attacks, move up to double near in a straight line and make 1
 * horn attack. If hit, x2 damage.
 */

public class Minotaur extends Monster {

    public Minotaur(String name) {
        super(
                name,
                7,
                new Stats(18,12,16,12,14,12),
                14,
                D8.roll() + D8.roll() + D8.roll() + D8.roll() + D8.roll() + D8.roll() +  D8.roll() + 3,
                new PerformAllActions(
                        new Weapon("Great Axe", D10, RollModifier.STRENGTH).addSlashing().addAttackRollBonus(2),
                        new Weapon("Great Axe", D10, RollModifier.STRENGTH).addSlashing().addAttackRollBonus(2)
                )
        );
        getLabels().add(CreatureLabel.FRONT_LINE);
        getLabels().add(CreatureLabel.CHAOTIC);
    }

}
