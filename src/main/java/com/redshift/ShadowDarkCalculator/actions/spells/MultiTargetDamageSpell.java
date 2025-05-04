package com.redshift.ShadowDarkCalculator.actions.spells;

import com.redshift.ShadowDarkCalculator.dice.RollModifier;
import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.dice.Dice;
import com.redshift.ShadowDarkCalculator.dice.RollOutcome;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D20;
import static java.lang.Math.min;

/**
 * A spell that can affect multiple targets and does damage to all... i.e. burning hands, fireball, lightening, etc.
 */

public abstract class MultiTargetDamageSpell extends Spell {

    protected final Dice totalTargets;
    protected final Dice damageDice;

    public MultiTargetDamageSpell(String name, int difficultyClass, RollModifier rollModifier, Dice damageDice, Dice totalTargets) {
        super(name, difficultyClass, rollModifier);
        this.damageDice = damageDice;
        this.totalTargets = totalTargets;
    }

    @Override
    public void perform(Creature actor, List<Creature> enemies, List<Creature> allies) {
        final int numberOfTargets = min(enemies.size(), totalTargets.roll());

        // Get list of targets...

        List<Creature> targets = new ArrayList<>(enemies);
        Collections.shuffle(targets);
        targets = targets.subList(0, numberOfTargets);

        performMultiTargetSpellAttack(actor, targets, this, difficultyClass, damageDice, rollModifier);
    }

    protected void performMultiTargetSpellAttack(
            Creature actor,
            List<Creature> targets,
            Spell spell,
            int difficultyClass,
            Dice damageDice,
            RollModifier rollModifier) {

        // See if they pass the spell check!
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
            targets.forEach(target -> {
                target.takeDamage(damage, false, true);
                System.out.println(actor.getName() + " critically hits a spell on " + target.getName() + " with a " + spell.getName() + ": damage=" + damage);
            });
        } else if (spellCheckRoll + spellCheckModifier + spellCheckBonus >= difficultyClass) {
            int damage = damageDice.roll();
            targets.forEach(target -> {
                target.takeDamage(damage, false, true);
                System.out.println(actor.getName() + " hits a spell on " + target.getName() + " with a " + spell.getName() + ": damage=" + damage);
            });
        } else {
            lost = true; // Failed spell check!
            System.out.println(actor.getName() + " MISSES the spell check with a " + spell.getName());
        }
    }
}
