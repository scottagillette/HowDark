package com.redshift.ShadowDarkCalculator.targets;

import com.redshift.ShadowDarkCalculator.creatures.Creature;

import java.util.List;

/**
 * Defines a class of components that can select a single target from a list of creatures.
 */

public interface SingleTargetSelector {

    /**
     * Given a set of options for a target, select one, possibly null.
     */

    Creature get(List<Creature> targetOptions);

}
