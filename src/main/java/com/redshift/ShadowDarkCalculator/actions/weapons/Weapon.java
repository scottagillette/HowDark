package com.redshift.ShadowDarkCalculator.actions.weapons;

import com.redshift.ShadowDarkCalculator.actions.Action;
import com.redshift.ShadowDarkCalculator.actions.BaseAction;
import com.redshift.ShadowDarkCalculator.conditions.HolyWeaponCondition;
import com.redshift.ShadowDarkCalculator.dice.RollModifier;
import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.dice.Dice;
import com.redshift.ShadowDarkCalculator.dice.RollOutcome;
import com.redshift.ShadowDarkCalculator.encounter.Encounter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D20;

/**
 * An action that attempts to attack with a melee or ranged weapon.
 */

@Slf4j
public class Weapon extends BaseAction implements Action {

    protected final Dice damageDice;
    protected final RollModifier rollModifier;
    protected int attackRollBonus = 0;
    protected int damageRollBonus = 0;
    protected boolean magical;
    protected boolean silvered;

    public Weapon(String name, Dice damageDice, RollModifier rollModifier) {
        super(name);
        this.damageDice = damageDice;
        this.rollModifier = rollModifier;
    }

    public Weapon addAttackRollBonus(int attackRollBonus) {
        this.attackRollBonus = this.attackRollBonus + attackRollBonus;
        return this;
    }

    public Weapon addDamageRollBonus(int damageRollBonus) {
        this.damageRollBonus = this.damageRollBonus + damageRollBonus;
        return this;
    }

    public Weapon addMagical() {
        this.magical = true;
        return this;
    }

    public Weapon addSilvered() {
        this.silvered = true;
        return this;
    }

    @Override
    public boolean canPerform(Creature actor, List<Creature> enemies, List<Creature> allies) {
        return true; // Melee and ranged attacks by default can always be performed.
    }

    @Override
    public boolean isMagicalWeapon() {
        return magical;
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
        final int attackRoll = D20.roll();

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

        int tempDamageRollBonus = damageRollBonus; // May get boosted up based on Holy Weapon
        boolean tempMagical = magical; // The item maybe magical already...

        // Enable holy weapon attack bonus and damage bonus if the actor has this condition AD this weapon isn't
        // magical already.
        if (actor.hasCondition(HolyWeaponCondition.class.getName()) && !magical) {
            attackRollModifier = attackRollModifier + 1;
            tempDamageRollBonus = tempDamageRollBonus + 1;
            tempMagical = true; // The weapon is magical while holy weapon is enabled.
        }

        if (criticalFailure) {
            log.info("{} critically MISSES an attack on {} with a {}", actor.getName(), target.getName(), weaponName);
            return false;
        } else if (criticalSuccess) {
            int damage = damageDice.roll() + damageDice.roll() + tempDamageRollBonus;
            log.info("{} critically hits an attack on {} with a {}: damage={}", actor.getName(), target.getName(), weaponName, damage);
            target.takeDamage(damage, silvered, tempMagical, false, false);
            return true;
        } else if (attackRoll + attackRollModifier + attackRollBonus >= target.getAC()) {
            int damage = damageDice.roll() + tempDamageRollBonus;
            log.info("{} hits an attack on {} with a {}: damage={}", actor.getName(), target.getName(), weaponName, damage);
            target.takeDamage(damage, silvered, tempMagical, false, false);
            return true;
        } else {
            // Miss
            log.info("{} MISSES the attack on {} with a {}", actor.getName(), target.getName(), weaponName);
            return false;
        }
    }

}
