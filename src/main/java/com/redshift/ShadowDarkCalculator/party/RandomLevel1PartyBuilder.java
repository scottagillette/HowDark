package com.redshift.ShadowDarkCalculator.party;

import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.party.loadout.RandomPlayerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Builds a random party comprised of randomly generated players.
 */

public class RandomLevel1PartyBuilder implements PartyBuilder {

    private final int playerCount;

    public RandomLevel1PartyBuilder() {
        this.playerCount = 4;
    }

    public RandomLevel1PartyBuilder(int playerCount) {
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
