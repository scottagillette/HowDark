package com.redshift.ShadowDarkCalculator.actions;

/**
 * A abstract action that holds properties used by any type of action.
 */

public abstract class BaseAction implements Action {

    public BaseAction(String name) {
        this.name = name;
    }

    private final String name;
    private int priority = 1; // All actions default to priority 1.

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
        if (priority <= 0) throw new UnsupportedOperationException("Priority must be positive.");

        this.priority = priority;
        return this;
    }
}
