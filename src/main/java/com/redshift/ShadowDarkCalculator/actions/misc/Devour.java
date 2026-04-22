package com.redshift.ShadowDarkCalculator.actions.misc;

import com.redshift.ShadowDarkCalculator.actions.Action;
import com.redshift.ShadowDarkCalculator.actions.BaseAction;
import com.redshift.ShadowDarkCalculator.conditions.DevouredCondition;
import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.encounter.CombatSimulator;
import com.redshift.ShadowDarkCalculator.targets.DeadCreatureTargetSelector;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D8;

/**
 * The Devour action, eat a dead body and heal for 3d8.
 */

@Slf4j
public class Devour extends BaseAction implements Action {

    public Devour() {
        super("Devour");
    }

    @Override
    public boolean canPerform(Creature actor, List<Creature> enemies, List<Creature> allies) {
        // Must have a dead target and this creature is wounded.
        final Creature deadEnemy = new DeadCreatureTargetSelector().get(enemies);
        return actor.isWounded() && deadEnemy != null;
    }

    @Override
    public void perform(Creature actor, List<Creature> enemies, List<Creature> allies, CombatSimulator simulator) {
        final Creature deadEnemy = new DeadCreatureTargetSelector().get(enemies);

        deadEnemy.addCondition(new DevouredCondition());

        int healingAmount = D8.roll() + D8.roll() + D8.roll();
        actor.healDamage(healingAmount);
        log.info(
                "{} is devoured by {} that is healed by {} hit points.",
                deadEnemy.getName(),
                actor.getName(),
                healingAmount
        );
    }

}
