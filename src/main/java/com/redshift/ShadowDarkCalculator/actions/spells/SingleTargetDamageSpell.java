package com.redshift.ShadowDarkCalculator.actions.spells;

import com.redshift.ShadowDarkCalculator.dice.RollModifier;
import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.dice.Dice;
import com.redshift.ShadowDarkCalculator.dice.RollOutcome;

import java.util.List;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D20;

/**
 * A spell that can affect a single targets and does damage to it... i.e. magic missile, acid arrow, etc.
 */

public abstract class SingleTargetDamageSpell extends Spell {

    protected final Dice damageDice;

    public SingleTargetDamageSpell(String name, int difficultyClass, RollModifier rollModifier, Dice damageDice, boolean advantage) {
        super(name, difficultyClass, rollModifier);
        this.damageDice = damageDice;
        this.spellCheckAdvantage = advantage;
    }

    @Override
    public void perform(Creature actor, List<Creature> enemies, List<Creature> allies) {
        final Creature target = actor.getEnemyTargetSelector().getTarget(enemies);

        if (target == null) {
            System.out.println(actor.getName() + " is skipping their turn... no target!");
        } else {
            performSingleTargetSpellAttack(actor, target, this, difficultyClass, damageDice, rollModifier);
        }
    }

    protected void performSingleTargetSpellAttack(
            Creature actor,
            Creature target,
            Spell spell,
            int difficultyClass,
            Dice damageDice,
            RollModifier rollModifier) {

        final int spellCheckRoll = getSpellCheckRoll();

        final boolean criticalSuccess = spellCheckRoll == RollOutcome.CRITICAL_SUCCESS;
        final boolean criticalFailure = spellCheckRoll == RollOutcome.CRITICAL_FAILURE;

        int spellCheckModifier = 0;

        if (rollModifier.equals(RollModifier.INTELLIGENCE)) {
            spellCheckModifier = actor.getStats().getIntelligenceModifier();
        } else {
            spellCheckModifier = actor.getStats().getWisdomModifier();
        }

        if (criticalFailure) {
            lost = true; // Failed spell check!
            System.out.println(actor.getName() + " critically MISSES the spell check with a " + spell.getName());
        } else if (criticalSuccess) {
            int damage = damageDice.roll() + damageDice.roll();
            target.takeDamage(damage);
            System.out.println(actor.getName() + " critically hits a spell on " + target.getName() + " with a " + spell.getName() + ": damage=" + damage);
        } else if (spellCheckRoll + spellCheckModifier >= difficultyClass) {
            int damage = damageDice.roll();
            System.out.println(actor.getName() + " hits a spell on " + target.getName() + " with a " + spell.getName() + ": damage=" + damage);
            target.takeDamage(damage);
        } else {
            lost = true; // Failed spell check!
            System.out.println(actor.getName() + " MISSES the spell check with a " + spell.getName());
        }
    }
}
