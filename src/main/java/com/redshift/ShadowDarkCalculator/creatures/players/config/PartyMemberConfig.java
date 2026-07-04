package com.redshift.ShadowDarkCalculator.creatures.players.config;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * A single party member; all configuration required and validated.
 */

@Data
public class PartyMemberConfig {

    private String name;

    @JsonProperty("class")
    private String playerClass;

    private int level = 1;

    private StatsConfig stats;

    @JsonAlias("ac")
    private Integer armorClass;

    @JsonAlias("hp")
    private Integer hitPoints;

    private boolean luckToken = false;

    private int healingPotions = 0;

    private List<WeaponConfig> weapons = new ArrayList<>();

    private List<SpellConfig> spells = new ArrayList<>();

    public void validate() {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("Party member is missing a name.");
        }

        if (playerClass == null || playerClass.isBlank()) {
            throw new IllegalArgumentException("Party member is missing a class.");
        }

        if (level < 0 || level > 10) {
            throw new IllegalArgumentException("Party member level cannot be negative: " + level);
        }

        if (armorClass == null || armorClass < 1 || armorClass > 20) {
            throw new IllegalArgumentException("Party member armorClass must be at least 1 and less than 21: " + armorClass);
        }

        if (hitPoints == null || hitPoints < 1 || hitPoints > 100) {
            throw new IllegalArgumentException("Party member hitPoints must be at least 1: " + hitPoints);
        }

        if (stats == null) {
            throw new IllegalArgumentException("Player stats must be specified.");
        }
        stats.validate();

        if (weapons == null && spells == null) {
            throw new IllegalArgumentException("Player must define weapons and/or spells.");
        }

        if (weapons != null) {
            weapons.forEach(WeaponConfig::validate);
        }

        if (spells != null) {
            spells.forEach(SpellConfig::validate);
        }
    }

}
