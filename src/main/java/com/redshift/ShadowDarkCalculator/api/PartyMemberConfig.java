package com.redshift.ShadowDarkCalculator.api;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * A single party member selected by class. By default the loadout (stats, armor, weapons
 * and spells) comes from the default archetype for that class, but every part of the build
 * can be overridden: stats, armor class, hit points, a luck token, healing potions, and the
 * exact weapons and spells carried. When weapons, spells or potions are listed they replace
 * the class default loadout entirely.
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

    /**
     * True when the member lists its own weapons, spells or potions; the custom loadout
     * replaces the class default actions.
     */
    public boolean hasCustomLoadout() {
        return (weapons != null && !weapons.isEmpty())
                || (spells != null && !spells.isEmpty())
                || healingPotions > 0;
    }

    public void validate() {
        if (playerClass == null || playerClass.isBlank()) {
            throw new IllegalArgumentException("party member is missing a class");
        }
        if (level < 0) {
            throw new IllegalArgumentException("party member level cannot be negative: " + level);
        }
        if (armorClass != null && armorClass < 1) {
            throw new IllegalArgumentException("party member armorClass must be at least 1: " + armorClass);
        }
        if (hitPoints != null && hitPoints < 1) {
            throw new IllegalArgumentException("party member hitPoints must be at least 1: " + hitPoints);
        }
        if (healingPotions < 0) {
            throw new IllegalArgumentException("party member healingPotions cannot be negative: " + healingPotions);
        }
        if (stats != null) {
            stats.validate();
        }
        if (weapons != null) {
            weapons.forEach(WeaponConfig::validate);
        }
        if (spells != null) {
            spells.forEach(SpellConfig::validate);
        }
    }

}
