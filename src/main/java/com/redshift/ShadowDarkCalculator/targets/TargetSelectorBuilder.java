package com.redshift.ShadowDarkCalculator.targets;

import java.util.function.Supplier;

public enum TargetSelectorBuilder {
    FOCUS_FIRE(FocusFireTargetSelector::new),
    RANDOM(RandomTargetSelector::new),
    HEAL(HealTargetSelector::new);

    private final Supplier<SingleTargetSelector> ctor;

    TargetSelectorBuilder(Supplier<SingleTargetSelector> ctor) {
        this.ctor = ctor;
    }

    public SingleTargetSelector build() {
        return ctor.get();
    }
}

