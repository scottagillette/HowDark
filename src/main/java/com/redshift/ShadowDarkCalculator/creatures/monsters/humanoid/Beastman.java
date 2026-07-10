package com.redshift.ShadowDarkCalculator.creatures.monsters.humanoid;

import com.redshift.ShadowDarkCalculator.actions.PerformAllActions;
import com.redshift.ShadowDarkCalculator.actions.weapons.Weapon;
import com.redshift.ShadowDarkCalculator.actions.weapons.WeaponBuilder;
import com.redshift.ShadowDarkCalculator.creatures.CreatureLabel;
import com.redshift.ShadowDarkCalculator.creatures.Stats;
import com.redshift.ShadowDarkCalculator.creatures.monsters.Monster;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D6;
import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D8;

/**
 * A cave hominid with scraggly fur and a stone-tipped spear.
 * AC 12 (leather), HP 5, ATK 1 spear (close/near) +2 (1d6 + 1), MV near
 * S +2, D +1, C +1, I -2, W +1, Ch -1, AL C, LV 1
 * Brutal. +1 damage with melee weapons (included).
 */

public class Beastman extends Monster {

    public Beastman(String name) {
        super(
                name,
                1,
                new Stats(14, 12, 12, 6,12, 8),
                12,
                D8.roll() + 1,
                WeaponBuilder.SPEAR_STR.build().addDamageRollBonus(1)
        );
        getLabels().add(CreatureLabel.FRONT_LINE);
        getLabels().add(CreatureLabel.HUMANOID);
        getLabels().add(CreatureLabel.CHAOTIC);
    }

}
