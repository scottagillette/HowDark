package com.redshift.ShadowDarkCalculator.targets;

import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.dice.SingleDie;

import java.util.List;

/**
 * Random target selector of a conscious non-dead targets, if any otherwise random.
 */

public class RandomTargetSelector implements SingleTargetSelector {

    @Override
    public Creature get(List<Creature> targetOptions) {
        Creature target = null;

        final List<Creature> consciousNotDeadTargets = targetOptions.stream()
                .filter(creature -> !creature.isUnconscious())
                .filter(creature -> !creature.isDead())
                .toList();

        if (consciousNotDeadTargets.isEmpty()) {
            final List<Creature> notDeadTargets = targetOptions.stream()
                    .filter(creature -> !creature.isDead())
                    .toList();

            if (!notDeadTargets.isEmpty()) {
                final SingleDie dice = new SingleDie(notDeadTargets.size());
                target = notDeadTargets.get(dice.roll() - 1);
            }
        } else {
            final SingleDie dice = new SingleDie(consciousNotDeadTargets.size());
            target = consciousNotDeadTargets.get(dice.roll() - 1);
        }

        return target;
    }

}
