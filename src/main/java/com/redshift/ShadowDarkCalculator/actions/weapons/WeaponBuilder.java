package com.redshift.ShadowDarkCalculator.actions.weapons;

import com.redshift.ShadowDarkCalculator.dice.RollModifier;
import com.redshift.ShadowDarkCalculator.dice.Dice;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.*;

/**
 * All simple weapons in the game available to characters and monsters.
 */

public enum WeaponBuilder {

    BASTARD_SWORD_1H("Bastard Sword 1h", D8, RollModifier.STRENGTH),
    BASTARD_SWORD_2H("Bastard Sword 2h", D10, RollModifier.STRENGTH),
    CLUB("Club", D4, RollModifier.STRENGTH),
    CROSSBOW("Crossbow", D6, RollModifier.DEXTERITY),
    DAGGER_DEX("Dagger (DEX)", D4, RollModifier.DEXTERITY),
    DAGGER_STR("Dagger (STR)", D4, RollModifier.STRENGTH),
    GREATAXE_1H("Greataxe 1h", D8, RollModifier.STRENGTH),
    GREATAXE_2H("Greataxe 2h", D10, RollModifier.STRENGTH),
    GREATSWORD("Greatsword 2h", D12, RollModifier.STRENGTH),
    JAVELIN_STR("Javelin", D4, RollModifier.STRENGTH),
    JAVELIN_DEX("Javelin", D4, RollModifier.DEXTERITY),
    LONGBOW("Longbow", D8, RollModifier.DEXTERITY),
    LONGSWORD("Longsword", D8, RollModifier.STRENGTH),
    MACE("Mace", D6, RollModifier.STRENGTH),
    SHORTBOW("Short Bow", D4, RollModifier.DEXTERITY),
    SHORTSWORD("Shortsword", D6, RollModifier.STRENGTH),
    SPEAR_STR("Spear (STR)", D6, RollModifier.STRENGTH),
    SPEAR_DEX("Spear (DEX)", D6, RollModifier.DEXTERITY),
    STAFF("Staff", D4, RollModifier.STRENGTH),
    WARHAMMER("Warhammer", D10, RollModifier.STRENGTH);

    private final String name;
    private final Dice dice;
    private final RollModifier rollModifier;

    WeaponBuilder(String name, Dice dice, RollModifier rollModifier) {
        this.name = name;
        this.dice = dice;
        this.rollModifier = rollModifier;
    }

    public Weapon build() {
        return new Weapon(name, dice, rollModifier);
    }

    public Weapon build(int attackModifierBonus) {
        return new Weapon(name, dice, rollModifier, false, false, attackModifierBonus, 0,1);
    }

    public Weapon build(int attackModifierBonus, int damageModifierBonus) {
        return new Weapon(name, dice, rollModifier, false, false, attackModifierBonus, damageModifierBonus,1);
    }

    public Weapon build(int attackModifierBonus, int damageModifierBonus, boolean magical, boolean silvered, int priority) {
        return new Weapon(name, dice, rollModifier, magical, silvered, attackModifierBonus, damageModifierBonus, priority);
    }

    public Weapon buildSilvered() {
        return new Weapon(name, dice, rollModifier, false, true, 0, 0,1);
    }
    public Weapon buildMagicPlus1() {
        return new Weapon(name, dice, rollModifier, true, false, 1, 1,1);
    }

    public Weapon buildMagicPlus2() {
        return new Weapon(name, dice, rollModifier, true, false, 2, 2,1);
    }

    public Weapon buildMagicPlus3() {
        return new Weapon(name, dice, rollModifier, true, false, 3, 3,1);
    }
}