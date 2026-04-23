package com.redshift.ShadowDarkCalculator.targets;

import com.redshift.ShadowDarkCalculator.conditions.HolyWeaponCondition;
import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.creatures.CreatureLabel;

import java.util.Collections;
import java.util.List;

/**
 * Gets a random creature that is:
 * - Conscious
 * - Alive
 * - Does not have a magic weapon
 * - Does not have Holy Weapon already
 * - Is a Front Line character.
 */

public class HolyWeaponTargetSelector implements SingleTargetSelector {

    @Override
    public Creature get(List<Creature> targetOptions) {
        final List<Creature> creaturesWithoutHolyWeapon = new java.util.ArrayList<>(targetOptions
                .stream()
                .filter(creature -> !creature.isUnconscious())
                .filter(creature -> !creature.isDead())
                .filter(creature -> !creature.getAction().isMagicalWeapon())
                .filter(creature -> creature.getLabels().contains(CreatureLabel.FRONT_LINE))
                .filter(creature -> !creature.hasCondition(HolyWeaponCondition.class.getName()))
                .toList());

        if (creaturesWithoutHolyWeapon.isEmpty()) {
            return null;
        } else {
            Collections.shuffle(creaturesWithoutHolyWeapon);
            return creaturesWithoutHolyWeapon.getFirst();
        }
    }

}
