package com.redshift.ShadowDarkCalculator.targets;

import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.dice.SingleDie;

import java.util.List;

/**
 * Random target selector of a conscious target, if any.
 */

public class RandomTargetSelector implements SingleTargetSelector {

    @Override
    public Creature getTarget(List<Creature> targetOptions) {
        Creature target = null;

        final List<Creature> consciousTargets = targetOptions.stream()
                .filter(creature -> !creature.isUnconscious())
                .toList();

        if (!consciousTargets.isEmpty()) {
            final SingleDie dice = new SingleDie(consciousTargets.size());
            target = consciousTargets.get(dice.roll() - 1);
        }

        return target;
    }

}
