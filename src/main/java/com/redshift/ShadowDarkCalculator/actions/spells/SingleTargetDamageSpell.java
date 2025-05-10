package com.redshift.ShadowDarkCalculator.actions.spells;

import com.redshift.ShadowDarkCalculator.dice.RollModifier;
import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.dice.Dice;
import com.redshift.ShadowDarkCalculator.dice.RollOutcome;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * A spell that can affect a single targets and does damage to it... i.e. magic missile, acid arrow, etc.
 */

@Slf4j
public abstract class SingleTargetDamageSpell extends Spell {

    protected final Dice damageDice;

    public SingleTargetDamageSpell(String name, int difficultyClass, RollModifier rollModifier, Dice damageDice, boolean advantage) {
        super(name, difficultyClass, rollModifier);
        this.damageDice = damageDice;
        this.spellCheckAdvantage = advantage;
    }

    @Override
    public void perform(Creature actor, List<Creature> enemies, List<Creature> allies) {
        final Creature target = actor.getSingleTargetSelector().get(enemies);

        if (target == null) {
            log.info(actor.getName() + " is skipping their turn... no target!");
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
            log.info(actor.getName() + " critically MISSES the spell check with a " + spell.getName());
        } else if (criticalSuccess) {
            int damage = damageDice.roll() + damageDice.roll();
            log.info(actor.getName() + " critically hits a spell on " + target.getName() + " with a " + spell.getName() + ": damage=" + damage);
            target.takeDamage(damage, false, true, false, false);
        } else if (spellCheckRoll + spellCheckModifier >= difficultyClass) {
            int damage = damageDice.roll();
            log.info(actor.getName() + " hits a spell on " + target.getName() + " with a " + spell.getName() + ": damage=" + damage);
            target.takeDamage(damage, false, true, false, false);
        } else {
            lost = true; // Failed spell check!
            log.info(actor.getName() + " MISSES the spell check with a " + spell.getName());
        }
    }
}
