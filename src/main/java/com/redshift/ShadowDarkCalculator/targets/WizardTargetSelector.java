package com.redshift.ShadowDarkCalculator.targets;

import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.creatures.CreatureLabel;
import com.redshift.ShadowDarkCalculator.dice.SingleDie;

import java.util.List;

/**
 * Returns a random Wizard that is conscious and alive; or null if one is not found.
 */

public class WizardTargetSelector implements SingleTargetSelector {

    @Override
    public Creature get(List<Creature> targetOptions) {
        final List<Creature> wizards = targetOptions.stream()
                .filter(creature -> creature.getLabels().contains(CreatureLabel.WIZARD))
                .filter(creature -> !creature.isUnconscious())
                .filter(creature -> !creature.isDead())
                .toList();

        if (wizards.isEmpty()) {
            return null;
        } else {
            final SingleDie dice = new SingleDie(wizards.size());
            return wizards.get(dice.roll() - 1);
        }
    }

}
