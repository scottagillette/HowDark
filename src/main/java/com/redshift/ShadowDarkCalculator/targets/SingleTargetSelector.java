package com.redshift.ShadowDarkCalculator.targets;

import com.redshift.ShadowDarkCalculator.creatures.Creature;

import java.util.List;

public interface SingleTargetSelector {

    /**
     * Given a set of options for a target select one.
     */

    Creature getTarget(List<Creature> targetOptions);

}
