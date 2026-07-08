package com.redshift.ShadowDarkCalculator.party.generator.actions;

import com.redshift.ShadowDarkCalculator.actions.Action;
import com.redshift.ShadowDarkCalculator.actions.weapons.Weapon;
import com.redshift.ShadowDarkCalculator.actions.weapons.WeaponBuilder;
import com.redshift.ShadowDarkCalculator.creatures.Stats;
import com.redshift.ShadowDarkCalculator.party.generator.Bonuses;

/**
 * Specific action builder for the Fighter class.
 */

public class FighterActionBuilder implements ActionBuilder {

    @Override
    public Action build(Stats stats, Bonuses bonuses) {
        // Fighter is pretty simple; STR; two-handed or not.

        final Weapon action;

        if (bonuses.isTwoHandsFree()) {
            action = WeaponBuilder.GREAT_SWORD.build();
        } else {
            action = WeaponBuilder.LONGSWORD.build();
        }
        // Add any bonuses
        action
                .addAttackRollBonus(bonuses.getMeleeAttackBonus())
                .addDamageRollBonus(bonuses.getMeleeDamageBonus());

        return action;
    }

}
