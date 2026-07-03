package com.redshift.ShadowDarkCalculator.creatures;

import com.redshift.ShadowDarkCalculator.actions.Action;
import com.redshift.ShadowDarkCalculator.actions.DamageType;
import com.redshift.ShadowDarkCalculator.conditions.Condition;
import com.redshift.ShadowDarkCalculator.dice.Dice;
import com.redshift.ShadowDarkCalculator.encounter.Encounter;
import com.redshift.ShadowDarkCalculator.targets.SingleTargetSelector;

import java.util.List;
import java.util.Set;

/**
 * All players, monsters, etc. are creatures; with appropriate subclass.
 */

public interface Creature {

    /**
     * Add the specified condition to the character.
     */

    void addCondition(Condition condition);

    /**
     * Returns true if the creature can act.
     */

    boolean canAct();

    /**
     * Returns true if the creature can move.
     */

    boolean canMove();

    /**
     * Marks the creature as fled the fight and any other custom behavior.
     */

    void flee();

    /**
     * Returns the AC of the creature.
     */

    int getAC();

    /**
     * Gets the creatures action..
     */

    Action getAction();

    /**
     * Returns the condition if the creature has it.
     */

    Condition getCondition(String conditionName);

    /**
     * Returns the creatures current hit points.
     */

    int getCurrentHitPoints();

    /**
     * Returns the creatures name.
     */

    String getName();

    /**
     * Returns a list of labels for the creature.
     */

    Set<CreatureLabel> getLabels();

    /**
     * Returns the target selector strategy for enemies.
     */

    SingleTargetSelector getSingleTargetSelector();

    /**
     * Returns the level of the creature.
     */

    int getLevel();

    /**
     * Returns the creatures maximum hit points.
     */

    int getMaxHitPoints();

    /**
     * Returns the creatures stats.
     */

    Stats getStats();

    /**
     * Reports a status of the creature; unconscious, dead, alive, etc.
     */

    String getStatus();

    /**
     * Returns the amount of hit points from maximum.
     */

    int getWoundedAmount();

    /**
     * Gives the creature a luck token; to a maximum of 1.
     */

    void giveLuckToken();

    /**
     * Returns true if the creature already has the same type of condition.
     */

    boolean hasCondition(String conditionName);

    /**
     * Returns true if the creature has this SPECIFIC condition.
     */

    boolean hasCondition(Condition condition);

    /**
     * Returns true if the creature has failed a morale check and has fled the battlefield.
     */

    boolean hasFled();

    /**
     * Returns true if the creature has a luck token to spend.
     */

    boolean hasLuckToken();

    /**
     * Heals the target for the specified amount.
     */

    void healDamage(int hitPoints);

    /**
     * Returns true if the creature is bloodied (i.e. half health)
     */

    boolean isBloodied();

    /**
     * Returns true if dead... can't be healed, can't take actions, etc.
     */

    boolean isDead();

    /**
     * Returns true if the creature is unconscious.
     */

    boolean isUnconscious();

    /**
     * Returns true if the creature is less then full hit points.
     */

    boolean isWounded();

    /**
     * Removes the specified condition if the creature has it.
     */

    Condition removeCondition(String conditionName);

    /**
     * Returns a new initiative roll.
     */

    int rollInitiative();

    /**
     * Sets whether the creature is dead or not.
     */

    void setDead(boolean dead);

    /**
     * Sets the dice used when rolling death saves (Default D4).
     */

    void setDyingDice(Dice dyingDice);

    /**
     * Sets if this creature will flee because of morale checks.
     */

    void setWillFlee(boolean willFlee);

    /**
     * Spends the creatures luck token if they have one; throws an exception if they don't.
     */

    void spendLuckToken();

    /**
     * Instructs the creature to take an amount of damage  and various aspects of damage types.
     */

    void takeDamage(int amount, DamageType damageType);

    /**
     * Allows creature to take a pre-combat turn for special effects, etc.
     */

    void takePreCombatTurn(List<Creature> enemies, List<Creature> allies, Encounter encounter);

    /**
     * Triggers any begin of turn triggers... count down timers, etc.
     */

    void takeTurn(List<Creature> enemies, List<Creature> allies, Encounter encounter);

    /**
     * Returns true if this creature has morale checks and will flee combat.
     */

    boolean willFlee();
}
