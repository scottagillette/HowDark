package com.redshift.ShadowDarkCalculator.actions.spells;

import com.redshift.ShadowDarkCalculator.actions.Action;
import com.redshift.ShadowDarkCalculator.actions.BaseAction;
import com.redshift.ShadowDarkCalculator.dice.RollModifier;
import com.redshift.ShadowDarkCalculator.creatures.Creature;

import java.util.List;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D20;

/**
 * An action that attempts to cast a spell.
 */

public abstract class Spell extends BaseAction implements Action {

    protected final int difficultyClass;
    protected final RollModifier rollModifier;
    protected boolean lost = false; // True if the spell failed to cast and is lost until a rest occurs.
    protected int spellCheckBonus = 0; // A bonus to the spell check roll... zero by default.
    protected boolean spellCheckAdvantage = false; // Some spells or wizards get advantage on spell check.

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

    protected int getSpellCheckRoll(boolean disadvantaged) {
        boolean checkWithAdvantaged = spellCheckAdvantage && !disadvantaged;
        boolean checkWithDisadvantaged = !spellCheckAdvantage && disadvantaged;

        if (checkWithAdvantaged) {
            return Math.max(D20.roll(), D20.roll());
        } else if (checkWithDisadvantaged) {
            return Math.min(D20.roll(), D20.roll());
        }
        return D20.roll();
    }

    @Override
    public boolean isMagical() {
        return true;
    }

}
