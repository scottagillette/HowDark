package com.redshift.ShadowDarkCalculator.actions.weapons;

import com.redshift.ShadowDarkCalculator.dice.RollModifier;
import com.redshift.ShadowDarkCalculator.dice.Dice;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.*;

/**
 * All simple weapons in the game available to characters and monsters.
 */

public enum WeaponBuilder {

    BASTARD_SWORD_1H("Bastard Sword 1h", D8, RollModifier.STRENGTH, WeaponType.SLASHING),
    BASTARD_SWORD_2H("Bastard Sword 2h", D10, RollModifier.STRENGTH, WeaponType.SLASHING),
    CLUB("Club", D4, RollModifier.STRENGTH, WeaponType.CRUSHING),
    CROSSBOW("Crossbow", D6, RollModifier.DEXTERITY, WeaponType.PIERCING),
    DAGGER_DEX("Dagger (DEX)", D4, RollModifier.DEXTERITY, WeaponType.PIERCING),
    DAGGER_STR("Dagger (STR)", D4, RollModifier.STRENGTH, WeaponType.PIERCING),
    FIST("Fist", D1, RollModifier.STRENGTH, WeaponType.PIERCING),
    GREAT_AXE_1H("Greataxe 1h", D8, RollModifier.STRENGTH, WeaponType.SLASHING),
    GREAT_AXE_2H("Greataxe 2h", D10, RollModifier.STRENGTH, WeaponType.SLASHING),
    GREAT_SWORD("Greatsword 2h", D12, RollModifier.STRENGTH, WeaponType.SLASHING),
    JAVELIN_STR("Javelin", D4, RollModifier.STRENGTH, WeaponType.PIERCING),
    JAVELIN_DEX("Javelin", D4, RollModifier.DEXTERITY, WeaponType.PIERCING),
    LONGBOW("Longbow", D8, RollModifier.DEXTERITY, WeaponType.PIERCING),
    LONGSWORD("Longsword", D8, RollModifier.STRENGTH, WeaponType.SLASHING),
    MACE("Mace", D6, RollModifier.STRENGTH, WeaponType.CRUSHING),
    SHORT_BOW("Short Bow", D4, RollModifier.DEXTERITY, WeaponType.PIERCING),
    SHORT_SWORD("Shortsword", D6, RollModifier.STRENGTH, WeaponType.SLASHING),
    SLING("Sling", D4, RollModifier.DEXTERITY, WeaponType.CRUSHING),
    SPEAR_STR("Spear (STR)", D6, RollModifier.STRENGTH, WeaponType.PIERCING),
    SPEAR_DEX("Spear (DEX)", D6, RollModifier.DEXTERITY, WeaponType.PIERCING),
    STAFF("Staff", D4, RollModifier.STRENGTH, WeaponType.CRUSHING),
    WAR_HAMMER("Warhammer", D10, RollModifier.STRENGTH, WeaponType.CRUSHING);

    private final String name;
    private final Dice dice;
    private final RollModifier rollModifier;
    private final WeaponType weaponType;

    WeaponBuilder(String name, Dice dice, RollModifier rollModifier, WeaponType weaponType) {
        this.name = name;
        this.dice = dice;
        this.rollModifier = rollModifier;
        this.weaponType = weaponType;
    }

    public Weapon build() {
        switch (weaponType) {
            case WeaponType.CRUSHING -> {
                return new Weapon(name, dice, rollModifier).addCrushing();
            }
            case WeaponType.PIERCING -> {
                return new Weapon(name, dice, rollModifier).addPiercing();
            }
            case WeaponType.SLASHING -> {
                return new Weapon(name, dice, rollModifier).addSlashing();
            }
            default -> {
                return new Weapon(name, dice, rollModifier);
            }
        }
    }

}