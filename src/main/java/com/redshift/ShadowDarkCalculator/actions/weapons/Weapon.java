package com.redshift.ShadowDarkCalculator.actions.weapons;

import com.redshift.ShadowDarkCalculator.actions.Action;
import com.redshift.ShadowDarkCalculator.actions.BaseAction;
import com.redshift.ShadowDarkCalculator.actions.DamageType;
import com.redshift.ShadowDarkCalculator.conditions.DisadvantagedCondition;
import com.redshift.ShadowDarkCalculator.conditions.HolyWeaponCondition;
import com.redshift.ShadowDarkCalculator.conditions.RageCondition;
import com.redshift.ShadowDarkCalculator.dice.RollModifier;
import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.dice.Dice;
import com.redshift.ShadowDarkCalculator.dice.RollOutcome;
import com.redshift.ShadowDarkCalculator.encounter.Encounter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D20;
import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D4;

/**
 * An action that attempts to attack with a melee or ranged weapon.
 */

@Slf4j
public class Weapon extends BaseAction implements Action {

    protected final Dice damageDice;
    protected final RollModifier rollModifier;
    protected int attackRollBonus = 0;
    protected int damageRollBonus = 0;

    protected DamageType damageType = new DamageType();

    public Weapon(String name, Dice damageDice, RollModifier rollModifier) {
        super(name);
        this.damageDice = damageDice;
        this.rollModifier = rollModifier;
    }

    public Weapon addAcid() {
        this.damageType.addAcid();
        return this;
    }
    public Weapon addAttackRollBonus(int attackRollBonus) {
        this.attackRollBonus = this.attackRollBonus + attackRollBonus;
        return this;
    }

    public Weapon addCrushing() {
        damageType.addCrushing();
        return this;
    }

    public Weapon addDamageRollBonus(int damageRollBonus) {
        this.damageRollBonus = this.damageRollBonus + damageRollBonus;
        return this;
    }

    public Weapon addMagical() {
        damageType.addMagical();
        return this;
    }

    public Weapon addPiercing() {
        damageType.addPiercing();
        return this;
    }

    public Weapon addPoison() {
        damageType.addPoison();
        return this;
    }

    public Weapon addSilvered() {
        damageType.addSilvered();
        return this;
    }

    public Weapon addSlashing() {
        damageType.addSlashing();
        return this;
    }

    @Override
    public boolean canPerform(Creature actor, List<Creature> enemies, List<Creature> allies) {
        return true; // Melee and ranged attacks by default can always be performed.
    }

    /**
     * Returns the spell check roll... which does not include the spell check bonus if any.
     */

    protected int getAttackRoll(boolean disadvantaged) {
        if (disadvantaged) {
            return Math.min(D20.roll(), D20.roll());
        } else {
            return D20.roll();
        }
    }

    @Override
    public boolean isMagicalWeapon() {
        return damageType.isMagical();
    }

    @Override
    public void perform(Creature actor, List<Creature> enemies, List<Creature> allies, Encounter encounter) {
        // Normal weapons attack a single target... custom weapons can hit multiple (see performMultipleTargetAttack())
        final Creature target = actor.getSingleTargetSelector().get(enemies);

        if (target == null) {
            log.info("{} is skipping their turn... no target!", actor.getName());
        } else {
            performSingleTargetAttack(actor, target, getName(), damageDice, rollModifier);
        }
    }

    protected boolean performSingleTargetAttack(Creature actor, Creature target, String weaponName, Dice damageDice, RollModifier rollModifier) {
        boolean disadvantage = actor.hasCondition(DisadvantagedCondition.class.getName());
        actor.removeCondition(DisadvantagedCondition.class.getName());

        final int attackRoll = getAttackRoll(disadvantage);

        final boolean criticalSuccess = attackRoll == RollOutcome.CRITICAL_SUCCESS;
        final boolean criticalFailure = attackRoll == RollOutcome.CRITICAL_FAILURE;

        int attackRollModifier = 0;

        if (rollModifier.equals(RollModifier.STRENGTH)) {
            attackRollModifier = attackRollModifier + actor.getStats().getStrengthModifier();
        } else if (rollModifier.equals(RollModifier.DEXTERITY)) {
            attackRollModifier = attackRollModifier + actor.getStats().getDexterityModifier();
        } else {
            throw new IllegalStateException("Invalid roll modifier: " + rollModifier.getClass().getName());
        }

        int tempAttackRollBonus = attackRollBonus; // May get boosted up based on Holy Weapon
        int tempDamageRollBonus = damageRollBonus; // May get boosted up based on Holy Weapon
        final DamageType tempDamageType = damageType.copy();

        // Enable holy weapon attack bonus and damage bonus if the actor has this condition
        // AND this weapon isn't magical already.

        if (actor.hasCondition(HolyWeaponCondition.class.getName()) && !damageType.isMagical()) {
            tempAttackRollBonus = attackRollBonus + 1;
            tempDamageRollBonus = damageRollBonus + 1;
            tempDamageType.addMagical();
        }

        if (actor.hasCondition(RageCondition.class.getName())) {
            tempDamageRollBonus = damageRollBonus + D4.roll();
        }

        if (criticalFailure) {
            log.info("{} critically MISSES an attack on {} with a {}", actor.getName(), target.getName(), weaponName);
            return false;
        } else if (criticalSuccess) {
            int damage = damageDice.roll() + damageDice.roll() + tempDamageRollBonus;
            log.info("{} critically hits an attack on {} with a {}: damage={}", actor.getName(), target.getName(), weaponName, damage);
            target.takeDamage(damage, tempDamageType);
            return true;
        } else if (attackRoll + attackRollModifier + tempAttackRollBonus >= target.getAC()) {
            int damage = damageDice.roll() + tempDamageRollBonus;
            log.info("{} hits an attack on {} with a {}: damage={}", actor.getName(), target.getName(), weaponName, damage);
            target.takeDamage(damage, tempDamageType);
            return true;
        } else {
            // Miss
            log.info("{} MISSES the attack on {} with a {}", actor.getName(), target.getName(), weaponName);
            return false;
        }
    }

}
