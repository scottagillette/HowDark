package com.redshift.ShadowDarkCalculator.api;

import com.redshift.ShadowDarkCalculator.actions.weapons.Weapon;
import com.redshift.ShadowDarkCalculator.actions.weapons.WeaponBuilder;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * Creates weapons by type name (e.g. "bastard-sword-1h") from a WeaponConfig, applying
 * enchantments (magical, silvered), roll bonuses and the action priority.
 *
 * Type names are the WeaponBuilder entries in kebab-case, so new weapons are picked up
 * automatically.
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

    public static Weapon create(WeaponConfig config) {
        config.validate();

        final WeaponBuilder builder = REGISTRY.get(normalize(config.getType()));

        if (builder == null) {
            throw new IllegalArgumentException(
                    "Unknown weapon: '" + config.getType() + "'. Available: " + availableWeapons());
        }

        final Weapon weapon = builder.build();

        if (config.isMagical()) {
            weapon.addMagical();
        }
        if (config.isSilvered()) {
            weapon.addSilvered();
        }
        if (config.getAttackRollBonus() != 0) {
            weapon.addAttackRollBonus(config.getAttackRollBonus());
        }
        if (config.getDamageRollBonus() != 0) {
            weapon.addDamageRollBonus(config.getDamageRollBonus());
        }

        weapon.setPriority(config.getPriority());

        return weapon;
    }

    public static Set<String> availableWeapons() {
        return new TreeSet<>(REGISTRY.keySet());
    }

    private static String normalize(String value) {
        return value.trim().toLowerCase().replaceAll("[\\s_]+", "-");
    }

}
