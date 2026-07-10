package com.redshift.ShadowDarkCalculator.fights;

import com.redshift.ShadowDarkCalculator.creatures.monsters.MonsterConfig;
import com.redshift.ShadowDarkCalculator.creatures.players.PlayerConfig;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Root configuration for a fight, typically loaded from YAML.
 *
 * The party is chosen one of two ways: name a prebuilt, hand-tuned party via
 * {@code partyBuilder}, or list members individually under {@code party}. Exactly one of
 * the two must be provided.
 */

@Data
public class FightConfig {

    private int simulations = 1;

    private String partyBuilder;

    private List<PlayerConfig> party = new ArrayList<>();

    private List<MonsterConfig> monsters = new ArrayList<>();

    public boolean usesPrebuiltParty() {
        return partyBuilder != null && !partyBuilder.isBlank();
    }

    public void validate() {
        if (simulations < 1) {
            throw new IllegalArgumentException("simulations must be at least 1");
        }

        final boolean hasList = party != null && !party.isEmpty();

        if (usesPrebuiltParty() && hasList) {
            throw new IllegalArgumentException("specify either partyBuilder or party, not both");
        }
        if (!usesPrebuiltParty() && !hasList) {
            throw new IllegalArgumentException("must specify either a partyBuilder or a non-empty party");
        }

        if (monsters == null || monsters.isEmpty()) {
            throw new IllegalArgumentException("monsters must contain at least one entry");
        }

        if (hasList) {
            party.forEach(PlayerConfig::validate);
        }
        monsters.forEach(MonsterConfig::validate);
    }

}
