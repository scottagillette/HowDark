package com.redshift.ShadowDarkCalculator.targets;

import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.dice.SingleDie;

import java.util.List;

/**
 * Returns a single target that has been damaged for it to be focused on otherwise a randome target. Skips all
 * unconscious targets.
 */

public class FocusFireTargetSelector implements SingleTargetSelector {

    @Override
    public Creature getTarget(List<Creature> targetOptions) {
        Creature target = null;

        final List<Creature> consciousTargets = targetOptions.stream()
                .filter(creature -> !creature.isUnconscious())
                .toList();

        if (!consciousTargets.isEmpty()) {
            final List<Creature> woundedCreatures = consciousTargets.stream()
                    .filter(Creature::isWounded)
                    .toList();

            if (woundedCreatures.isEmpty()) {
                final SingleDie dice = new SingleDie(consciousTargets.size());
                target = consciousTargets.get(dice.roll() - 1);
            } else {
                target = woundedCreatures.get(0);
            }
        }

        return target;
    }

}
