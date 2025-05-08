package com.redshift.ShadowDarkCalculator.creatures;

import com.redshift.ShadowDarkCalculator.actions.Action;
import com.redshift.ShadowDarkCalculator.conditions.*;
import com.redshift.ShadowDarkCalculator.targets.RandomTargetSelector;
import com.redshift.ShadowDarkCalculator.targets.SingleTargetSelector;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D20;
import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D4;

@Slf4j
public abstract class BaseCreature implements Creature {

    private final Action action;
    private final int armorClass;
    private final Map<String, Condition> conditions = new ConcurrentHashMap<>();
    private int currentHitPoints;
    private boolean dead = false;
    private final int hitPoints;
    private final int level;
    private final boolean monster;
    private final String name;
    private final Stats stats;
    private final SingleTargetSelector targetSelector;
    private final boolean undead;

    /**
     * Simplified constructor; not undead, but a monster.
     */

    public BaseCreature(String name, int level, Stats stats, int armorClass, int hitPoints, Action action) {
        this(name, level, false, true, stats, armorClass, hitPoints, action, new RandomTargetSelector());
    }

    /**
     * All argument constructor.
     */

    public BaseCreature(String name, int level, boolean undead, boolean monster, Stats stats, int armorClass, int hitPoints, Action action, SingleTargetSelector targetSelector) {
        this.name = name;
        this.level = level;
        this.undead = undead;
        this.monster = monster;
        this.stats = stats;
        this.armorClass = armorClass;
        this.hitPoints = hitPoints;
        this.currentHitPoints = hitPoints;
        this.action = action;
        this.targetSelector = targetSelector;
    }

    @Override
    public void addCondition(Condition condition) {
        // Adding the same condition will replace the prior one... so check prior to adding and default to not add
        // if that makes sense... for example the undead that paralyze don't add it if the target already has it.
        conditions.put(condition.getClass().getName(), condition);
    }

    @Override
    public boolean canAct() {
        final List<Condition> cantActConditions = conditions.values().stream()
                .filter(condition -> !condition.canAct())
                .toList();

        return cantActConditions.isEmpty() && !dead;
    }

    @Override
    public int getAC() {
        final List<Condition> cantActConditions = conditions.values().stream()
                .filter(condition -> !condition.canAct())
                .toList();

        if (cantActConditions.isEmpty()) {
            final ShieldOfFaithCondition condition = (ShieldOfFaithCondition) conditions.get(ShieldOfFaithCondition.class.getName());

            if (condition != null) {
                return armorClass + condition.getAcBonus();
            } else {
                return armorClass;
            }
        } else {
            return 0; // No AC for creatures that can't act.
        }
    }

    @Override
    public Action getAction() {
        return action;
    }

    @Override
    public int getCurrentHitPoints() {
        return currentHitPoints;
    }

    @Override
    public SingleTargetSelector getTargetSelector() {
        return targetSelector;
    }

    @Override
    public int getInitiative() {
        return D20.roll() + stats.getDexterityModifier();
    }

    @Override
    public int getLevel() {
        return level;
    }

    @Override
    public int getMaxHitPoints() {
        return hitPoints;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Stats getStats() {
        return stats;
    }

    @Override
    public String getStatus() {
        if (dead) return "Dead";
        if (isUnconscious()) return "Unconscious";
        return "Alive";
    }

    @Override
    public int getWoundedAmount() {
        return hitPoints - currentHitPoints;
    }

    @Override
    public boolean hasCondition(String conditionName) {
        return (conditions.get(conditionName) != null);
    }

    @Override
    public void healDamage(int amount) {
        // No sense in healing the dead!
        if (!dead) {
            currentHitPoints = Math.min(hitPoints, currentHitPoints + amount);
            conditions.remove(DyingCondition.class.getName());
            conditions.remove(UnconciousCondition.class.getName());
        }
    }

    @Override
    public boolean isDead() {
        return dead;
    }

    @Override
    public boolean isUnconscious() {
        return conditions.containsKey(UnconciousCondition.class.getName());
    }

    @Override
    public boolean isUndead() {
        return undead;
    }

    @Override
    public boolean isWounded() {
        // Dead people are not wounded!
        return currentHitPoints != 0 && currentHitPoints < hitPoints;
    }

    @Override
    public void setDead(boolean dead) {
        this.dead = dead; // Some undead can come back!
        if (dead) {
            currentHitPoints = 0;
            conditions.clear(); // Your dead! ... not unconscious!
        } else {
            conditions.clear();
        }
    }

    @Override
    public void takeDamage(int amount, boolean silvered, boolean magical, boolean fire, boolean cold) {
        // Damage awakens any creature!
        conditions.remove(SleepingCondition.class.getName());

        currentHitPoints = Math.max(0, currentHitPoints - amount);

        if (currentHitPoints == 0) {
            if (monster) {
                // Zero hp give them the unconscious condition!
                log.info(name + " is dead!");
                conditions.clear();
                dead = true;
            } else {
                conditions.put(UnconciousCondition.class.getName(), new UnconciousCondition());

                // Don't reset dying condition if they are already dying!
                if (conditions.get(DyingCondition.class.getName()) == null) {
                    // Zero hp give them the unconscious and dying condition!
                    int deathRounds = D4.roll();
                    log.info(name + " is unconscious and dying in " + deathRounds +" rounds!");
                    conditions.put(DyingCondition.class.getName(), new DyingCondition(deathRounds));
                }
            }
        }
    }

    @Override
    public void takeTurn(List<Creature> enemies, List<Creature> allies) {
        // Check conditions and remove the ones that have ended.
        final List<Condition> remainingConditions = conditions.values().stream().
                filter(condition -> !condition.hasEnded(this)).toList();

        conditions.clear();

        remainingConditions.forEach(condition -> conditions.put(condition.getClass().getName(), condition));

        // Have each condition perform its effect.
        conditions.values().forEach(condition -> condition.perform(this));

        if (canAct()) {
            getAction().perform(this, enemies, allies);
        }
    }
}
