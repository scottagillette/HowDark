package com.redshift.ShadowDarkCalculator.targets;

import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.dice.SingleDie;

import java.util.List;

/**
 * Returns a single target for a healing; prioritizing unconscious targets first; then simply wounded.
 */

public class HealTargetSelector implements SingleTargetSelector {

    @Override
    public Creature get(List<Creature> targetOptions) {
        final List<Creature> unconscious = targetOptions.stream()
                .filter(Creature::isUnconscious)
                .toList();

        if (unconscious.isEmpty()) {
            final List<Creature> woundedAndNotDead = targetOptions.stream()
                    .filter(Creature::isWounded)
                    .filter(creature -> !creature.isDead())
                    .toList();

            if (woundedAndNotDead.isEmpty()) return null;

            int selectionIndex = new SingleDie(woundedAndNotDead.size()).roll();
            return woundedAndNotDead.get(selectionIndex - 1); // Randomly choose a wounded creature
        } else {
            int selectionIndex = new SingleDie(unconscious.size()).roll();
            return unconscious.get(selectionIndex - 1); // Randomly choose an unconscious creature
        }
    }

}
