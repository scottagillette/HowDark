package com.redshift.ShadowDarkCalculator.party.generator.actions;

import com.redshift.ShadowDarkCalculator.actions.Action;
import com.redshift.ShadowDarkCalculator.actions.PerformOneAction;
import com.redshift.ShadowDarkCalculator.actions.spells.Fascinate;
import com.redshift.ShadowDarkCalculator.actions.weapons.Weapon;
import com.redshift.ShadowDarkCalculator.actions.weapons.WeaponBuilder;
import com.redshift.ShadowDarkCalculator.creatures.Stats;
import com.redshift.ShadowDarkCalculator.party.generator.Bonuses;

/**
 * Specific action builder for the Bard class.
 */

public class BardActionBuilder implements ActionBuilder {

    @Override
    public Action build(Stats stats, Bonuses bonuses) {

        // TODO: Randomize the buildout?
        // Crossbow, dagger, mace, shortbow, shortsword, spear, staff

        final Weapon action;

        if (stats.getStrength() > stats.getDexterity()) {
            // STR build.
            action = WeaponBuilder.SPEAR_STR.build()
                    .addAttackRollBonus(bonuses.getMeleeAttackBonus())
                    .addDamageRollBonus(bonuses.getMeleeDamageBonus());
        } else {
            // DEX build.
            if (bonuses.isTwoHandsFree()) {
                action = WeaponBuilder.CROSSBOW.build()
                        .addAttackRollBonus(bonuses.getRangedAttackBonus())
                        .addDamageRollBonus(bonuses.getRangedDamageBonus());
            } else {
                action = WeaponBuilder.DAGGER_DEX.build()
                        .addAttackRollBonus(bonuses.getMeleeAttackBonus())
                        .addDamageRollBonus(bonuses.getMeleeDamageBonus());
            }
        }

        action.setPriority(1);

        // Add the Bard Fascinate ability!
        final Action fascinate = new Fascinate().setPriority(2);

        return new PerformOneAction(action, fascinate);
    }

}
