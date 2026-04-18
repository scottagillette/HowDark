package com.redshift.ShadowDarkCalculator.actions.misc;

import com.redshift.ShadowDarkCalculator.actions.Action;
import com.redshift.ShadowDarkCalculator.conditions.FearCondition;
import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.encounter.CombatSimulator;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D4;

/**
 * All creatures within near DC 15 CHA or flee in a random direction for 1d4 rounds.
 */

@Slf4j
public class WakingFear implements Action {

    private int priority;

    @Override
    public boolean canPerform(Creature actor, List<Creature> enemies, List<Creature> allies) {
        return true; // Not a spell
    }

    @Override
    public String getName() {
        return "Waking Fear";
    }

    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    public boolean isLost() {
        return false;
    }

    @Override
    public boolean isMagical() {
        return false;
    }

    @Override
    public void perform(Creature actor, List<Creature> enemies, List<Creature> allies, CombatSimulator simulator) {
        // All creatures within near DC 15 CHA or flee in a random direction for 1d4 rounds.

        log.info("{} invokes Waking Fear to all!", actor.getName());

        enemies.forEach(enemy -> {
            if (!enemy.isUnconscious() && !enemy.isDead()) {
                if (enemy.getStats().charismaSave(15)) {
                    log.info("{} resists the fear.", enemy.getName());
                } else {
                    log.info("{} runs in fear from {}", enemy.getName(), actor.getName());
                    enemy.addCondition(new FearCondition(D4.roll()));
                }
            }
        });
    }

    @Override
    public Action setPriority(int priority) {
        this.priority = priority;
        return this;
    }
}
