package com.redshift.ShadowDarkCalculator.creatures.monsters.humanoid;

import com.redshift.ShadowDarkCalculator.actions.PerformOneAction;
import com.redshift.ShadowDarkCalculator.actions.weapons.WeaponBuilder;
import com.redshift.ShadowDarkCalculator.creatures.CreatureLabel;
import com.redshift.ShadowDarkCalculator.creatures.Stats;
import com.redshift.ShadowDarkCalculator.creatures.monsters.Monster;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D8;

/**
 * Hard-bitten rogue in tattered leathers and a hooded cloak.
 * AC 13 (leather + shield), HP 4, ATK 1 club +1 (1d4) or 1 short bow (far) +0 (1d4), MV near
 * S +1, D +0, C +0, I -1, W +0, Ch -1, AL C, LV 1
 * Ambush. Deal an extra die of damage when undetected.
 */

public class Bandit extends Monster {

    public Bandit(String name) {
        super(
                name,
                2,
                new Stats(12, 10, 10, 8,10, 8),
                13,
                D8.roll(),
                new PerformOneAction(
                        WeaponBuilder.CLUB.build().setPriority(1),
                        WeaponBuilder.SHORT_BOW.build().setPriority(1)
                )
        );
        getLabels().add(CreatureLabel.FRONT_LINE);
        getLabels().add(CreatureLabel.HUMANOID);
    }
}