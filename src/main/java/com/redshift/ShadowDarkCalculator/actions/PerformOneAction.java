package com.redshift.ShadowDarkCalculator.actions;

import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.dice.SingleDie;
import com.redshift.ShadowDarkCalculator.encounter.CombatSimulator;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class PerformOneAction extends BaseAction implements Action {

    private final List<Action> actions;

    public PerformOneAction(Action... actions) {
        super("Perform One Action");
        this.actions = List.of(actions);
    }

    @Override
    public boolean canPerform(Creature actor, List<Creature> enemies, List<Creature> allies) {
        final List<Action> canPerformActions = actions.stream()
                .filter(action -> action.canPerform(actor, enemies, allies))
                .filter(action -> !action.isLost())
                .toList();

        return !canPerformActions.isEmpty();
    }

    @Override
    public int getPriority() {
        int priority = 1; // Default priority of all actions.

        for (Action action : actions) {
            priority = Math.max(priority, action.getPriority());
        }

        return priority;
    }

    @Override
    public boolean isLost() {
        final List<Action> filteredActions = actions.stream()
                .filter(action -> !action.isLost())
                .toList();

        return filteredActions.isEmpty();
    }

    @Override
    public boolean isMagical() {
        // TODO: Does this need to check?
        return false;
    }

    @Override
    public void perform(Creature actor, List<Creature> enemies, List<Creature> allies, CombatSimulator simulator) {
        // Choose 1 action based on priority; but can't be lost and can be performed.

        final List<Action> availableActions = actions.stream()
                .filter(action -> action.canPerform(actor, enemies, allies))
                .filter(action -> !action.isLost())
                .toList();

        if (!availableActions.isEmpty()) {
            int maxPriority = 0;

            for (Action action : availableActions) {
                maxPriority = maxPriority + action.getPriority();
            }

            final int priorityRoll = new SingleDie(maxPriority).roll();

            int sum = 0;

            for (Action action : availableActions) {
                final int actionPriority = action.getPriority();
                sum = sum + actionPriority;

                if (priorityRoll >= sum - (actionPriority - 1) && priorityRoll <= sum) {
                    action.perform(actor, enemies, allies, simulator);
                    break;
                }
            }
        }
    }

}
