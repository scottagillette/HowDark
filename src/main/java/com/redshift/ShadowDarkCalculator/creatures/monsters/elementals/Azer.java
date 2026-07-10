package com.redshift.ShadowDarkCalculator.creatures.monsters.elementals;

import com.redshift.ShadowDarkCalculator.actions.DamageType;
import com.redshift.ShadowDarkCalculator.actions.PerformAllActions;
import com.redshift.ShadowDarkCalculator.actions.PerformOneAction;
import com.redshift.ShadowDarkCalculator.actions.weapons.WeaponBuilder;
import com.redshift.ShadowDarkCalculator.creatures.CreatureLabel;
import com.redshift.ShadowDarkCalculator.creatures.Stats;
import com.redshift.ShadowDarkCalculator.creatures.monsters.Monster;
import com.redshift.ShadowDarkCalculator.resistance.FireImmunity;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D8;

/**
 * Dwarves with bronze, metallic skin and flames in place of hair. Gifted blacksmiths.
 * AC 15, HP 15, ATK 2 flaming warhammer +3 (1d10, ignites flammables) or 1 crossbow (far) +0 (1d6), MV near
 * S +3, D +0, C +2, I +0, W +0, Ch +0, AL L, LV 3
 * Impervious. Fire immune.
 */

public class Azer extends Monster {

    public Azer(String name) {
        super(
                name,
                3,
                new Stats(16, 10, 14, 10, 10, 10),
                15,
                D8.roll() + D8.roll() + D8.roll() + 2,
                new PerformOneAction(
                        new PerformAllActions(
                                WeaponBuilder.WAR_HAMMER.build(),
                                WeaponBuilder.WAR_HAMMER.build()
                        ).setPriority(2),
                        new PerformOneAction(WeaponBuilder.CROSSBOW.build().setPriority(1))
                )
        );
        getLabels().add(CreatureLabel.FRONT_LINE);
        getLabels().add(CreatureLabel.HUMANOID);
        getLabels().add(CreatureLabel.LAWFUL);
    }

    @Override
    public void takeDamage(int amount, DamageType damageType) {
        int damage = new FireImmunity().calculateDamage(this, amount, damageType);
        if (damage > 0) {
            super.takeDamage(damage, damageType);
        }
    }

}
