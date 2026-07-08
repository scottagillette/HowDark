package com.redshift.ShadowDarkCalculator.actions.weapons;

import com.redshift.ShadowDarkCalculator.actions.Action;
import com.redshift.ShadowDarkCalculator.actions.BaseAction;
import com.redshift.ShadowDarkCalculator.actions.DamageType;
import com.redshift.ShadowDarkCalculator.conditions.*;
import com.redshift.ShadowDarkCalculator.creatures.CreatureLabel;
import com.redshift.ShadowDarkCalculator.dice.RollModifier;
import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.dice.Dice;
import com.redshift.ShadowDarkCalculator.dice.RollOutcome;
import com.redshift.ShadowDarkCalculator.encounter.Encounter;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.*;

/**
 * An action that attempts to attack with a melee or ranged weapon.
 */

@Getter
@Slf4j
public class Weapon extends BaseAction implements Action {

    protected final Dice damageDice;
    protected final RollModifier rollModifier;
    protected int attackRollBonus = 0;
    protected int damageRollBonus = 0;
    protected DamageType damageType = new DamageType();
    protected boolean destroyed;

    public Weapon(String name, Dice damageDice, RollModifier rollModifier) {
        super(name);
        this.damageDice = damageDice;
        this.rollModifier = rollModifier;
    }

    public final Weapon addAcid() {
        this.damageType.addAcid();
        return this;
    }

    public final Weapon addAttackRollBonus(int attackRollBonus) {
        this.attackRollBonus = this.attackRollBonus + attackRollBonus;
        return this;
    }

    public final Weapon addCrushing() {
        damageType.addCrushing();
        return this;
    }

    public final Weapon addDamageRollBonus(int damageRollBonus) {
        this.damageRollBonus = this.damageRollBonus + damageRollBonus;
        return this;
    }

    public final Weapon addFire() {
        damageType.addFire();
        return this;
    }

    public final Weapon addMagical() {
        damageType.addMagical();
        return this;
    }

    public final Weapon addPiercing() {
        damageType.addPiercing();
        return this;
    }

    public final Weapon addPoison() {
        damageType.addPoison();
        return this;
    }

    public final Weapon addSilvered() {
        damageType.addSilvered();
        return this;
    }

    public final Weapon addSlashing() {
        damageType.addSlashing();
        return this;
    }

    private boolean checkAdvantage(Creature actor, Creature target) {
        // Check for advantage condition and then remove it.
        boolean advantage = actor.hasCondition(AdvantageCondition.class.getName());
        actor.removeCondition(AdvantageCondition.class.getName());

        // Advantage on attack if the target creature can't act; unconscious, paralyzed, etc.
        if (!target.canAct()) {
            advantage = true;
        } else if (target.hasCondition(BlindedCondition.class.getName())) {
            advantage = true;
        }

        return advantage;
    }

    private boolean checkDisadvantage(Creature actor, Creature target) {
        // Check for disadvantage condition and then remove it.
        boolean disadvantage = actor.hasCondition(DisadvantagedCondition.class.getName());
        actor.removeCondition(DisadvantagedCondition.class.getName());

        // Check for protection from evil giving disadvantage.
        if (target.hasCondition(ProtectionFromEvilCondition.class.getName())) {
            if (!disadvantage && actor.getLabels().contains(CreatureLabel.CHAOTIC)) {
                disadvantage = true;
                log.info("{} has disadvantage attacking {} because of protection from evil!", actor.getName(), target.getName());
            }
        }

        // Check for the Disadvantage To Attack Condition.
        if (target.hasCondition(DisadvantageToAttackCondition.class.getName())) {
            disadvantage = true;
            log.info("{} has disadvantage attacking {} ", actor.getName(), target.getName());
        }

        return disadvantage;
    }

    @Override
    public final boolean canPerform(Creature actor, List<Creature> enemies, List<Creature> allies) {
        return !destroyed; // Sometimes weapons can be broken, melted, destroyed, etc.
    }

    private void checkWeaponBreaks(Creature actor, Creature target, boolean magical) {
        if (target.hasCondition(CosticToWeaponsCondition.class.getName()) && !magical) {
            int roll = D6.roll();

            if (roll == 1) {
                log.info("{} has their weapon break!", actor.getName());
                destroyed = true;
            }
        }
    }

    /**
     * Returns the attack roll; using the luck token if can and needed.
     */

