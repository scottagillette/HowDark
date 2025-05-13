package com.redshift.ShadowDarkCalculator.targets;

import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.creatures.Label;

import java.util.Collections;
import java.util.List;

/**
 * Returns a randomized list of (non-undead) living creatures that are not unconscious or dead to a maximum number.
 */

public class LivingTargetSelector implements MultiTargetSelector {

    @Override
    public List<Creature> getTargets(List<Creature> targetOptions, int maxTargets) {
        final List<Creature> livingCreatures = new java.util.ArrayList<>(targetOptions.stream()
                .filter(creature -> !creature.getLabels().contains(Label.UNDEAD))
                .filter(creature -> !creature.isUnconscious())
                .filter(creature -> !creature.isDead())
                .toList());

        if (livingCreatures.isEmpty()) {
            return livingCreatures; // Return empty list.
        } else {
            Collections.shuffle(livingCreatures);
            return livingCreatures.subList(0, Math.min(livingCreatures.size(), maxTargets));
        }
    }

}