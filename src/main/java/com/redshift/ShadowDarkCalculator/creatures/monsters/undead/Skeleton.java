package com.redshift.ShadowDarkCalculator.creatures.monsters.undead;

import com.redshift.ShadowDarkCalculator.actions.PerformOneAction;
import com.redshift.ShadowDarkCalculator.actions.weapons.WeaponBuilder;
import com.redshift.ShadowDarkCalculator.creatures.CreatureLabel;
import com.redshift.ShadowDarkCalculator.creatures.Stats;
import com.redshift.ShadowDarkCalculator.creatures.monsters.UndeadMonster;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D8;

/**
 * A bleach-boned skeleton with red pinpoints of light in its eyes.
 * AC 13 (chainmail), HP 11, ATK 1 short sword +1 (1d6) or 1 short bow (far) +0 (1d4), MV near
 * S +1, D +0, C +2, I -2, W +0, Ch -1, AL C, LV 2
 * Undead. Immune to morale checks.
 */

public class Skeleton extends UndeadMonster {

    public Skeleton(String name) {
        super(
                name,
                2,
                new Stats(12,10,14,9,10,9),
                13,
                D8.roll() + D8.roll() + 2,
                new PerformOneAction(
                        WeaponBuilder.SHORTSWORD.build(),
                        WeaponBuilder.SHORTBOW.build()
                )
        );
        getLabels().add(CreatureLabel.FRONT_LINE);
        getLabels().add(CreatureLabel.HUMANOID); // Not a humanoid?
    }
}
