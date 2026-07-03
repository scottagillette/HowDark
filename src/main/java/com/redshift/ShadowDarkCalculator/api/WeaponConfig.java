package com.redshift.ShadowDarkCalculator.api;

import lombok.Data;

/**
 * A weapon carried by a custom party member, selected by type (e.g. "bastard-sword-1h")
 * with optional enchantments and roll bonuses.
 */

@Data
public class WeaponConfig {

    private String type;

    private int priority = 1;

    private boolean magical = false;

    private boolean silvered = false;

    private int attackRollBonus = 0;

    private int damageRollBonus = 0;

    public void validate() {
        if (type == null || type.isBlank()) {
            throw new IllegalArgumentException("weapon entry is missing a type");
        }
        if (priority < 1) {
            throw new IllegalArgumentException("weapon priority must be at least 1: " + priority);
        }
    }

}
