package com.redshift.ShadowDarkCalculator.party;

import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.dice.RandomAccess;
import com.redshift.ShadowDarkCalculator.party.generator.RandomPlayerFactory;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Builds a random party comprised of randomly generated players.
 */

@Slf4j
public class RandomLevel1PartyBuilder implements PartyBuilder {

    private final int playerCount;
    private long seed = new Random().nextLong();

    public RandomLevel1PartyBuilder() {
        this.playerCount = 4;
    }

    public RandomLevel1PartyBuilder(int playerCount) {
        this.playerCount = playerCount;
    }

    public RandomLevel1PartyBuilder(int playerCount, long seed) {
        this.playerCount = playerCount;
        this.seed = seed;
    }

    @Override
    public List<Creature> build() {
        RandomAccess.RANDOM.setSeed(seed);
        log.info("Generating Party: seed={}L \n", seed);

        final RandomPlayerFactory factory = new RandomPlayerFactory();

        final List<Creature> players = new ArrayList<>();

        for (int i = 1; i <= playerCount; i++) {
            players.add(factory.create());
        }

        // Make sure to reset the random number generator after each generation
        // or each combat will be identical too!
        RandomAccess.reset();

        return players;
    }

}
