package com.redshift.ShadowDarkCalculator.creatures.players.config;

import lombok.Data;

/**
 * A spell known by a custom party member, selected by type (e.g. "cure-wounds") with an
 * optional spell check bonus, advantage on the check, and an action priority.
 */

@Data
public class SpellConfig {

    private String type;

    private int priority = 1;

    private int spellCheckBonus = 0;

    private boolean advantage = false;

    public void validate() {
        if (type == null || type.isBlank()) {
            throw new IllegalArgumentException("Spell entry is missing a type");
        }
        if (priority < 1) {
            throw new IllegalArgumentException("Spell priority must be at least 1: " + priority);
        }
    }

}
