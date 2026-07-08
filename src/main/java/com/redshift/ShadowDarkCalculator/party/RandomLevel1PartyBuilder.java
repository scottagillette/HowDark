package com.redshift.ShadowDarkCalculator.party;

import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.dice.RandomAccess;
import com.redshift.ShadowDarkCalculator.party.loadout.RandomPlayerFactory;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * Builds a random party comprised of randomly generated players.
 */

@Slf4j
public class RandomLevel1PartyBuilder implements PartyBuilder {

    private final int playerCount;
    private final long seed = System.nanoTime();

    public RandomLevel1PartyBuilder() {
        this.playerCount = 4;
        RandomAccess.RANDOM.setSeed(seed);
    }

    public RandomLevel1PartyBuilder(int playerCount) {
        this.playerCount = playerCount;
        RandomAccess.RANDOM.setSeed(seed);
    }

    public RandomLevel1PartyBuilder(int playerCount, long seed) {
        this.playerCount = playerCount;
        RandomAccess.RANDOM.setSeed(seed);
    }

    @Override
    public List<Creature> build() {
        log.info("Generating Party: seed={}L \n", seed);

        final RandomPlayerFactory factory = new RandomPlayerFactory();

        final List<Creature> players = new ArrayList<>();

        for (int i = 1; i <= playerCount; i++) {
            players.add(factory.create());
        }

        return players;
    }

}
