package com.redshift.ShadowDarkCalculator.targets;

import com.redshift.ShadowDarkCalculator.creatures.Creature;

import java.util.List;

public interface MultiTargetSelector {

    /**
     * Given a set of options for a target select up to a certain amount.
     */

    List<Creature> getTargets(List<Creature> targetOptions, int maxTargets);

}
