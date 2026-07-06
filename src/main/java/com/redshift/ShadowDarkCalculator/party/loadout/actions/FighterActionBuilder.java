package com.redshift.ShadowDarkCalculator.party.loadout.actions;

import com.redshift.ShadowDarkCalculator.actions.Action;
import com.redshift.ShadowDarkCalculator.actions.weapons.WeaponBuilder;
import com.redshift.ShadowDarkCalculator.creatures.Stats;
import com.redshift.ShadowDarkCalculator.party.loadout.Bonuses;

/**
 * Specific action builder for the Fighter class.
 */

public class FighterActionBuilder implements ActionBuilder {

    @Override
    public Action build(Stats stats, Bonuses bonuses) {
        // Fighter is pretty simple; STR; dual wield or not?

        final Action action;

        if (bonuses.isTwoHandsFree()) {
            action = WeaponBuilder.GREAT_SWORD.build();
        } else {
            action = WeaponBuilder.LONGSWORD.build();
        }

        return action;
    }

}
