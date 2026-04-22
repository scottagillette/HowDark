package com.redshift.ShadowDarkCalculator.actions;

import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.encounter.CombatSimulator;

import java.util.List;

public class PerformAllActions extends BaseAction implements Action {

    private final List<Action> actions;

    public PerformAllActions(Action... actions) {
        super("Perform All Actions");
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
        // Choose all actions; but can't be lost and can be performed.

        final List<Action> filteredActions = actions.stream()
                .filter(action -> action.canPerform(actor, enemies, allies))
                .filter(action -> !action.isLost())
                .toList();

        if (!filteredActions.isEmpty()) {
            filteredActions.forEach(action -> action.perform(actor, enemies, allies, simulator));
        }
    }

}
