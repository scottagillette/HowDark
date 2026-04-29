package com.redshift.ShadowDarkCalculator.creatures.monsters.monstrosities;

import com.redshift.ShadowDarkCalculator.actions.PerformOneAction;
import com.redshift.ShadowDarkCalculator.actions.weapons.Weapon;
import com.redshift.ShadowDarkCalculator.creatures.*;
import com.redshift.ShadowDarkCalculator.creatures.monsters.Monster;
import com.redshift.ShadowDarkCalculator.dice.MultipleDice;
import com.redshift.ShadowDarkCalculator.dice.RollModifier;
import lombok.extern.slf4j.Slf4j;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D6;
import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D8;

/**
 * Horse-sized, rust-brown insects. They burrow vast, underground warrens into the bedrock.
 * AC 14, HP 14, ATK 1 bite +4 (1d6) or 1 acid spray (near) +4 (2d6), MV near (burrow)
 * S +2, D +2, C +1, I -2, W +1, Ch -2, AL N, LV 3
 */

@Slf4j
public class Ankheg extends Monster {

    public Ankheg(String name) {
        super(
                name,
                3,
                new Stats(14,14,12,7,12,7),
                14,
                D8.roll() + D8.roll() + D8.roll() + 1,
                new PerformOneAction(
                        new Weapon("Bite", D6, RollModifier.STRENGTH, true).addAttackRollBonus(2).setPriority(2),
                        new Weapon("Acid Spray", new MultipleDice(D6, D6), RollModifier.STRENGTH, false).addAttackRollBonus(2).setPriority(1)
                )
        );
        getLabels().add(CreatureLabel.FRONT_LINE);
    }

}
