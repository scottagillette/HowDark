package com.redshift.ShadowDarkCalculator.actions;

import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.dice.SingleDie;

import java.util.List;

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
    public boolean isLost() {
        final List<Action> filteredActions = actions.stream()
                .filter(action -> !action.isLost())
                .toList();

        return filteredActions.isEmpty();
    }

    @Override
    public void perform(Creature actor, List<Creature> enemies, List<Creature> allies) {
        // Choose 1 action at random; but can't be lost and can be performed.

        final List<Action> filteredActions = actions.stream()
                .filter(action -> action.canPerform(actor, enemies, allies))
                .filter(action -> !action.isLost())
                .toList();

        if (!filteredActions.isEmpty()) {
            final SingleDie singleDie = new SingleDie(filteredActions.size());
            filteredActions.get(singleDie.roll() - 1).perform(actor, enemies, allies);
        }
    }
}
