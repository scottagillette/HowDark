package com.redshift.ShadowDarkCalculator.creatures.players.config;

import com.redshift.ShadowDarkCalculator.actions.weapons.Weapon;
import com.redshift.ShadowDarkCalculator.actions.weapons.WeaponBuilder;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * Creates weapons by type name (e.g. "bastard-sword-1h") from a WeaponConfig, applying
 * enchantments (magical, silvered), roll bonuses and the action priority. Type names
 * are the WeaponBuilder entries in kebab-case, so new weapons are picked up automatically.
 */

public final class WeaponFactory {

    private static final Map<String, WeaponBuilder> REGISTRY = new LinkedHashMap<>();

    static {
        for (WeaponBuilder builder : WeaponBuilder.values()) {
            REGISTRY.put(builder.name().toLowerCase().replace('_', '-'), builder);
        }
    }

    private WeaponFactory() {
    }

    public static Set<String> availableWeapons() {
        return new TreeSet<>(REGISTRY.keySet());
    }

    public static Weapon create(WeaponConfig weaponConfig) {
        weaponConfig.validate();

        final WeaponBuilder builder = REGISTRY.get(normalize(weaponConfig.getType()));

        if (builder == null) {
            throw new IllegalArgumentException("Unknown weapon: '" + weaponConfig.getType() + "'. Available: " + availableWeapons());
        }

        final Weapon weapon = builder.build();

        if (weaponConfig.isMagical()) weapon.addMagical();
        if (weaponConfig.isSilvered()) weapon.addSilvered();
        if (weaponConfig.getAttackRollBonus() != 0) weapon.addAttackRollBonus(weaponConfig.getAttackRollBonus());
        if (weaponConfig.getDamageRollBonus() != 0) weapon.addDamageRollBonus(weaponConfig.getDamageRollBonus());

        weapon.setPriority(weaponConfig.getPriority());

        return weapon;
    }

    private static String normalize(String value) {
        return value.trim().toLowerCase().replaceAll("[\\s_]+", "-");
    }

}
