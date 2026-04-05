package com.redshift.ShadowDarkCalculator.actions.misc;

import com.redshift.ShadowDarkCalculator.actions.Action;
import com.redshift.ShadowDarkCalculator.conditions.DevouredCondition;
import com.redshift.ShadowDarkCalculator.creatures.Creature;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Random;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D8;

/**
 * The Devour action, eat a dead body and heal for 3d8.
 */

@Slf4j
public class Devour implements Action {

    private int priority = 0;

    @Override
    public boolean canPerform(Creature actor, List<Creature> enemies, List<Creature> allies) {
        final List<Creature> deadCreaturesNotDevoured = enemies
                .stream()
                .filter(creature -> creature.isDead() && !creature.hasCondition(DevouredCondition.class.getName()))
                .toList();


        return actor.isWounded() && !deadCreaturesNotDevoured.isEmpty();
    }

    @Override
    public String getName() {
        return "Devour";
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
        final List<Creature> deadCreaturesNotDevoured = enemies
                .stream()
                .filter(creature -> creature.isDead() && !creature.hasCondition(DevouredCondition.class.getName()))
                .toList();

        if (deadCreaturesNotDevoured.isEmpty()) {
            log.info("Devour attempted but there are no dead bodies!");
        } else {
            final Creature devouredEnemy = deadCreaturesNotDevoured.getFirst();
            devouredEnemy.addCondition(new DevouredCondition());

            int healingAmount = D8.roll() + D8.roll() + D8.roll();
            actor.healDamage(healingAmount);
            log.info(
                    "{} is devoured by {} that is healed by {} hitpoints.",
                    devouredEnemy.getName(),
                    actor.getName(),
                    healingAmount
            );
        }
    }

    @Override
    public Action setPriority(int priority) {
        this.priority = priority;
        return this;
    }
}
