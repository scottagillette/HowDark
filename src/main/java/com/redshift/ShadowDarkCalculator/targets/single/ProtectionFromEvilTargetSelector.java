package com.redshift.ShadowDarkCalculator.targets.single;

import com.redshift.ShadowDarkCalculator.actions.spells.ProtectionFromEvil;
import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.targets.SingleTargetSelector;

import java.util.Collections;
import java.util.List;

/**
 * Gets a random creature that is:
 * - Conscious
 * - Alive
 * - Does not have a protection from evil
 */

public class ProtectionFromEvilTargetSelector implements SingleTargetSelector {

    @Override
    public Creature get(List<Creature> targetOptions) {
        final List<Creature> creaturesWithoutProtection = new java.util.ArrayList<>(targetOptions
                .stream()
                .filter(creature -> !creature.isUnconscious())
                .filter(creature -> !creature.isDead())
                .filter(creature -> !creature.hasCondition(ProtectionFromEvil.class.getName()))
                .toList());

        if (creaturesWithoutProtection.isEmpty()) {
            return null;
        } else {
            Collections.shuffle(creaturesWithoutProtection);
            return creaturesWithoutProtection.getFirst();
        }
    }

}
