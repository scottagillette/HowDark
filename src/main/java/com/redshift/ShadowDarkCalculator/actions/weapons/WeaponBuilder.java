package com.redshift.ShadowDarkCalculator.actions.weapons;

import com.redshift.ShadowDarkCalculator.dice.RollModifier;
import com.redshift.ShadowDarkCalculator.dice.Dice;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.*;

/**
 * All simple weapons in the game available to characters and monsters.
 */

public enum WeaponBuilder {

    BASTARD_SWORD_1H("Bastard Sword 1h", D8, RollModifier.STRENGTH, false),
    BASTARD_SWORD_2H("Bastard Sword 2h", D10, RollModifier.STRENGTH, false),
    CLUB("Club", D4, RollModifier.STRENGTH, false),
    CROSSBOW("Crossbow", D6, RollModifier.DEXTERITY, true),
    DAGGER_DEX("Dagger (DEX)", D4, RollModifier.DEXTERITY, true),
    DAGGER_STR("Dagger (STR)", D4, RollModifier.STRENGTH, true),
    FIST("Fist", D1, RollModifier.STRENGTH, false),
    GREATAXE_1H("Greataxe 1h", D8, RollModifier.STRENGTH, false),
    GREATAXE_2H("Greataxe 2h", D10, RollModifier.STRENGTH, false),
    GREATSWORD("Greatsword 2h", D12, RollModifier.STRENGTH, false),
    JAVELIN_STR("Javelin", D4, RollModifier.STRENGTH, true),
    JAVELIN_DEX("Javelin", D4, RollModifier.DEXTERITY, true),
    LONGBOW("Longbow", D8, RollModifier.DEXTERITY, true),
    LONGSWORD("Longsword", D8, RollModifier.STRENGTH, false),
    MACE("Mace", D6, RollModifier.STRENGTH, false),
    SHORTBOW("Short Bow", D4, RollModifier.DEXTERITY, true),
    SHORTSWORD("Shortsword", D6, RollModifier.STRENGTH, false),
    SLING("Sling", D4, RollModifier.DEXTERITY, false),
    SPEAR_STR("Spear (STR)", D6, RollModifier.STRENGTH, true),
    SPEAR_DEX("Spear (DEX)", D6, RollModifier.DEXTERITY, true),
    STAFF("Staff", D4, RollModifier.STRENGTH, false),
    WARHAMMER("Warhammer", D10, RollModifier.STRENGTH, false);

    private final String name;
    private final Dice dice;
    private final RollModifier rollModifier;
    private final boolean piercing;

    WeaponBuilder(String name, Dice dice, RollModifier rollModifier, boolean piercing) {
        this.name = name;
        this.dice = dice;
        this.rollModifier = rollModifier;
        this.piercing = piercing;
    }

    public Weapon build() {
        return new Weapon(name, dice, rollModifier, piercing);
    }

}