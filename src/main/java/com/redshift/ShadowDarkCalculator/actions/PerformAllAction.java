package com.redshift.ShadowDarkCalculator.actions;

import com.redshift.ShadowDarkCalculator.creatures.Creature;

import java.util.List;

public class PerformAllAction implements Action {

    private final List<Action> actions;

    public PerformAllAction(Action... actions) {
        this.actions = List.of(actions);
    }

    public PerformAllAction(List<Action> actions) {
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
    public boolean isLost() {
        final List<Action> filteredActions = actions.stream()
                .filter(action -> !action.isLost())
                .toList();

        return filteredActions.isEmpty();
    }

    @Override
    public void perform(Creature actor, List<Creature> enemies, List<Creature> allies) {
        // Choose all actions; but can't be lost and can be performed.

        final List<Action> filteredActions = actions.stream()
                .filter(action -> action.canPerform(actor, enemies, allies))
                .filter(action -> !action.isLost())
                .toList();

        if (!filteredActions.isEmpty()) {
            filteredActions.forEach(action -> action.perform(actor, enemies, allies));
        }
    }
}
