package com.redshift.ShadowDarkCalculator.targets;

import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.creatures.CreatureLabel;

import java.util.Collections;
import java.util.List;

/**
 * Select from the target options creatures with the given label.
 */

public class CreatureLabelTargetSelector implements MultiTargetSelector {

    private final CreatureLabel label;

    public CreatureLabelTargetSelector(CreatureLabel label) {
        this.label = label;
    }

    @Override
    public List<Creature> getTargets(List<Creature> targetOptions, int maxTargets) {
        final List<Creature> labelledCreatures = new java.util.ArrayList<>(targetOptions.stream()
                .filter(creature -> creature.getLabels().contains(label))
                .toList());

        if (labelledCreatures.isEmpty()) {
            return labelledCreatures; // Return empty list.
        } else {
            Collections.shuffle(labelledCreatures);
            return labelledCreatures.subList(0, Math.min(labelledCreatures.size(), maxTargets));
        }
    }
}
