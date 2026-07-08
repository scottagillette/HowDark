package com.redshift.ShadowDarkCalculator.party.generator.actions;

import com.redshift.ShadowDarkCalculator.actions.Action;
import com.redshift.ShadowDarkCalculator.actions.weapons.Weapon;
import com.redshift.ShadowDarkCalculator.actions.weapons.WeaponBuilder;
import com.redshift.ShadowDarkCalculator.creatures.Stats;
import com.redshift.ShadowDarkCalculator.party.generator.Bonuses;

/**
 * Specific action builder for the Paladin class.
 */

public class PaladinActionBuilder implements ActionBuilder {

    @Override
    public Action build(Stats stats, Bonuses bonuses) {
        // Bastard sword, dagger, greatsword, javelin, lance, longsword, shortsword

        final Weapon action;

        if (bonuses.isTwoHandsFree()) {
            // Paladin...Named Blade +0 Magic Blade
            action = WeaponBuilder.GREAT_SWORD.build();
        } else {
            // Paladin...Named Blade +0 Magic Blade
            action = WeaponBuilder.LONGSWORD.build().addMagical();
        }

        action
                .addMagical()
                .addAttackRollBonus(bonuses.getMeleeAttackBonus())
                .addDamageRollBonus(bonuses.getMeleeDamageBonus());

        return action;
    }

}
