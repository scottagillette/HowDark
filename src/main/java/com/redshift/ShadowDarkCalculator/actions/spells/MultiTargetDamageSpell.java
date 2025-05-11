package com.redshift.ShadowDarkCalculator.actions.spells;

import com.redshift.ShadowDarkCalculator.conditions.DisadvantagedCondition;
import com.redshift.ShadowDarkCalculator.dice.RollModifier;
import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.dice.Dice;
import com.redshift.ShadowDarkCalculator.dice.RollOutcome;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static java.lang.Math.min;

/**
 * A spell that can affect multiple targets and does damage to all... i.e. burning hands, fireball, lightening, etc.
 */

@Slf4j
public abstract class MultiTargetDamageSpell extends Spell {

    protected final Dice totalTargets;
    protected final Dice damageDice;
    private final boolean fireDamage;
    private final boolean coldDamage;

    public MultiTargetDamageSpell(
            String name,
            int difficultyClass,
            RollModifier rollModifier,
            Dice damageDice,
            Dice totalTargets,
            boolean fireDamage,
            boolean coldDamage) {

        super(name, difficultyClass, rollModifier);
        this.damageDice = damageDice;
        this.totalTargets = totalTargets;
        this.fireDamage = fireDamage;
        this.coldDamage = coldDamage;
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

        boolean disadvantage = actor.hasCondition(DisadvantagedCondition.class.getName());
        actor.removeCondition(DisadvantagedCondition.class.getName());

        // See if they pass the spell check!
        final int spellCheckRoll = getSpellCheckRoll(disadvantage);

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
            log.info("{} critically MISSES the spell check with a {}", actor.getName(), spell.getName());
        } else if (criticalSuccess) {
            int damage = damageDice.roll() + damageDice.roll();
            targets.forEach(target -> {
                log.info("{} critically hits a spell on {} with a {}: damage={}", actor.getName(), target.getName(), spell.getName(), damage);
                target.takeDamage(damage, false, true, fireDamage, coldDamage);
            });
        } else if (spellCheckRoll + spellCheckModifier + spellCheckBonus >= difficultyClass) {
            int damage = damageDice.roll();
            targets.forEach(target -> {
                log.info("{} hits a spell on {} with a {}: damage={}", actor.getName(), target.getName(), spell.getName(), damage);
                target.takeDamage(damage, false, true, fireDamage, coldDamage);
            });
        } else {
            lost = true; // Failed spell check!
            log.info("{} MISSES the spell check with a {}", actor.getName(), spell.getName());
        }
    }
}
