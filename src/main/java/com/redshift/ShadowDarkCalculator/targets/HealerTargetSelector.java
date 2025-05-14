package com.redshift.ShadowDarkCalculator.targets;

import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.creatures.Label;
import com.redshift.ShadowDarkCalculator.dice.SingleDie;

import java.util.List;

/**
 * Returns a random healer that is not unconscious or dead; or null if one not found.
 */

public class HealerTargetSelector implements SingleTargetSelector {

    @Override
    public Creature get(List<Creature> targetOptions) {
        final List<Creature> casters = targetOptions.stream()
                .filter(creature -> creature.getLabels().contains(Label.HEALER))
                .filter(creature -> !creature.isUnconscious())
                .filter(creature -> !creature.isDead())
                .toList();

        if (casters.isEmpty()) {
            return null;
        } else {
            final SingleDie dice = new SingleDie(casters.size());
            return casters.get(dice.roll() - 1);
        }
    }

}
