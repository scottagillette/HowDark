package com.redshift.ShadowDarkCalculator.party.loadout.actions;

import com.redshift.ShadowDarkCalculator.actions.Action;
import com.redshift.ShadowDarkCalculator.actions.weapons.WeaponBuilder;
import com.redshift.ShadowDarkCalculator.creatures.Stats;
import com.redshift.ShadowDarkCalculator.party.loadout.Bonuses;

/**
 * Specific action builder for the Thief class.
 */

public class ThiefActionBuilder implements ActionBuilder {

    @Override
    public Action build(Stats stats, Bonuses bonuses) {
        // Thief is pretty simple; DEX; dual wield or not?

        // TODO: Randomize from all weapons they can use?
        // Club, crossbow, dagger, shortbow, shortsword

        final Action action;

        if (bonuses.isTwoHandsFree()) {
            action = WeaponBuilder.CROSSBOW.build();
        } else {
            action = WeaponBuilder.DAGGER_DEX.build();
        }

        return action;
    }

}
