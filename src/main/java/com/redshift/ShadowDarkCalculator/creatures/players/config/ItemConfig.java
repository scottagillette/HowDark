package com.redshift.ShadowDarkCalculator.creatures.players.config;

import lombok.Data;

@Data
public class ItemConfig {
    private String type;

    private int priority = 1;

    public void validate() {
        if (type == null || type.isBlank()) {
            throw new IllegalArgumentException("Item entry is missing a type");
        }
        if (priority < 1) {
            throw new IllegalArgumentException("Item priority must be at least 1: " + priority);
        }
    }
}
