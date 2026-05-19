package com.redshift.ShadowDarkCalculator.targets.multi;

import com.redshift.ShadowDarkCalculator.conditions.FearCondition;
import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.targets.MultiTargetSelector;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Selects undead targets not dead and not feared already!
 */

public class TurnUndeadTargetSelector implements MultiTargetSelector {
    @Override
    public List<Creature> getTargets(List<Creature> targetOptions, int maxTargets) {
        final List<Creature> possibleTargets = new UndeadTargetSelector().getTargets(targetOptions, maxTargets);

        final ArrayList<Creature> actualTargets = new ArrayList<>(possibleTargets.stream()
                .filter(creature -> !creature.hasCondition(FearCondition.class.getName()))
                .toList());

        if (actualTargets.isEmpty()) {
            return actualTargets; // Return empty list.
        } else {
            Collections.shuffle(actualTargets);
            return actualTargets.subList(0, Math.min(actualTargets.size(), maxTargets));
        }
    }
}
