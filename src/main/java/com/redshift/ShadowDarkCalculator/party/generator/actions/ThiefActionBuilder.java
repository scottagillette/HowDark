package com.redshift.ShadowDarkCalculator.party.generator.actions;

import com.redshift.ShadowDarkCalculator.actions.Action;
import com.redshift.ShadowDarkCalculator.actions.weapons.Weapon;
import com.redshift.ShadowDarkCalculator.actions.weapons.WeaponBuilder;
import com.redshift.ShadowDarkCalculator.creatures.Stats;
import com.redshift.ShadowDarkCalculator.party.generator.Bonuses;

/**
 * Specific action builder for the Thief class.
 */

public class ThiefActionBuilder implements ActionBuilder {

    @Override
    public Action build(Stats stats, Bonuses bonuses) {
        // Thief is pretty simple; DEX; dual wield or not?

        // TODO: Randomize from all weapons they can use?
        // Club, crossbow, dagger, shortbow, shortsword

        final Weapon action;

        if (bonuses.isTwoHandsFree()) {
            action = WeaponBuilder.CROSSBOW.build()
                    .addAttackRollBonus(bonuses.getRangedAttackBonus())
                    .addDamageRollBonus(bonuses.getRangedDamageBonus());
        } else {
            action = WeaponBuilder.DAGGER_DEX.build()
                    .addAttackRollBonus(bonuses.getMeleeAttackBonus())
                    .addDamageRollBonus(bonuses.getMeleeDamageBonus());
        }

        return action;
    }

}
