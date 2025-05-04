package com.redshift.ShadowDarkCalculator.actions;

import com.redshift.ShadowDarkCalculator.creatures.Creature;

import java.util.List;

/**
 * Something creatures are able to do on their turn... cast a spell, attack with a weapon, etc.
 */

public interface Action {

    /**
     * Returns true if this action can (or should) be performed. (i.e. don't heal if no one is hurt)
     */

    boolean canPerform(Creature actor, List<Creature> enemies, List<Creature> allies);

    /**
     * Returns the name of the action.
     */

    String getName();

    /**
     * Returns true if the action cannot be used. (i.e. spells that are lost)
     */

    boolean isLost();

    /**
     * Returns true if the action is magical.
     */

    boolean isMagical();

    /**
     * Performs the specific action.
     */

    void perform(Creature actor, List<Creature> enemies, List<Creature> allies);

}
