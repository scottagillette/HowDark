package com.redshift.ShadowDarkCalculator.actions.weapons;

import com.redshift.ShadowDarkCalculator.dice.Dice;
import com.redshift.ShadowDarkCalculator.dice.RollModifier;

/**
 * A custom weapon with custom attributes and optional modifiers.
 */

public class CustomWeapon extends Weapon {

    public CustomWeapon(String name, Dice dice, RollModifier rollModifier, int attackModifier) {
        super(name, dice, rollModifier);
        addAttackBonus(attackModifier);
    }
    public CustomWeapon(String name, Dice dice, RollModifier rollModifier, int attackModifier, int damageBonus) {
        super(name, dice, rollModifier);
        addAttackBonus(attackModifier);
        addDamageBonus(damageBonus);
    }

    public CustomWeapon(String name, Dice dice, RollModifier rollModifier, int attackModifier, int damageBonus, boolean magical) {
        super(name, dice, rollModifier);
        addAttackBonus(attackModifier);
        addDamageBonus(damageBonus);
    }

}
