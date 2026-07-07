package com.redshift.ShadowDarkCalculator.party.loadout.actions;

import com.redshift.ShadowDarkCalculator.actions.Action;
import com.redshift.ShadowDarkCalculator.actions.PerformOneAction;
import com.redshift.ShadowDarkCalculator.actions.misc.Curative;
import com.redshift.ShadowDarkCalculator.actions.weapons.Weapon;
import com.redshift.ShadowDarkCalculator.actions.weapons.WeaponBuilder;
import com.redshift.ShadowDarkCalculator.creatures.Stats;
import com.redshift.ShadowDarkCalculator.dice.RollModifier;
import com.redshift.ShadowDarkCalculator.party.loadout.Bonuses;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D12;
import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D4;

/**
 * Specific action builder for the Ranger class.
 */

public class RangerActionBuilder implements ActionBuilder {

    @Override
    public Action build(Stats stats, Bonuses bonuses) {
        // TODO: Randomize the buildout?
        // Weapons: Dagger, longbow, longsword, shortbow, shortsword, spear, staff

        final Weapon weapon;

        if (stats.getStrength() > stats.getDexterity()) {
            // STR build.
            if (bonuses.isDamageDiceD12()) {
                weapon = new Weapon("Elven Longsword", D12, RollModifier.STRENGTH)
                        .addAttackRollBonus(bonuses.getMeleeAttackBonus())
                        .addDamageRollBonus(bonuses.getMeleeDamageBonus());
            } else {
                weapon = WeaponBuilder.LONGSWORD.build()
                        .addAttackRollBonus(bonuses.getMeleeAttackBonus())
                        .addDamageRollBonus(bonuses.getMeleeDamageBonus());
            }
        } else {
            // DEX build.
            if (bonuses.isDamageDiceD12()) {
                if (bonuses.isTwoHandsFree()) {
                    weapon = new Weapon("Elven Longbow", D12, RollModifier.DEXTERITY)
                            .addAttackRollBonus(bonuses.getRangedAttackBonus())
                            .addDamageRollBonus(bonuses.getRangedDamageBonus());
                } else {
                    weapon = new Weapon("Elven Dagger", D12, RollModifier.DEXTERITY)
                            .addAttackRollBonus(bonuses.getMeleeAttackBonus())
                            .addDamageRollBonus(bonuses.getMeleeDamageBonus());
                }
            } else {
                if (bonuses.isTwoHandsFree()) {
                    weapon = WeaponBuilder.LONGBOW.build()
                            .addAttackRollBonus(bonuses.getRangedAttackBonus())
                            .addDamageRollBonus(bonuses.getRangedDamageBonus());
                } else {
                    weapon = WeaponBuilder.DAGGER_DEX.build()
                            .addAttackRollBonus(bonuses.getMeleeAttackBonus())
                            .addDamageRollBonus(bonuses.getMeleeDamageBonus());
                }
            }
        }

        weapon.setPriority(1);

        // Ranger curative (i.e. healing potions, DC 15)
        final Curative curative = (Curative) new Curative().setPriority(1); // 50/50 chance to use curative
        if (bonuses.getSpellAdvantages() != 0) {
            curative.addAdvantage();
        }

        return new PerformOneAction(weapon, curative);
    }

}
