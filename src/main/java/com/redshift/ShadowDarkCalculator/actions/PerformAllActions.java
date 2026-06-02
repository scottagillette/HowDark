package com.redshift.ShadowDarkCalculator.actions;

import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.encounter.Encounter;

import java.util.List;

/**
 * A composite action that performs all actions in sequence that can be performed.
 */

public final class PerformAllActions extends BaseAction implements Action {

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
    public boolean isMagicalWeapon() {
        // If one or more actions are magical weapons return true.
        final List<Action> magicalWeapons = actions.stream()
                .filter(Action::isMagicalWeapon)
                .toList();

        return !magicalWeapons.isEmpty();
    }

    @Override
    public void perform(Creature actor, List<Creature> enemies, List<Creature> allies, Encounter encounter) {
        actions.forEach(action -> {
            // Some actions can be lost as other actions are performed so don't
            // filter, just execute if you can.
            if (action.canPerform(actor, enemies, allies)) {
                action.perform(actor, enemies, allies, encounter);
            }
        });
    }

}
