package com.redshift.ShadowDarkCalculator.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * A single party member selected by class; the loadout (stats, weapons, spells) comes
 * from the default archetype for that class.
 */

@Data
public class PartyMemberConfig {

    private String name;

    @JsonProperty("class")
    private String playerClass;

    private int level = 1;

    public void validate() {
        if (playerClass == null || playerClass.isBlank()) {
            throw new IllegalArgumentException("party member is missing a class");
        }
        if (level < 0) {
            throw new IllegalArgumentException("party member level cannot be negative: " + level);
        }
    }

}
