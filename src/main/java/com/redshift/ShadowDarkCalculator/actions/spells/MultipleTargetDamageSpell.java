package com.redshift.ShadowDarkCalculator.actions.spells;

import com.redshift.ShadowDarkCalculator.actions.DamageType;
import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.dice.Dice;
import com.redshift.ShadowDarkCalculator.dice.RollModifier;
import com.redshift.ShadowDarkCalculator.encounter.Encounter;
import com.redshift.ShadowDarkCalculator.targets.multi.AliveTargetSelector;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static java.lang.Math.min;

/**
 * A spell that effects multiple targets and does damage to all... i.e. burning hands,
 * fireball, lightening, etc.
 */

@Slf4j
public class MultipleTargetDamageSpell extends MultipleTargetSpell {

    protected final Dice damageDice;
    protected final DamageType damageType = new DamageType();

    public MultipleTargetDamageSpell(
            String name,
            int difficultyClass,
            RollModifier rollModifier,
            Dice damageDice,
            Dice totalTargets) {

        super(name, difficultyClass, rollModifier, totalTargets);
        this.damageDice = damageDice;

        damageType.addMagical(); // All damage spells should be magical.
    }

    @Override
    public boolean canPerform(Creature actor, List<Creature> enemies, List<Creature> allies) {
        final boolean canPerform = super.canPerform(actor, enemies, allies);
        final boolean hasTargets = !getTargets(actor, enemies, allies).isEmpty();
        return (canPerform && hasTargets);
    }

    @Override
    public List<Creature> getTargets(Creature actor, List<Creature> enemies, List<Creature> allies) {
        final int numberOfTargets = min(enemies.size(), totalTargets.roll());
        return new AliveTargetSelector().getTargets(enemies, numberOfTargets);
    }

    @Override
    public void performCriticalSpell(Creature actor, List<Creature> targets, Encounter encounter, int spellCheckRoll) {
        int damage = damageDice.roll() + damageDice.roll();
        targets.forEach(target -> {
            log.info("{} critically hits a spell on {} with {} for {} damage", actor.getName(), target.getName(), name, damage);
            target.takeDamage(damage, damageType);
        });
    }

    @Override
    public void performSpell(Creature actor, List<Creature> targets, Encounter encounter, int spellCheckRoll) {
        int damage = damageDice.roll();
        targets.forEach(target -> {
            log.info("{} hits a spell on {} with {} for {} damage", actor.getName(), target.getName(), name, damage);
            target.takeDamage(damage, damageType);
        });
    }
}
