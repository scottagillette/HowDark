package com.redshift.ShadowDarkCalculator.creatures.players.config;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PartyConfig {

    private List<PartyMemberConfig> party = new ArrayList<>();

    public void validate() {
        if (party == null || party.isEmpty()) {
            throw new IllegalArgumentException("Party config is not defined.");
        }
    }
}
