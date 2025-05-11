package com.redshift.ShadowDarkCalculator.actions.weapons;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D20;

import com.redshift.ShadowDarkCalculator.actions.Action;
import com.redshift.ShadowDarkCalculator.conditions.DisadvantagedCondition;
import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.dice.Dice;
import com.redshift.ShadowDarkCalculator.dice.RollModifier;
import com.redshift.ShadowDarkCalculator.dice.RollOutcome;
import java.util.List;
import lombok.extern.slf4j.Slf4j;

/**
 * An action that attempts to attack with a melee or ranged weapon.
 */

@Slf4j
public class Weapon implements Action {

    protected final String name;
    protected final Dice dice;
    protected final RollModifier rollModifier;
    protected int attackRollBonus = 0;
    protected int damageRollBonus = 0;
    protected boolean magical;
    protected boolean silvered;
    protected int priority = 1;

    public Weapon(String name, Dice dice, RollModifier rollModifier) {
        this(name, dice, rollModifier, false, false, 0, 0, 1);
    }

    public Weapon(
        String name,
        Dice dice,
        RollModifier rollModifier,
        int attackRollBonus
    ) {
        this(name, dice, rollModifier, false, false, attackRollBonus, 0, 1);
    }

    public Weapon(
        String name,
        Dice dice,
        RollModifier rollModifier,
        boolean magical,
        boolean silvered,
        int attackRollBonus,
        int damageRollBonus,
        int priority
    ) {
        this.name = name;
        this.dice = dice;
        this.rollModifier = rollModifier;
        this.attackRollBonus = attackRollBonus;
        this.damageRollBonus = damageRollBonus;
        this.magical = magical;
        this.silvered = silvered;
        this.priority = priority;
    }

    @Override
    public boolean canPerform(
        Creature actor,
        List<Creature> enemies,
        List<Creature> allies
    ) {
        return true; // Melee and ranged attacks by default can always be performed.
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    public boolean isLost() {
        return false; // Melee and ranged attacks by default cannot be lost like spells.
    }

    @Override
    public boolean isMagical() {
        return magical;
    }

    @Override
    public void perform(
        Creature actor,
        List<Creature> enemies,
        List<Creature> allies
    ) {
        // Normal weapons attack a single target... custom weapons can hit multiple (see performMultipleTargetAttack())
        final Creature target = actor.getSingleTargetSelector().get(enemies);

        if (target == null) {
            log.info(actor.getName() + " is skipping their turn... no target!");
        } else {
            performSingleTargetAttack(
                actor,
                target,
                name,
                dice,
                rollModifier,
                actor.isDisadvantaged()
            );
            actor.clearCondition(DisadvantagedCondition.class.getName());
        }
    }

    protected boolean performSingleTargetAttack(
        Creature actor,
        Creature target,
        String weaponName,
        Dice damageDice,
        RollModifier rollModifier,
        boolean disadvantaged
    ) {
        final int attackRoll = disadvantaged
            ? Math.min(D20.roll(), D20.roll())
            : D20.roll();

        final boolean criticalSuccess =
            attackRoll == RollOutcome.CRITICAL_SUCCESS;
        final boolean criticalFailure =
            attackRoll == RollOutcome.CRITICAL_FAILURE;

        int attackRollModifier = 0;

        if (rollModifier.equals(RollModifier.STRENGTH)) {
            attackRollModifier =
                attackRollModifier + actor.getStats().getStrengthModifier();
        } else if (rollModifier.equals(RollModifier.DEXTERITY)) {
            attackRollModifier =
                attackRollModifier + actor.getStats().getDexterityModifier();
        }

        if (criticalFailure) {
            // Do nothing
            log.info(
                actor.getName() +
                " critically MISSES an attack on " +
                target.getName() +
                " with a " +
                weaponName
            );
            return false;
        } else if (criticalSuccess) {
            int damage =
                damageDice.roll() + damageDice.roll() + damageRollBonus;
            log.info(
                actor.getName() +
                " critically hits an attack on " +
                target.getName() +
                " with a " +
                weaponName +
                ": damage=" +
                damage
            );
            target.takeDamage(damage, silvered, magical, false, false);
            return true;
        } else if (
            attackRoll + attackRollModifier + attackRollBonus >= target.getAC()
        ) {
            int damage = damageDice.roll() + damageRollBonus;
            log.info(
                actor.getName() +
                " hits an attack on " +
                target.getName() +
                " with a " +
                weaponName +
                ": damage=" +
                damage
            );
            target.takeDamage(damage, silvered, magical, false, false);
            return true;
        } else {
            // Miss
            log.info(
                actor.getName() +
                " MISSES the attack on " +
                target.getName() +
                " with a " +
                weaponName
            );
            return false;
        }
    }

    protected void performMultipleTargetAttack(
        Creature actor,
        List<Creature> targets,
        String weaponName,
        Dice damageDice,
        RollModifier rollModifier,
        boolean disadvantaged
    ) {
        // TODO: disadvantaged should only affect the first roll here.

        targets.forEach(target -> {
            final int attackRoll = disadvantaged
                ? Math.min(D20.roll(), D20.roll())
                : D20.roll();

            final boolean criticalSuccess =
                attackRoll == RollOutcome.CRITICAL_SUCCESS;
            final boolean criticalFailure =
                attackRoll == RollOutcome.CRITICAL_FAILURE;

            int attackRollModifier = 0;

            if (rollModifier.equals(RollModifier.STRENGTH)) {
                attackRollModifier =
                    attackRollModifier + actor.getStats().getStrengthModifier();
            } else if (rollModifier.equals(RollModifier.DEXTERITY)) {
                attackRollModifier =
                    attackRollModifier +
                    actor.getStats().getDexterityModifier();
            }

            if (criticalFailure) {
                // Do nothing
                log.info(
                    actor.getName() +
                    " critically MISSES an attack on " +
                    target.getName() +
                    " with a " +
                    weaponName
                );
            } else if (criticalSuccess) {
                int damage =
                    damageDice.roll() + damageDice.roll() + damageRollBonus;
                log.info(
                    actor.getName() +
                    " critically hits an attack on " +
                    target.getName() +
                    " with a " +
                    weaponName +
                    ": damage=" +
                    damage
                );
                target.takeDamage(damage, silvered, magical, false, false);
            } else if (
                attackRoll + attackRollModifier + attackRollBonus >=
                target.getAC()
            ) {
                int damage = damageDice.roll() + damageRollBonus;
                log.info(
                    actor.getName() +
                    " hits an attack on " +
                    target.getName() +
                    " with a " +
                    weaponName +
                    ": damage=" +
                    damage
                );
                target.takeDamage(damage, silvered, magical, false, false);
            } else {
                // Miss
                log.info(
                    actor.getName() +
                    " MISSES the attack on " +
                    target.getName() +
                    " with a " +
                    weaponName
                );
            }
        });
    }

    @Override
    public Weapon setPriority(int priority) {
        this.priority = priority;
        return this;
    }
}
