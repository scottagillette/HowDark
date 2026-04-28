package com.redshift.ShadowDarkCalculator.creatures.monsters.goblinoid;

import com.redshift.ShadowDarkCalculator.actions.weapons.WeaponBuilder;
import com.redshift.ShadowDarkCalculator.creatures.CreatureLabel;
import com.redshift.ShadowDarkCalculator.creatures.monsters.Monster;
import com.redshift.ShadowDarkCalculator.creatures.Stats;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D8;

/**
 * A scarred goblin with knotted muscles and a crown of iron.
 * AC 14 (chainmail), HP 20, ATK 1 spear (close/near) +3 (1d6), MV near
 * S +2, D +1, C +2, I -1, W +0, Ch +1, AL C, LV 4
 * Keen Senses. Can't be surprised.
 */

public class GoblinBoss extends Monster {

    public GoblinBoss(String name) {
        super(
                name,
                4,
                new Stats(14,12,14,9,10,12),
                14,
                D8.roll() + D8.roll() + D8.roll() + D8.roll() + 2,
                WeaponBuilder.SPEAR_STR.build().addAttackRollBonus(1)
        );
        getLabels().add(CreatureLabel.FRONT_LINE);
        getLabels().add(CreatureLabel.HUMANOID);
    }

}