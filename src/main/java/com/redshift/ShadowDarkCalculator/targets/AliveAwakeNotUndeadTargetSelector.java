package com.redshift.ShadowDarkCalculator.targets;

import com.redshift.ShadowDarkCalculator.conditions.SleepingCondition;
import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.creatures.CreatureLabel;

import java.util.Collections;
import java.util.List;

/**
 * Returns a randomized list of alive, awake and not undead creatures to a maximum number.
 */

public class AliveAwakeNotUndeadTargetSelector implements MultiTargetSelector {

    @Override
    public List<Creature> getTargets(List<Creature> targetOptions, int maxTargets) {
        final List<Creature> livingCreatures = new java.util.ArrayList<>(targetOptions.stream()
                .filter(creature -> !creature.getLabels().contains(CreatureLabel.UNDEAD)) // Not undead
                .filter(creature -> !creature.hasCondition(SleepingCondition.class.getName())) // Awake
                .filter(creature -> !creature.isUnconscious()) // Awake
                .filter(creature -> !creature.isDead()) // Alive
                .toList());

        if (livingCreatures.isEmpty()) {
            return livingCreatures; // Return empty list.
        } else {
            Collections.shuffle(livingCreatures);
            return livingCreatures.subList(0, Math.min(livingCreatures.size(), maxTargets));
        }
    }

}