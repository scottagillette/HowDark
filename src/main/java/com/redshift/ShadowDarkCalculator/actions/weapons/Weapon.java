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
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.*;

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

        return disadvantage;
    }

    @Override
    public final boolean canPerform(Creature actor, List<Creature> enemies, List<Creature> allies) {
        return !destroyed; // Sometimes weapons can be broken, melted, destroyed, etc.
    }

    /**
     * Returns the attack roll... which does not include the spell check bonus if any.
     */

    private int getAttackRoll(Creature actor, Creature target, int rollBonus, int armorClass) {
        final boolean disadvantage = checkDisadvantage(actor, target);

        int d20Roll = D20.roll();

        if (disadvantage) {
            d20Roll = Math.min(D20.roll(), D20.roll());
        } else if (d20Roll == RollOutcome.CRITICAL_SUCCESS) {
            return d20Roll;
        }

        if (d20Roll + rollBonus >= armorClass && d20Roll != RollOutcome.CRITICAL_FAILURE) {
            return d20Roll;
        } else {
            if (actor.hasLuckToken()) {
                log.info("{} uses their luck token to re-roll!", actor.getName());
                actor.spendLuckToken();
                return getAttackRoll(actor, target, rollBonus, armorClass); // Can't be disadvantaged on a luck re-roll is my guess.
            }
        }

        return d20Roll;
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
            tempAttackRollBonus = attackRollBonus + 1;
            tempDamageRollBonus = damageRollBonus + 1;
            tempDamageType.addMagical();
        }

        // Add extra damage if they have the Rage condition!

        if (actor.hasCondition(RageCondition.class.getName())) {
            tempDamageRollBonus = damageRollBonus + D4.roll();
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

    private void checkWeaponBreaks(Creature actor, Creature target, boolean magical) {
        if (target.hasCondition(CosticToWeaponsCondition.class.getName()) && !magical) {
            int roll = D6.roll();

            if (roll == 1) {
                log.info("{} has their weapon break!", actor.getName());
                destroyed = true;
            }
        }
    }
}
