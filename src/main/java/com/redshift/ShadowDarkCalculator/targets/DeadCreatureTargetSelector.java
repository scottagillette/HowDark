package com.redshift.ShadowDarkCalculator.targets;

import com.redshift.ShadowDarkCalculator.conditions.DevouredCondition;
import com.redshift.ShadowDarkCalculator.creatures.Creature;

import java.util.Collections;
import java.util.List;

/**
 * Returns a random dead creature that isn't devoured; or null.
 */

public class DeadCreatureTargetSelector implements SingleTargetSelector {

    @Override
    public Creature get(List<Creature> targetOptions) {
        final List<Creature> deadCreatures = new java.util.ArrayList<>(targetOptions.stream()
                .filter(Creature::isDead)
                .filter(creature -> !creature.hasCondition(DevouredCondition.class.getName()))
                .toList());

        if (deadCreatures.isEmpty()) {
            return null;
        } else {
            Collections.shuffle(deadCreatures);
            return deadCreatures.getFirst();
        }
    }

}
