package com.redshift.ShadowDarkCalculator.party;

import com.redshift.ShadowDarkCalculator.creatures.Creature;

import java.util.ArrayList;
import java.util.List;

public class MonsterPartyBuilder implements PartyBuilder {

    private final List<Creature> creatures = new ArrayList<>();

    @Override
    public PartyBuilder add(Creature creature) {
        creatures.add(creature);
        return this;
    }

    @Override
    public List<Creature> build() {
        return creatures;
    }
}
