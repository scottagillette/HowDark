package com.redshift.ShadowDarkCalculator.actions.spells;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D20;

import com.redshift.ShadowDarkCalculator.actions.Action;
import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.dice.RollModifier;
import java.util.List;

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
    protected int priority;

    protected Spell(
        String name,
        int difficultyClass,
        RollModifier rollModifier
    ) {
        this(name, difficultyClass, rollModifier, 1);
    }

    public Spell(
        String name,
        int difficultyClass,
        RollModifier rollModifier,
        int priority
    ) {
        this.name = name;
        this.difficultyClass = difficultyClass;
        this.rollModifier = rollModifier;
        this.priority = priority;
    }

    public Spell addAdvantage() {
        spellCheckAdvantage = true;
        return this;
    }

    public Spell addBonus(int bonus) {
        this.spellCheckBonus = bonus;
        return this;
    }

    @Override
    public boolean canPerform(
        Creature actor,
        List<Creature> enemies,
        List<Creature> allies
    ) {
        return !lost;
    }

    /**
     * Returns the spell check roll... which does not include the spell check bonus if any.
     */

    protected int getSpellCheckRoll(boolean disadvantaged) {
        boolean atkAdvantaged = spellCheckAdvantage && !disadvantaged;
        boolean atkDisadvantaged = !spellCheckAdvantage && disadvantaged;

        return (atkAdvantaged)
            ? Math.max(D20.roll(), D20.roll())
            : (atkDisadvantaged)
                ? Math.min(D20.roll(), D20.roll())
                : D20.roll();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getPriority() {
        return priority;
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
