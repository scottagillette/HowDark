package com.redshift.ShadowDarkCalculator.targets;

import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.creatures.Label;

import java.util.Collections;
import java.util.List;

/**
 * Returns a randomized list of undead that are not unconscious or dead to a maximum number.
 */

public class UndeadTargetSelector implements MultiTargetSelector {

    @Override
    public List<Creature> getTargets(List<Creature> targetOptions, int maxTargets) {
        final List<Creature> undead = new java.util.ArrayList<>(targetOptions.stream()
                .filter(creature -> creature.getLabels().contains(Label.UNDEAD))
                .filter(creature -> !creature.isUnconscious())
                .filter(creature -> !creature.isDead())
                .toList());

        if (undead.isEmpty()) {
            return undead; // Return empty list.
        } else {
            Collections.shuffle(undead);
            return undead.subList(0, Math.min(undead.size(), maxTargets));
        }
    }

}