package com.redshift.ShadowDarkCalculator.actions.spells;

import com.redshift.ShadowDarkCalculator.actions.Action;
import com.redshift.ShadowDarkCalculator.actions.BaseAction;
import com.redshift.ShadowDarkCalculator.conditions.DisadvantagedCondition;
import com.redshift.ShadowDarkCalculator.dice.RollModifier;
import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.dice.RollOutcome;
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

    /**
     * Returns the spell check roll... which does not include the spell check bonus if any.
     */

    protected int getSpellCheckRoll(Creature actor, int spellCheckModifier) {
        boolean disadvantage = actor.hasCondition(DisadvantagedCondition.class.getName());
        actor.removeCondition(DisadvantagedCondition.class.getName());

        boolean checkWithAdvantaged = spellCheckAdvantage && !disadvantage;
        boolean checkWithDisadvantaged = !spellCheckAdvantage && disadvantage;

        int d20Result = D20.roll();

        if (checkWithAdvantaged) {
            d20Result = Math.max(D20.roll(), D20.roll());
        }
        if (checkWithDisadvantaged) {
            d20Result = Math.min(D20.roll(), D20.roll());
        }

        if (d20Result + spellCheckModifier + spellCheckBonus >= difficultyClass && d20Result != RollOutcome.CRITICAL_FAILURE) {
            return d20Result; // Return the raw result to check for critical success or failure.
        } else {
            if (actor.hasLuckToken()) {
                log.info("{} uses their luck token to re-roll!", actor.getName());
                actor.spendLuckToken();
                return getSpellCheckRoll(actor, spellCheckModifier); // Can't be disadvantaged on a luck re-roll is my guess.
            }
        }

        return d20Result; // Return the raw result to check for critical success or failure.
    }

    @Override
    public boolean isMagicalWeapon() {
        return false;
    }
}
