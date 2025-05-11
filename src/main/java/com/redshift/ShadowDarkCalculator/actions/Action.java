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
     * Returns the priority of an action. When there are multiple actions but a creature can take only one each actions
     * priority will be retrieved weighted by their priority. A random number drawn that totals all actions priority
     * then selection occurs based on that number. So for example a Wizard with three spells could prioritize the first
     * one 2 and the other two 1... for a total of 4. A random D4 is rolled... 1 or 2 the first action, 3 the second,
     * 4 the third. Thus giving the first spell a 50% chance of being selected out of three spells.
     */

    int getPriority();

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

    /**
     * Sets the priority of the action; default 1.
     */

    Action setPriority(int priority);
}
