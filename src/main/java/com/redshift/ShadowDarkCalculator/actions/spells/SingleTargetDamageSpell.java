package com.redshift.ShadowDarkCalculator.actions.spells;

import com.redshift.ShadowDarkCalculator.actions.DamageType;
import com.redshift.ShadowDarkCalculator.dice.RollModifier;
import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.dice.Dice;
import com.redshift.ShadowDarkCalculator.dice.RollOutcome;
import com.redshift.ShadowDarkCalculator.encounter.Encounter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * A spell that can affect a single targets and does damage to it... i.e. magic missile, etc.
 */

@Slf4j
public abstract class SingleTargetDamageSpell extends Spell {

    protected final Dice damageDice;
    protected final DamageType damageType = new DamageType();

    public SingleTargetDamageSpell(String name, int difficultyClass, RollModifier rollModifier, Dice damageDice, boolean advantage) {
        super(name, difficultyClass, rollModifier);
        this.damageDice = damageDice;
        this.spellCheckAdvantage = advantage;

        damageType.addMagical(); // All damage spells should be magical.
    }

    @Override
    public void perform(Creature actor, List<Creature> enemies, List<Creature> allies, Encounter encounter) {
        final Creature target = actor.getSingleTargetSelector().get(enemies);

        if (target == null) {
            log.info("{} is skipping their turn... no target!", actor.getName());
        } else {
            performSingleTargetSpellAttack(actor, target, this, difficultyClass, damageDice, rollModifier);
        }
    }

    protected boolean performSingleTargetSpellAttack(
            Creature actor,
            Creature target,
            Spell spell,
            int difficultyClass,
            Dice damageDice,
            RollModifier rollModifier) {

        int spellCheckModifier = 0;

        if (rollModifier.equals(RollModifier.INTELLIGENCE)) {
            spellCheckModifier = actor.getStats().getIntelligenceModifier();
        } else if (rollModifier.equals(RollModifier.WISDOM)) {
            spellCheckModifier = actor.getStats().getWisdomModifier();
        } else if (rollModifier.equals(RollModifier.CHARISMA)) {
            spellCheckModifier = actor.getStats().getCharismaModifier();
        }

        final int d20Roll = getSpellCheckRoll(actor, List.of(target), spellCheckModifier);

        final boolean criticalSuccess = d20Roll == RollOutcome.CRITICAL_SUCCESS;
        final boolean criticalFailure = d20Roll == RollOutcome.CRITICAL_FAILURE;

        if (criticalFailure) {
            lost = true; // Failed spell check!
            log.info("{} critically MISSES the spell check with a {}", actor.getName(), spell.getName());
            return false;
        } else if (criticalSuccess) {
            int damage = damageDice.roll() + damageDice.roll();
            log.info("{} critically hits a spell on {} with a {}: damage={}", actor.getName(), target.getName(), spell.getName(), damage);
            target.takeDamage(damage, damageType);
            return true;
        } else if (d20Roll + spellCheckModifier + spellCheckBonus >= difficultyClass) {
            int damage = damageDice.roll();
            log.info("{} hits a spell on {} with a {}: damage={}", actor.getName(), target.getName(), spell.getName(), damage);
            target.takeDamage(damage, damageType);
            return true;
        } else {
            lost = true; // Failed spell check!
            log.info("{} MISSES the spell check with a {}", actor.getName(), spell.getName());
            return false;
        }
    }
}
