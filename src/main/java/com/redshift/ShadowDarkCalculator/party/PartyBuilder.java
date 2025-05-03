package com.redshift.ShadowDarkCalculator.party;

import com.redshift.ShadowDarkCalculator.creatures.Creature;

import java.util.List;

/**
 * A builder that can create a party to compete with.
 */

public interface PartyBuilder {

    /**
     * Fluent method to add a creature and return the builder.
     */

    PartyBuilder addMember(Creature creature);

    /**
     * Build the party!
     */

    List<Creature> build();

}
