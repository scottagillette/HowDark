package com.redshift.ShadowDarkCalculator.targets;

import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.dice.SingleDie;

import java.util.List;

/**
 * Returns a single target for a healing; prioritizing unconscious targets first; then simply wounded.
 */

public class HealTargetSelector implements SingleTargetSelector {

    @Override
    public Creature getTarget(List<Creature> targetOptions) {
        final List<Creature> unconscious = targetOptions.stream().filter(Creature::isUnconscious).toList();

        if (unconscious.isEmpty()) {
            final List<Creature> wounded = targetOptions.stream().filter(Creature::isWounded).toList();
            if (wounded.isEmpty()) return null;

            int selectionIndex = new SingleDie(wounded.size()).roll();
            return wounded.get(selectionIndex - 1); // Randomly choose a wounded creature
        } else {
            int selectionIndex = new SingleDie(unconscious.size()).roll();
            return unconscious.get(selectionIndex - 1); // Randomly choose an unconscious creature
        }
    }

}
