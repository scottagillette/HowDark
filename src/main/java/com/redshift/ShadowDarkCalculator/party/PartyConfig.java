package com.redshift.ShadowDarkCalculator.party;

import com.redshift.ShadowDarkCalculator.creatures.players.PlayerConfig;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PartyConfig {

    private List<PlayerConfig> party = new ArrayList<>();

    public void validate() {
        if (party == null || party.isEmpty()) {
            throw new IllegalArgumentException("Party config is not defined.");
        }

        for (PlayerConfig config : party) {
            config.validate();
        }
    }
}
