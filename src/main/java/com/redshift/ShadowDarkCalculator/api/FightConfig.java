package com.redshift.ShadowDarkCalculator.api;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Root configuration for a fight, typically loaded from YAML.
 */

@Data
public class FightConfig {

    private int simulations = 1;

    private List<PartyMemberConfig> party = new ArrayList<>();

    private List<MonsterConfig> monsters = new ArrayList<>();

    public void validate() {
        if (simulations < 1) {
            throw new IllegalArgumentException("simulations must be at least 1");
        }
        if (party == null || party.isEmpty()) {
            throw new IllegalArgumentException("party must contain at least one member");
        }
        if (monsters == null || monsters.isEmpty()) {
            throw new IllegalArgumentException("monsters must contain at least one entry");
        }
        party.forEach(PartyMemberConfig::validate);
        monsters.forEach(MonsterConfig::validate);
    }

}
