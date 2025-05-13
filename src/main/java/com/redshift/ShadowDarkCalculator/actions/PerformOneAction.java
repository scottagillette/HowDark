package com.redshift.ShadowDarkCalculator.actions;

import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.dice.SingleDie;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class PerformOneAction implements Action {

    private final List<Action> actions;

    public PerformOneAction(Action... actions) {
        this.actions = List.of(actions);
    }

    public PerformOneAction(List<Action> actions) {
        this.actions = actions;
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
    public String getName() {
        return "";
    }

    @Override
    public int getPriority() {
        int priority = 1;

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
    public void perform(Creature actor, List<Creature> enemies, List<Creature> allies) {
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
                    action.perform(actor, enemies, allies);
                    break;
                }
            }
        }
    }

    @Override
    public Action setPriority(int priority) {
        // mkay
        return this;
    }
}
