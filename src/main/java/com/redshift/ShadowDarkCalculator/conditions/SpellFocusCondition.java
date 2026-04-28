package com.redshift.ShadowDarkCalculator.conditions;

import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.dice.RollModifier;
import lombok.extern.slf4j.Slf4j;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D20;

/**
 * A condition a creature has when it's focusing on a spell.
 */

@Slf4j
public class SpellFocusCondition implements Condition {

    private final int difficultyClass;
    private final RollModifier rollModifier;
    private final boolean spellCheckAdvantage;
    private final int spellCheckBonus;
    private final Runnable spellLostRunnable;

    public SpellFocusCondition(
            int difficultyClass,
            RollModifier rollModifier,
            boolean spellCheckAdvantage,
            int spellCheckBonus,
            Runnable spellLostRunnable) {

        this.difficultyClass = difficultyClass;
        this.rollModifier = rollModifier;
        this.spellCheckAdvantage = spellCheckAdvantage;
        this.spellCheckBonus = spellCheckBonus;
        this.spellLostRunnable = spellLostRunnable;
    }

    @Override
    public boolean appliesToDeadCreatures() {
        return false;
    }

    @Override
    public boolean canAct() {
        return true;
    }

    @Override
    public void end() {
        spellLostRunnable.run(); // Do whatever is done when it's lost...
    }

    private int getSpellCheckRoll(boolean disadvantaged) {
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
    public boolean hasEnded(Creature creature) {
        boolean disadvantage = creature.hasCondition(DisadvantagedCondition.class.getName());
        creature.removeCondition(DisadvantagedCondition.class.getName());

        final int spellCheckRoll = getSpellCheckRoll(disadvantage);

        int spellCheckModifier = 0;

        if (rollModifier.equals(RollModifier.INTELLIGENCE)) {
            spellCheckModifier = creature.getStats().getIntelligenceModifier();
        } else if (rollModifier.equals(RollModifier.WISDOM)) {
            spellCheckModifier = creature.getStats().getWisdomModifier();
        } else if (rollModifier.equals(RollModifier.CHARISMA)) {
            spellCheckModifier = creature.getStats().getCharismaModifier();
        }

        // Critical success or failure has no extra effect.

        if (spellCheckRoll + spellCheckModifier + spellCheckBonus >= difficultyClass) {
            log.info("{} continues to holding spell focus.", creature.getName());
            return false; // Spell focus succeeded and it remains.
        } else {
            log.info("{} has lost spell focus!", creature.getName());
            spellLostRunnable.run(); // Do whatever is done when it's lost...
            return true; // Spell focus ended and it will be removed.
        }
    }

    @Override
    public void perform(Creature creature) {
        // Nothing to perform... all the action is checking if focus is maintained.
    }
}