    private int getAttackRoll(Creature actor, Creature target, int rollBonus, int armorClass) {
        boolean disadvantage = checkDisadvantage(actor, target);
        boolean advantage = checkAdvantage(actor, target);

        // Check to see if they negate.
        if (disadvantage && advantage) {
            disadvantage = false;
            advantage = false;
        }

        int d20Roll = D20.roll();

        if (advantage) {
            d20Roll = Math.max(D20.roll(), D20.roll());
        } else if (disadvantage) {
            d20Roll = Math.min(D20.roll(), D20.roll());
        } else if (d20Roll == RollOutcome.CRITICAL_SUCCESS) {
            return d20Roll; // 20 is always success!
        }

        if (d20Roll + rollBonus >= armorClass && d20Roll != RollOutcome.CRITICAL_FAILURE) {
            return d20Roll; // Return if it's a successful attack roll!
        } else {
            // Re-roll with luck token if they have it.
            if (actor.hasLuckToken()) {
                log.info("{} uses their luck token to re-roll!", actor.getName());
                actor.spendLuckToken();
                return getAttackRoll(actor, target, rollBonus, armorClass); // Can't be disadvantaged on a luck re-roll is my guess.
            }
        }

        return d20Roll; // Return the raw result.
    }

    @Override
    public final boolean isMagicalWeapon() {
        return damageType.isMagical();
    }

    @Override
    public void perform(Creature actor, List<Creature> enemies, List<Creature> allies, Encounter encounter) {
        // Normal weapons attack a single target... custom weapons can hit multiple (see performMultipleTargetAttack())
        final Creature target = actor.getSingleTargetSelector().get(enemies);

        if (target == null) {
            log.info("{} is skipping their turn... no target!", actor.getName());
        } else {
            performSingleTargetAttack(actor, target);
        }
    }

    protected boolean performSingleTargetAttack(Creature actor, Creature target) {
        // Check for any attack roll and damage roll bonuses like Holy Weapon
        int tempAttackRollBonus = attackRollBonus;
        int tempDamageRollBonus = damageRollBonus;
        final DamageType tempDamageType = damageType.copy();

        if (actor.hasCondition(HolyWeaponCondition.class.getName()) && !damageType.isMagical()) {
            final HolyWeaponCondition holyWeaponCondition = (HolyWeaponCondition) actor.getCondition(HolyWeaponCondition.class.getName());
            tempAttackRollBonus += holyWeaponCondition.getBonus();
            tempDamageRollBonus += holyWeaponCondition.getBonus();
            tempDamageType.addMagical();
        }

        // Add extra damage if they have the Rage condition!
        if (actor.hasCondition(RageCondition.class.getName())) {
            tempDamageRollBonus += D4.roll();
        }

        // Add any extra damage from special conditions.
        if (actor.hasCondition(ExtraDamageDiceCondition.class.getName())) {
            ExtraDamageDiceCondition extraDamageDiceCondition = (ExtraDamageDiceCondition) actor.getCondition(ExtraDamageDiceCondition.class.getName());
            tempDamageRollBonus += extraDamageDiceCondition.getExtraDamage(damageDice);
            actor.removeCondition((ExtraDamageDiceCondition.class.getName())); // Single use.
        }

        // Calculate the attack roll modifier based on STR or DEX only.
        int attackRollModifier = 0;

        if (rollModifier.equals(RollModifier.STRENGTH)) {
            attackRollModifier = actor.getStats().getStrengthModifier();
        } else if (rollModifier.equals(RollModifier.DEXTERITY)) {
            attackRollModifier = actor.getStats().getDexterityModifier();
        }

        // Roll the attack!

        int d20Result = getAttackRoll(actor, target, attackRollModifier + tempAttackRollBonus, target.getAC());

        final boolean criticalSuccess = d20Result == RollOutcome.CRITICAL_SUCCESS;
        final boolean criticalFailure = d20Result == RollOutcome.CRITICAL_FAILURE;

        if (criticalFailure) {
            log.info("{} critically MISSES an attack on {} with a {}", actor.getName(), target.getName(), name);
            return false;
        } else if (criticalSuccess) {
            int damage = damageDice.roll() + damageDice.roll() + tempDamageRollBonus;
            log.info("{} critically hits an attack on {} with a {} for {} damage", actor.getName(), target.getName(), name, damage);
            target.takeDamage(damage, tempDamageType);
            checkWeaponBreaks(actor, target, tempDamageType.isMagical());
            return true;
        } else if (d20Result + attackRollModifier + tempAttackRollBonus >= target.getAC()) {
            int damage = damageDice.roll() + tempDamageRollBonus;
            log.info("{} hits an attack on {} with a {} for {} damage", actor.getName(), target.getName(), name, damage);
            target.takeDamage(damage, tempDamageType);
            checkWeaponBreaks(actor, target, tempDamageType.isMagical());
            return true;
        } else {
            log.info("{} MISSES the attack on {} with a {}", actor.getName(), target.getName(), name);
            return false;
        }
    }

    @Override
    public String toString() {
        return getName();
    }
}
