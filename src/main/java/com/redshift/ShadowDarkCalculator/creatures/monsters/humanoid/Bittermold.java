package com.redshift.ShadowDarkCalculator.creatures.monsters.humanoid;

import com.redshift.ShadowDarkCalculator.actions.DamageType;
import com.redshift.ShadowDarkCalculator.actions.PerformOneAction;
import com.redshift.ShadowDarkCalculator.actions.weapons.WeaponBuilder;
import com.redshift.ShadowDarkCalculator.creatures.CreatureLabel;
import com.redshift.ShadowDarkCalculator.creatures.monsters.Monster;
import com.redshift.ShadowDarkCalculator.creatures.Stats;
import lombok.extern.slf4j.Slf4j;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D8;

/**
 * A staring, stooped human that scuttles along. Pale, wet skin.
 * AC 12, HP 5, ATK 1 short sword +1 (1d6) or 1 sling (far) +2 (1d4), MV near
 * S +1, D +2, C +1, I +0, W +0, Ch +0, AL C, LV 1
 * Rubbery. Half damage from stabbing weapons.
 */

@Slf4j
public class Bittermold extends Monster {

    public Bittermold(String name) {
        super(
                name,
                1,
                new Stats(12,14,12,10,10,10),
                12,
                D8.roll() + 1,
                new PerformOneAction(
                    WeaponBuilder.SHORT_SWORD.build(),
                    WeaponBuilder.SLING.build()
                )
        );
        getLabels().add(CreatureLabel.FRONT_LINE);
        getLabels().add(CreatureLabel.HUMANOID);
    }

    @Override
    public void takeDamage(int amount, DamageType damageType) {
        if (damageType.isPiercing()) {
            log.info("{} seems to take less damage than normal from piercing!", getName());
            super.takeDamage((amount / 2), damageType);
        } else {
            super.takeDamage(amount, damageType);
        }
    }
}
