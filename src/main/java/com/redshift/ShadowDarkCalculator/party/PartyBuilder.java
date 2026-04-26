package com.redshift.ShadowDarkCalculator.party;

import com.redshift.ShadowDarkCalculator.creatures.Creature;

import java.util.List;

/**
 * A builder that can create a party to compete with.
 */

public interface PartyBuilder {

    /**
     * Build the party!
     */

    List<Creature> build();

}
