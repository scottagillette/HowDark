package com.redshift.ShadowDarkCalculator.party.loadout.actions;

import com.redshift.ShadowDarkCalculator.actions.Action;
import com.redshift.ShadowDarkCalculator.actions.weapons.Weapon;
import com.redshift.ShadowDarkCalculator.actions.weapons.WeaponBuilder;
import com.redshift.ShadowDarkCalculator.creatures.Stats;
import com.redshift.ShadowDarkCalculator.dice.RollModifier;
import com.redshift.ShadowDarkCalculator.party.loadout.Bonuses;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D12;

/**
 * Specific action builder for the Ranger class.
 */

public class RangerActionBuilder implements ActionBuilder {

    @Override
    public Action build(Stats stats, Bonuses bonuses) {
        // TODO: Randomize the buildout?
        // Weapons: Dagger, longbow, longsword, shortbow, shortsword, spear, staff

        final Weapon action;

        if (stats.getStrength() > stats.getDexterity()) {
            // STR build.
            if (bonuses.isDamageDiceD12()) {
                action = new Weapon("Elven Longsword", D12, RollModifier.STRENGTH)
                        .addAttackRollBonus(bonuses.getMeleeAttackBonus())
                        .addDamageRollBonus(bonuses.getMeleeDamageBonus());
            } else {
                action = WeaponBuilder.LONGSWORD.build()
                        .addAttackRollBonus(bonuses.getMeleeAttackBonus())
                        .addDamageRollBonus(bonuses.getMeleeDamageBonus());
            }
        } else {
            // DEX build.
            if (bonuses.isDamageDiceD12()) {
                if (bonuses.isTwoHandsFree()) {
                    action = new Weapon("Elven Longbow", D12, RollModifier.DEXTERITY)
                            .addAttackRollBonus(bonuses.getRangedAttackBonus())
                            .addDamageRollBonus(bonuses.getRangedDamageBonus());
                } else {
                    action = new Weapon("Elven Dagger", D12, RollModifier.DEXTERITY)
                            .addAttackRollBonus(bonuses.getMeleeAttackBonus())
                            .addDamageRollBonus(bonuses.getMeleeDamageBonus());
                }
            } else {
                if (bonuses.isTwoHandsFree()) {
                    action = WeaponBuilder.LONGBOW.build()
                            .addAttackRollBonus(bonuses.getRangedAttackBonus())
                            .addDamageRollBonus(bonuses.getRangedDamageBonus());
                } else {
                    action = WeaponBuilder.DAGGER_DEX.build()
                            .addAttackRollBonus(bonuses.getMeleeAttackBonus())
                            .addDamageRollBonus(bonuses.getMeleeDamageBonus());
                }
            }
        }

        action.setPriority(1);

        return action;
    }

}
