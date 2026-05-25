package com.redshift.ShadowDarkCalculator.actions.spells;

import com.redshift.ShadowDarkCalculator.actions.DamageType;
import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.dice.Dice;
import com.redshift.ShadowDarkCalculator.dice.RollModifier;
import com.redshift.ShadowDarkCalculator.encounter.Encounter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * A spell that can affect a single targets and does damage to it. Note this
 * uses the actors assigned target selection; not a specialized one.
 */

@Slf4j
public abstract class SingleTargetDamageSpell extends Spell {

    protected final Dice damageDice;
    protected final DamageType damageType = new DamageType();

    public SingleTargetDamageSpell(String name, int difficultyClass, RollModifier rollModifier, Dice damageDice, boolean advantage) {
        super(name, difficultyClass, rollModifier);
        this.damageDice = damageDice;
        damageType.addMagical(); // All damage spells should be magical.
    }

    @Override
    public boolean canPerform(Creature actor, List<Creature> enemies, List<Creature> allies) {
        final boolean canPerform = super.canPerform(actor, enemies, allies);
        final boolean hasTarget = !getTargets(actor, enemies, allies).isEmpty();
        return (canPerform && hasTarget);
    }

    @Override
    public List<Creature> getTargets(Creature actor, List<Creature> enemies, List<Creature> allies) {
        // Default implementation; get a single target based on the creatures specific
        // target selection algorithm.
        final List<Creature> targets = new ArrayList<>();
        final Creature target = actor.getSingleTargetSelector().get(enemies);
        if (target != null) targets.add(target);
        return targets;
    }

    @Override
    public void performCriticalSpell(Creature actor, List<Creature> targets, Encounter encounter, int spellCheckRoll) {
        final Creature target = targets.getFirst(); // Always uses single target.
        int damage = damageDice.roll() + damageDice.roll();
        log.info("{} critically hits a spell on {} with a {} for {} damage", actor.getName(), target.getName(), name, damage);
        target.takeDamage(damage, damageType);
    }

    @Override
    public void performSpell(Creature actor, List<Creature> targets, Encounter encounter, int spellCheckRoll) {
        final Creature target = targets.getFirst(); // Always uses single target.
        int damage = damageDice.roll();
        log.info("{} hits a spell on {} with a {} for {} damage", actor.getName(), target.getName(), name, damage);
        target.takeDamage(damage, damageType);
    }
}
