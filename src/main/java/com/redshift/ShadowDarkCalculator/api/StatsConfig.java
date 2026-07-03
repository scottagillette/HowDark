package com.redshift.ShadowDarkCalculator.api;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.redshift.ShadowDarkCalculator.creatures.Stats;
import lombok.Data;

/**
 * A full stat block for a custom party member. When present all six stats are required,
 * so a partially specified block can't silently mix custom and default values.
 * Short forms (str, dex, con, int, wis, cha) are accepted as aliases.
 */

@Data
public class StatsConfig {

    @JsonAlias("str")
    private Integer strength;

    @JsonAlias("dex")
    private Integer dexterity;

    @JsonAlias("con")
    private Integer constitution;

    @JsonAlias("int")
    private Integer intelligence;

    @JsonAlias("wis")
    private Integer wisdom;

    @JsonAlias("cha")
    private Integer charisma;

    public Stats toStats() {
        return new Stats(strength, dexterity, constitution, intelligence, wisdom, charisma);
    }

    public void validate() {
        requireStat("strength", strength);
        requireStat("dexterity", dexterity);
        requireStat("constitution", constitution);
        requireStat("intelligence", intelligence);
        requireStat("wisdom", wisdom);
        requireStat("charisma", charisma);
    }

    private void requireStat(String name, Integer value) {
        if (value == null) {
            throw new IllegalArgumentException("stats block is missing " + name + " (all six stats are required)");
        }
        if (value < 1 || value > 30) {
            throw new IllegalArgumentException(name + " must be between 1 and 30: " + value);
        }
    }

}
