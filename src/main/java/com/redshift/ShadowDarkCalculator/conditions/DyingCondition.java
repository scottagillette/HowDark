package com.redshift.ShadowDarkCalculator.conditions;

import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.dice.RollOutcome;
import lombok.extern.slf4j.Slf4j;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D20;

/**
 * Dying and can't act!
 */

@Slf4j
public class DyingCondition implements Condition {

    private int rounds;

    public DyingCondition(int rounds) {
        this.rounds = rounds + 1; // Since we check at the beginning of the creatures turn... add 1.
    }

    @Override
    public boolean appliesToDeadCreatures() {
        return false;
    }

    @Override
    public boolean canAct() {
        return false;
    }

    @Override
    public boolean canMove() {
        return false;
    }

    @Override
    public void end() {
        // No specific behavior
        rounds = 0;
    }

    @Override
    public boolean hasEnded(Creature creature) {
        int requiredRoll = RollOutcome.CRITICAL_SUCCESS;
        int hpHealed = 1;

        final InspiredCondition inspiredCondition = (InspiredCondition) creature.getCondition(InspiredCondition.class.getName());
        if (inspiredCondition != null) {
            requiredRoll = 18;
            hpHealed = inspiredCondition.getHpRecovered();
        }

        // On a dying creatures turn roll a D20, on a 20 heal 1 hp and rise!
        if (D20.roll() >= requiredRoll) {
            log.info("{} is no longer dying and heals {} hp!", creature.getName(), hpHealed);
            creature.healDamage(hpHealed); // Healing removes the dying condition
            return true;
        } else {
            rounds = Math.max(0, rounds - 1);
            return false;
        }
    }

    @Override
    public void perform(Creature creature) {
        if (rounds == 0) {
            log.info("{} has died!", creature.getName());
            creature.setDead(true);
        } else {
            log.info("{} has their death timer tick down; {} remaining.", creature.getName(), rounds);

            // Loose any spell focus while dying!
            final SpellFocusCondition spellFocusCondition = (SpellFocusCondition) creature.removeCondition(SpellFocusCondition.class.getName());
            if (spellFocusCondition != null) {
                spellFocusCondition.end();
            }
        }
    }
}
