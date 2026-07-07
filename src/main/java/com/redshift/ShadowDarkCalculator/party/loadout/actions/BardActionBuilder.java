package com.redshift.ShadowDarkCalculator.party.loadout.actions;

import com.redshift.ShadowDarkCalculator.actions.Action;
import com.redshift.ShadowDarkCalculator.actions.PerformOneAction;
import com.redshift.ShadowDarkCalculator.actions.spells.Fascinate;
import com.redshift.ShadowDarkCalculator.actions.weapons.WeaponBuilder;
import com.redshift.ShadowDarkCalculator.creatures.Stats;
import com.redshift.ShadowDarkCalculator.party.loadout.Bonuses;

/**
 * Specific action builder for the Bard class.
 */

public class BardActionBuilder implements ActionBuilder {

    @Override
    public Action build(Stats stats, Bonuses bonuses) {

        // TODO: Randomize the buildout?
        // Crossbow, dagger, mace, shortbow, shortsword, spear, staff

        final Action action;

        if (stats.getStrength() >= stats.getDexterity()) {
            // STR build.
            if (bonuses.isTwoHandsFree()) {
                // No big time two hand'ers to use...
                action = WeaponBuilder.SPEAR_STR.build();
            } else {
                action = WeaponBuilder.SPEAR_STR.build();
            }
        } else {
            // DEX build.
            if (bonuses.isTwoHandsFree()) {
                action = WeaponBuilder.CROSSBOW.build();
            } else {
                action = WeaponBuilder.DAGGER_DEX.build();
            }
        }

        action.setPriority(1);

        // Add the Bard Fascinate ability!
        final Action fascinate = new Fascinate().setPriority(2);

        return new PerformOneAction(action, fascinate);
    }

}
