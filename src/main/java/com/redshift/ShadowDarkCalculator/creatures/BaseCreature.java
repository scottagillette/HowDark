package com.redshift.ShadowDarkCalculator.creatures;

import com.redshift.ShadowDarkCalculator.actions.Action;
import com.redshift.ShadowDarkCalculator.conditions.*;
import com.redshift.ShadowDarkCalculator.encounter.Encounter;
import com.redshift.ShadowDarkCalculator.targets.SingleTargetSelector;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D20;
import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D4;

/**
 * All standard implementations of a creature; see subclasses for custom behavior.
 */

@Slf4j
public abstract class BaseCreature implements Creature {

    private final Action action;
    private final int armorClass;
    private final Map<String, Condition> conditions = new ConcurrentHashMap<>();
    private int currentHitPoints;
    private boolean dead = false;
    private final int hitPoints;
    private final int level;
    private final String name;
    private final SingleTargetSelector singleTargetSelector;
    private final Stats stats;
    private final Set<CreatureLabel> creatureLabels = new HashSet<>();

    /**
     * All argument constructor.
     */

    public BaseCreature(
            String name,
            int level,
            Stats stats,
            int armorClass,
            int hitPoints,
            Action action,
            SingleTargetSelector singleTargetSelector) {

        this.name = name;
        this.level = level;
        this.stats = stats;
        this.armorClass = armorClass;
        this.hitPoints = hitPoints;
        this.currentHitPoints = hitPoints;
        this.action = action;
        this.singleTargetSelector = singleTargetSelector;
    }

    @Override
    public void addCondition(Condition condition) {
        // Adding the same condition will replace the prior one... so check prior to adding and default to not add
        // if that makes sense... for example the undead that paralyze don't add it if the target already has it.
        if (!dead || condition.appliesToDeadCreatures()) {
            conditions.put(condition.getClass().getName(), condition);
        }
    }

    @Override
    public boolean canAct() {
        final List<Condition> cantActConditions = conditions.values()
                .stream()
                .filter(condition -> !condition.canAct())
                .toList();

        return cantActConditions.isEmpty() && !dead;
    }

    @Override
    public int getAC() {
        final List<Condition> cantActConditions = conditions.values()
                .stream()
                .filter(condition -> !condition.canAct())
                .toList();

        if (cantActConditions.isEmpty()) {
            final boolean hasShieldOfFaith = hasCondition(ShieldOfFaithCondition.class.getName());
            final boolean hasMageArmor = hasCondition(MageArmorCondition.class.getName());

            if (hasShieldOfFaith) {
                final ShieldOfFaithCondition condition = (ShieldOfFaithCondition) conditions.get(ShieldOfFaithCondition.class.getName());
                return armorClass + condition.getAcBonus();
            } else if (hasMageArmor) {
                final MageArmorCondition condition = (MageArmorCondition) conditions.get(MageArmorCondition.class.getName());
                return condition.getAC();
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
    public SingleTargetSelector getSingleTargetSelector() {
        return singleTargetSelector;
    }

    @Override
    public int rollInitiative() {
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
    public Set<CreatureLabel> getLabels() {
        return creatureLabels;
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
    public boolean hasCondition(Condition condition) {
        return (conditions.containsValue(condition));
    }

    @Override
    public void healDamage(int amount) {
        // No sense in healing the dead!
        if (!dead) {
            currentHitPoints = Math.min(hitPoints, currentHitPoints + amount);
            conditions.remove(DyingCondition.class.getName());
            conditions.remove(UnconsciousCondition.class.getName());
        }
    }

    @Override
    public boolean isBloodied() {
        return (currentHitPoints <=  hitPoints / 2);
    }

    @Override
    public boolean isDead() {
        return dead;
    }

    @Override
    public boolean isUnconscious() {
        return conditions.containsKey(UnconsciousCondition.class.getName());
    }

    @Override
    public boolean isWounded() {
        // Dead people are not wounded!
        return currentHitPoints != 0 && currentHitPoints < hitPoints;
    }

    @Override
    public Condition removeCondition(String conditionName) {
        return conditions.remove(conditionName);
    }

    @Override
    public void setDead(boolean dead) {
        this.dead = dead; // Some undead can come back!
        if (dead) {
            currentHitPoints = 0;
            conditions.clear(); // Your dead! ... not unconscious or paralyzed!
        } else {
            conditions.clear(); // Rise from the dead... have no conditions!
        }
    }

    @Override
    public void takeDamage(int amount, boolean silvered, boolean magical, boolean fire, boolean cold, boolean piercing) {
        // Damage awakens any creature sleeping or dazed!
        conditions.remove(SleepingCondition.class.getName());
        conditions.remove(DazedAndConfusedCondition.class.getName());

        // Check spell focus on damage or death
        final SpellFocusCondition spellFocusCondition = (SpellFocusCondition)conditions.get(SpellFocusCondition.class.getName());

        currentHitPoints = Math.max(0, currentHitPoints - amount);

        if (currentHitPoints == 0) {
            if (spellFocusCondition != null) {
                spellFocusCondition.end(); // Spell focus ends when you die or unconscious!
            }
            if (creatureLabels.contains(CreatureLabel.MONSTER)) {
                log.info("{} is dead!", name);
                setDead(true);
            } else {
                if (conditions.get(DyingCondition.class.getName()) == null) {
                    // Zero hp give them the unconscious and dying condition!
                    int deathRounds = D4.roll();
                    log.info("{} is unconscious and dying in {} rounds!", name, deathRounds);
                    conditions.put(UnconsciousCondition.class.getName(), new UnconsciousCondition());
                    conditions.put(DyingCondition.class.getName(), new DyingCondition(deathRounds));
                } else {
                    // Taking damage while dying makes you dead! Thx Dave!
                    log.info("{} is dead!", name);
                    setDead(true);
                }
            }
        } else {
            if (spellFocusCondition != null) {
                // Check on damage if the spell focus is lost or maintained.
                spellFocusCondition.hasEnded(this);
            }
        }
    }

    @Override
    public void takeTurn(List<Creature> enemies, List<Creature> allies, Encounter encounter) {
        if (dead) throw new IllegalStateException("Dead creatures can't take a turn.. they are dead!");

        // Check conditions and remove the ones that have ended.
        final List<Condition> remainingConditions = conditions.values().stream()
                .filter(condition -> !condition.hasEnded(this))
                .toList();

        conditions.clear();

        remainingConditions.forEach(condition -> conditions.put(condition.getClass().getName(), condition));

        // Have each condition perform its effect.
        conditions.values().forEach(condition -> condition.perform(this));

        if (canAct()) {
            getAction().perform(this, enemies, allies, encounter);
        }
    }
}
