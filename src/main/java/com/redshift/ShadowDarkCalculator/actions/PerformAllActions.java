package com.redshift.ShadowDarkCalculator.actions;

import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.encounter.Encounter;

import java.util.List;

/**
 * A composite action that performs all actions in sequence that can be performed.
 */

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
                .toList();

        return !canPerformActions.isEmpty();
    }

    @Override
    public void perform(Creature actor, List<Creature> enemies, List<Creature> allies, Encounter encounter) {
        // Choose all actions; but can't be lost and can be performed.

        final List<Action> canPerformActions = actions.stream()
                .filter(action -> action.canPerform(actor, enemies, allies))
                .toList();

        canPerformActions.forEach(action -> action.perform(actor, enemies, allies, encounter));
    }

}
