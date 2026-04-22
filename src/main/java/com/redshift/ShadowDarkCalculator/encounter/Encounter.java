package com.redshift.ShadowDarkCalculator.encounter;

import com.redshift.ShadowDarkCalculator.creatures.Creature;

/**
 * An interface describing a specific encounter and ways creatures, spells and weapons might interact with the encounter.
 */

public interface Encounter {

    /**
     * Adds a friendly creatures to the encounter.
     *
     * @param actor
     *      a creature that the new creature is friendly to.
     * @param newCreature
     *      the new creature to add to the encounter. New creatures get added to the end of the initiative order.
     */

    void addFriendlyCreature(Creature actor, Creature newCreature);
}
