package com.redshift.ShadowDarkCalculator.creatures.monsters;

import lombok.Data;

/**
 * A monster (or group of identical monsters) selected by type.
 */

@Data
public class MonsterConfig {

    private String type;

    private String name;

    private int count = 1;

    public void validate() {
        if (type == null || type.isBlank()) {
            throw new IllegalArgumentException("Monster entry is missing a type");
        }
        if (count < 1) {
            throw new IllegalArgumentException("Monster count must be at least 1: " + count);
        }
    }

}
