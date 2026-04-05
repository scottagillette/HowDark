package com.redshift.ShadowDarkCalculator.actions.misc;

import com.redshift.ShadowDarkCalculator.actions.Action;
import com.redshift.ShadowDarkCalculator.conditions.ParalyzedCondition;
import com.redshift.ShadowDarkCalculator.creatures.Creature;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D4;

/**
 * One target in near DC 15 CHA or paralyzed 1d4 rounds.
 */

@Slf4j
public class Terrify implements Action {

    private int priority = 1;

    @Override
    public boolean canPerform(Creature actor, List<Creature> enemies, List<Creature> allies) {
        List<Creature> targets = enemies
                .stream()
                .filter(enemy -> !enemy.isDead() && !enemy.isUnconscious())
                .toList();

        return !targets.isEmpty();
    }

    @Override
    public String getName() {
        return "Terrify";
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
    public void perform(Creature actor, List<Creature> enemies, List<Creature> allies) {
        // One target in near DC 15 CHA or paralyzed 1d4 rounds.

        List<Creature> targets = enemies
                .stream()
                .filter(enemy -> !enemy.isDead() && !enemy.isUnconscious())
                .toList();

        if (targets.isEmpty()) {
            log.info("{} finds no targets to Terrify!", actor.getName());
        } else {
            final Creature enemy = targets.getFirst();
            if (enemy.getStats().charismaSave(15)) {
                log.info("{} resists the Terrify.", enemy.getName());
            } else {
                log.info("{} is terrified and is paralyzed.", enemy.getName());
                enemy.addCondition(new ParalyzedCondition(D4.roll()));
            }
        }
    }

    @Override
    public Action setPriority(int priority) {
        this.priority = priority;
        return this;
    }
}
