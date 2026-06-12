package com.redshift.ShadowDarkCalculator.targets.multi;

import com.redshift.ShadowDarkCalculator.conditions.SleepingCondition;
import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.creatures.CreatureLabel;
import com.redshift.ShadowDarkCalculator.targets.MultiTargetSelector;

import java.util.Collections;
import java.util.List;

/**
 * Returns a randomized list of alive, awake and not undead creatures to a maximum number.
 */

public class AliveAwakeNotUndeadTargetSelector implements MultiTargetSelector {

    @Override
    public List<Creature> getTargets(List<Creature> targetOptions, int maxTargets) {
        final List<Creature> targets = new java.util.ArrayList<>(targetOptions.stream()
                .filter(creature -> !creature.getLabels().contains(CreatureLabel.UNDEAD)) // Not undead
                .filter(creature -> !creature.hasCondition(SleepingCondition.class.getName())) // Awake
                .filter(creature -> !creature.isUnconscious()) // Awake
                .filter(creature -> !creature.isDead()) // Alive
                .filter(creature -> !creature.hasFled())
                .toList());

        if (targets.isEmpty()) {
            return targets; // Return empty list.
        } else {
            Collections.shuffle(targets);
            return targets.subList(0, Math.min(targets.size(), maxTargets));
        }
    }

}