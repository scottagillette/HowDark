package com.redshift.ShadowDarkCalculator.creatures.monsters.goblinoid;

import com.redshift.ShadowDarkCalculator.actions.PerformOneAction;
import com.redshift.ShadowDarkCalculator.actions.weapons.WeaponBuilder;
import com.redshift.ShadowDarkCalculator.creatures.CreatureLabel;
import com.redshift.ShadowDarkCalculator.creatures.monsters.Monster;
import com.redshift.ShadowDarkCalculator.creatures.Stats;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D8;

/**
 * A short, hairless humanoid with green skin and pointy ears.
 * AC 11, HP 5, ATK 1 club +0 (1d4) or 1 shortbow (far) +1 (1d4), MV near
 * S +0, D +1, C +1, I -1, W -1, Ch -2, AL C, LV 1
 * Keen Senses. Can't be surprised.
 */

public class Goblin extends Monster {

    public Goblin(String name) {
        super(
                name,
                1,
                new Stats(10,12,12,9,9,7),
                12,
                D8.roll() + 1,
                new PerformOneAction(
                        WeaponBuilder.DAGGER_DEX.build(),
                        WeaponBuilder.SHORTBOW.build()
                )
        );
        getLabels().add(CreatureLabel.BACKLINE);
        getLabels().add(CreatureLabel.HUMANOID);
    }

}
