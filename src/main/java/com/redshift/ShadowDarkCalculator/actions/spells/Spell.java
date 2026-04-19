package com.redshift.ShadowDarkCalculator.actions.spells;

import com.redshift.ShadowDarkCalculator.actions.Action;
import com.redshift.ShadowDarkCalculator.dice.RollModifier;
import com.redshift.ShadowDarkCalculator.creatures.Creature;

import java.util.List;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D20;

/**
 * An action that attempts to cast a spell.
 */

public abstract class Spell implements Action {

    protected final String name;
    protected final int difficultyClass;
    protected final RollModifier rollModifier;
    protected boolean lost = false; // True if the spell failed to cast and is lost until a rest occurs.
    protected int spellCheckBonus = 0; // A bonus to the spell check roll... zero by default.
    protected boolean spellCheckAdvantage = false; // Some spells or wizards get advantage on spell check.
    protected int priority; // Non-Zero integer value indicating it's overall priority to other actions.

    protected Spell(String name, int difficultyClass, RollModifier rollModifier) {
        this(name, difficultyClass, rollModifier, 1);
    }

    public Spell(String name, int difficultyClass, RollModifier rollModifier, int priority) {
        this.name = name;
        this.difficultyClass = difficultyClass;
        this.rollModifier = rollModifier;
        this.priority = priority;
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

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getPriority() {
        return priority;
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
    public boolean isLost() {
        return lost;
    }

    @Override
    public boolean isMagical() {
        return true;
    }

    @Override
    public Spell setPriority(int priority) {
        this.priority = priority;
        return this;
    }
}
