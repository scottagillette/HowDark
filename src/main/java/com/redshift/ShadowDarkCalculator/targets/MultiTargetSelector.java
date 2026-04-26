package com.redshift.ShadowDarkCalculator.targets;

import com.redshift.ShadowDarkCalculator.creatures.Creature;

import java.util.List;

/**
 * Defines a class of components that can select multiple target from a list of creatures.
 */

public interface MultiTargetSelector {

    /**
     * Given a set of options for a target select up to a certain amount.
     */

    List<Creature> getTargets(List<Creature> targetOptions, int maxTargets);

}
