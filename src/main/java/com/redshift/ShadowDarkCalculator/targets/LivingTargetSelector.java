package com.redshift.ShadowDarkCalculator.targets;

import com.redshift.ShadowDarkCalculator.creatures.Creature;

import java.util.Collections;
import java.util.List;

/**
 * Returns a randomized list of living creatures that are not unconscious to a maximum number.
 */

public class LivingTargetSelector implements MultiTargetSelector {

    @Override
    public List<Creature> getTargets(List<Creature> targetOptions, int maxTargets) {
        final List<Creature> livingCreatures = new java.util.ArrayList<>(targetOptions.stream()
                .filter(creature -> !creature.isUndead())
                .filter(creature -> !creature.isUnconscious())
                .toList());

        Collections.shuffle(livingCreatures);

        if (livingCreatures.isEmpty()) {
            return livingCreatures;
        } else {
            return livingCreatures.subList(0, Math.min(livingCreatures.size(), maxTargets));
        }
    }

}