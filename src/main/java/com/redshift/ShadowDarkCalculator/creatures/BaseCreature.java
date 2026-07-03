package com.redshift.ShadowDarkCalculator.creatures;

import com.redshift.ShadowDarkCalculator.actions.Action;
import com.redshift.ShadowDarkCalculator.actions.DamageType;
import com.redshift.ShadowDarkCalculator.conditions.*;
import com.redshift.ShadowDarkCalculator.dice.Dice;
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
    private boolean hasLuckToken = false;
    private final int hitPoints;
    private final int level;
    private final String name;
    private final SingleTargetSelector singleTargetSelector;
    private final Stats stats;
    private final Set<CreatureLabel> creatureLabels = new HashSet<>();
    private Dice dyingDice = D4;
    private boolean fled = false;
    private boolean willFlee = true; // Default all creatures will flee.

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
        this.hitPoints = Math.max(hitPoints, 1); // Some creature roll negative CON.
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
        final List<Condition> cantActConditions = conditions.values().stream()
                .filter(condition -> !condition.canAct())
                .toList();

        return cantActConditions.isEmpty() && !dead;
    }

    @Override
    public void flee() {
        fled = true;
        log.info("{} has failed a morale check and has fled the battle field!", name);
        // No other custom behavior.
    }

    @Override
    public int getAC() {
        int finalArmorClass = armorClass; // Base armor class of creature, DEX mod, armor, shield.

        final boolean hasMageArmor = hasCondition(MageArmorCondition.class.getName());
        if (hasMageArmor) {
            final MageArmorCondition condition = (MageArmorCondition) conditions.get(MageArmorCondition.class.getName());
            finalArmorClass = condition.getAC();
        }

        final boolean hasShieldOfFaith = hasCondition(ShieldOfFaithCondition.class.getName());
        if (hasShieldOfFaith) {
            final ShieldOfFaithCondition condition = (ShieldOfFaithCondition) conditions.get(ShieldOfFaithCondition.class.getName());
            finalArmorClass += condition.getAcBonus();
        }

        return finalArmorClass;
    }

    @Override
    public Action getAction() {
        return action;
    }

    @Override
    public Condition getCondition(String conditionName) {
        return conditions.get(conditionName);
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
        if (hasFled()) return "Fled";
        return "Alive";
    }

    @Override
    public int getWoundedAmount() {
        return hitPoints - currentHitPoints;
    }

    @Override
    public void giveLuckToken() {
        hasLuckToken = true;
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
    public boolean hasFled() {
        return fled;
    }

    @Override
    public boolean hasLuckToken() {
        return hasLuckToken;
    }

    @Override
    public void healDamage(int amount) {
        // No sense in healing the dead!
        if (!dead) {
            if (this.hasCondition(DiseasedCondition.class.getName())) {
                log.info("{} is diseased and cannot be healed.", this.getName());
            } else {
                currentHitPoints = Math.min(hitPoints, currentHitPoints + amount);
                conditions.remove(DyingCondition.class.getName());
                conditions.remove(UnconsciousCondition.class.getName());
            }
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
    public int rollInitiative() {
        return D20.roll() + stats.getDexterityModifier();
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
    public void setDyingDice(Dice dice) {
        this.dyingDice = dice;
    }

    @Override
    public void setWillFlee(boolean willFlee) {
        this.willFlee = willFlee;
    }

    @Override
    public void spendLuckToken() {
        if (!hasLuckToken) throw new IllegalStateException("No luck token!");
        hasLuckToken = false;
    }

    @Override
    public void takeDamage(int amount, DamageType damageType) {
        // Damage awakens any creature sleeping or dazed!
        conditions.remove(SleepingCondition.class.getName());
        conditions.remove(StupefiedCondition.class.getName());

        // Check spell focus on damage or death
        final SpellFocusCondition spellFocusCondition = (SpellFocusCondition)conditions.get(SpellFocusCondition.class.getName());

        currentHitPoints = Math.max(0, currentHitPoints - amount);

        if (currentHitPoints == 0) {
            if (spellFocusCondition != null) {
                spellFocusCondition.end(); // Spell focus ends when you die or unconscious!
                conditions.remove(SpellFocusCondition.class.getName());
            }
            if (creatureLabels.contains(CreatureLabel.MONSTER)) {
                log.info("{} is dead!", name);
                setDead(true);
            } else {
                if (conditions.get(DyingCondition.class.getName()) == null) {
                    // Death time D4 + CON mod (min 1)
                    int deathRounds = Math.max(dyingDice.roll() + getStats().getConstitutionModifier(), 1);

                    // Zero hp give them the unconscious and dying condition!
                    conditions.put(UnconsciousCondition.class.getName(), new UnconsciousCondition());
                    conditions.put(DyingCondition.class.getName(), new DyingCondition(deathRounds));

                    log.info("{} is unconscious and dying in {} rounds!", name, deathRounds);
                } else {
                    // Taking damage while dying makes you dead! Thx Dave!
                    log.info("{} is dead!", name);
                    setDead(true);
                }
            }
        } else {
            if (spellFocusCondition != null) {
                // Check on damage if the spell focus is lost or maintained.
                if (spellFocusCondition.hasEnded(this)) {
                    spellFocusCondition.end();
                    conditions.remove(SpellFocusCondition.class.getName());
                }
            }
        }
    }

    @Override
    public void takePreCombatTurn(List<Creature> enemies, List<Creature> allies, Encounter encounter) {
        // No default pre-combat action; only specific classes or monsters have them.
    }

    @Override
    public void takeTurn(List<Creature> enemies, List<Creature> allies, Encounter encounter) {
        if (dead) {
            throw new IllegalStateException("Dead creatures can't take a turn.. they are dead!");
        }

        // Check conditions and remove the ones that have ended.
        final List<Condition> remainingConditions = conditions.values().stream()
                .filter(condition -> !condition.hasEnded(this))
                .toList();

        conditions.clear();

        remainingConditions.forEach(condition -> conditions.put(condition.getClass().getName(), condition));

        // Have each condition perform its effect.
        conditions.values().forEach(condition -> condition.perform(this));

        try {
            Thread.sleep(encounter.getDelay() * 1000L);
        } catch (InterruptedException ignored) {
            // Nothing
        }

        if (canAct()) {
            final Action action = getAction();
            if (action.canPerform(this, enemies, allies)) {
                action.perform(this, enemies, allies, encounter);
            } else {
                log.info("{} has no action to perform!", name);
            }
        }
    }

    @Override
    public boolean willFlee() {
        return willFlee;
    }

}
