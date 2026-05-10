package com.redshift.ShadowDarkCalculator.actions;

/**
 * A abstract action that holds properties used by any type of action.
 */

public abstract class BaseAction implements Action {

    protected final String name;
    protected int priority = 1; // All actions default to priority 1.

    public BaseAction(String name) {
        if (name == null || name.isEmpty()) throw new UnsupportedOperationException("An action name must specified.");
        this.name = name;
    }


    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    public Action setPriority(int priority) {
        if (priority <= 0) throw new UnsupportedOperationException("An action priority must be positive.");

        this.priority = priority;
        return this;
    }

}
