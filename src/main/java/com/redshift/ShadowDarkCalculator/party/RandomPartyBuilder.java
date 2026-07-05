package com.redshift.ShadowDarkCalculator.party;

import com.redshift.ShadowDarkCalculator.creatures.Creature;

import java.util.ArrayList;
import java.util.List;

/**
 * Builds a random party comprised of randomly generated players.
 */

public class RandomPartyBuilder implements PartyBuilder {

    private final int playerCount;

    public RandomPartyBuilder() {
        this.playerCount = 4;
    }

    public RandomPartyBuilder(int playerCount) {
        this.playerCount = playerCount;
    }

    @Override
    public List<Creature> build() {
        final RandomPlayerFactory factory = new RandomPlayerFactory();

        final List<Creature> players = new ArrayList<>();

        for (int i = 1; i <= playerCount; i++) {
            players.add(factory.create());
        }

        return players;
    }

}
