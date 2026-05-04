package com.redshift.ShadowDarkCalculator.creatures.monsters.humanoid;

import com.redshift.ShadowDarkCalculator.actions.PerformAllActions;
import com.redshift.ShadowDarkCalculator.actions.weapons.WeaponBuilder;
import com.redshift.ShadowDarkCalculator.creatures.CreatureLabel;
import com.redshift.ShadowDarkCalculator.creatures.Stats;
import com.redshift.ShadowDarkCalculator.creatures.monsters.Monster;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D8;

/**
 * A knight in blackened armor riddled with cruel barbs.
 * AC 17 (plate mail + shield), HP 28, ATK 3 bastard sword +4 (1d8 + 2), MV near
 * S +3, D +0, C +1, I +0, W +0, Ch +2, AL C, LV 6
 * Bloodlust. +2 damage with melee weapons (included).
 */

public class Reaver extends Monster {

    public Reaver(String name) {
        super(
                name,
                6,
                new Stats(16, 10, 12, 10,10, 14),
                17,
                D8.roll() + D8.roll() + D8.roll() + D8.roll() + D8.roll() + D8.roll() + 1,
                new PerformAllActions(
                        WeaponBuilder.BASTARD_SWORD_1H.build().addAttackRollBonus(1).addDamageRollBonus(2),
                        WeaponBuilder.BASTARD_SWORD_1H.build().addAttackRollBonus(1).addDamageRollBonus(2),
                        WeaponBuilder.BASTARD_SWORD_1H.build().addAttackRollBonus(1).addDamageRollBonus(2)

                )
        );
        getLabels().add(CreatureLabel.FRONT_LINE);
        getLabels().add(CreatureLabel.HUMANOID);
    }

}
