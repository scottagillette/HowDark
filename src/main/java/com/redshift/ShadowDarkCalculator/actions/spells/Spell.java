package com.redshift.ShadowDarkCalculator.actions.spells;

import com.redshift.ShadowDarkCalculator.actions.Action;
import com.redshift.ShadowDarkCalculator.actions.BaseAction;
import com.redshift.ShadowDarkCalculator.conditions.DisadvantagedCondition;
import com.redshift.ShadowDarkCalculator.conditions.ProtectionFromEvilCondition;
import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.creatures.CreatureLabel;
import com.redshift.ShadowDarkCalculator.dice.RollModifier;
import com.redshift.ShadowDarkCalculator.dice.RollOutcome;
import com.redshift.ShadowDarkCalculator.encounter.Encounter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D20;

/**
 * An action that attempts to cast a spell.
 */

@Slf4j
public abstract class Spell extends BaseAction implements Action {

    protected final int difficultyClass;
    protected final RollModifier rollModifier;
    protected boolean lost = false; // True if the spell failed to cast and is lost until a rest occurs.
    protected int spellCheckBonus = 0; // A bonus to the spell check roll... zero by default.
    protected boolean spellCheckAdvantage = false; // Some talents or spells get advantage on the spell check.

    protected Spell(String name, int difficultyClass, RollModifier rollModifier) {
        super(name);
        this.difficultyClass = difficultyClass;
        this.rollModifier = rollModifier;
    }

    public Spell addAdvantage() {
        spellCheckAdvantage = true;
        return this;
    }

    public Spell addSpellCheckBonus(int bonus) {
        this.spellCheckBonus = bonus;
        return this;
    }

    @Override
    public boolean canPerform(Creature actor, List<Creature> enemies, List<Creature> allies) {
        return !lost;
    }

    private boolean checkDisadvantage(Creature actor, List<Creature> targets) {
        // Check for disadvantage condition and then remove it.
        boolean disadvantage = actor.hasCondition(DisadvantagedCondition.class.getName());
        actor.removeCondition(DisadvantagedCondition.class.getName());

        // Check for protection from evil giving disadvantage, unless they already have it.
        if (!disadvantage && targets != null) {
            if (actor.getLabels().contains(CreatureLabel.CHAOTIC)) {
                final List<Creature> targetsWithProtection = targets.stream()
                        .filter(creature -> creature.hasCondition(ProtectionFromEvilCondition.class.getName()))
                        .toList();
                if (!targetsWithProtection.isEmpty()) {
                    disadvantage = true;
                    log.info("{} has disadvantage casting a spell because {} has protection from evil!", actor.getName(), targets.getFirst());
                }
            }
        }

        return disadvantage;
    }

    /**
     * Returns the spell check roll... which does not include the spell check bonus if any.
     */

    private int getSpellCheckRoll(Creature actor, List<Creature> targets, int spellCheckModifier) {
        boolean disadvantage = checkDisadvantage(actor, targets);

        boolean checkWithAdvantaged = spellCheckAdvantage && !disadvantage;
        boolean checkWithDisadvantaged = !spellCheckAdvantage && disadvantage;

        int d20Result = D20.roll();

        if (checkWithAdvantaged) {
            d20Result = Math.max(D20.roll(), D20.roll());
        } else if (checkWithDisadvantaged) {
            d20Result = Math.min(D20.roll(), D20.roll());
        }

        if (d20Result + spellCheckModifier + spellCheckBonus >= difficultyClass && d20Result != RollOutcome.CRITICAL_FAILURE) {
            return d20Result; // Return the raw result to check for critical success or failure.
        } else {
            if (actor.hasLuckToken()) {
                log.info("{} uses their luck token to re-roll!", actor.getName());
                actor.spendLuckToken();
                return getSpellCheckRoll(actor, targets, spellCheckModifier); // Can't be disadvantaged on a luck re-roll is my guess.
            }
        }

        return d20Result; // Return the raw result to check for critical success or failure.
    }

    /**
     * Retrieve the targets of the spell.
     */

    public abstract List<Creature> getTargets(Creature actor, List<Creature> enemies, List<Creature> allies);

    @Override
    public final boolean isMagicalWeapon() {
        return false; // Magical, but not specifically magical weapon.
    }

    @Override
    public final void perform(Creature actor, List<Creature> enemies, List<Creature> allies, Encounter encounter) {
        final List<Creature> targets = getTargets(actor, enemies, allies);

        if (targets.isEmpty()) {
            throw new IllegalStateException("Spell with no targets");
        }

        int spellCheckModifier = 0;

        if (rollModifier.equals(RollModifier.INTELLIGENCE)) {
            spellCheckModifier = actor.getStats().getIntelligenceModifier();
        } else if (rollModifier.equals(RollModifier.WISDOM)) {
            spellCheckModifier = actor.getStats().getWisdomModifier();
        } else if (rollModifier.equals(RollModifier.CHARISMA)) {
            spellCheckModifier = actor.getStats().getCharismaModifier();
        }

        // See if they pass the spell check!
        final int d20Roll = getSpellCheckRoll(actor, targets, spellCheckModifier);

        final boolean criticalSuccess = d20Roll == RollOutcome.CRITICAL_SUCCESS;
        final boolean criticalFailure = d20Roll == RollOutcome.CRITICAL_FAILURE;

        final int spellCheckRoll = d20Roll + spellCheckModifier + spellCheckBonus;

        if (criticalFailure) {
            lost = true; // Failed spell check!
            log.info("{} critically MISSES the spell check with a {}", actor.getName(), name);
        } else if (criticalSuccess) {
            performCriticalSpell(actor, targets, encounter,spellCheckRoll);
        } else if (d20Roll + spellCheckModifier + spellCheckBonus >= difficultyClass) {
            performSpell(actor, targets, encounter, spellCheckRoll);
        } else {
            lost = true; // Failed spell check!
            log.info("{} MISSES the spell check with a {}", actor.getName(), name);
        }
    }

    /**
     * Implemented by subtypes to perform the spell using critical success rules.
     */

    public abstract void performCriticalSpell(Creature actor, List<Creature> targets, Encounter encounter, int spellCheckRoll);

    /**
     * Implemented by subtypes to perform the spell using normal rules.
     */

    public abstract void performSpell(Creature actor, List<Creature> targets, Encounter encounter, int spellCheckRoll);

}
