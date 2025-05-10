package com.redshift.ShadowDarkCalculator.creatures;

import com.redshift.ShadowDarkCalculator.actions.Action;
import com.redshift.ShadowDarkCalculator.targets.RandomTargetSelector;
import com.redshift.ShadowDarkCalculator.targets.SingleTargetSelector;

/**
 * Monsters extends form the BaseCreature class and add common labels for all monsters.
 */

public class Monster extends BaseCreature {

    public Monster(
            String name,
            int level,
            Stats stats,
            int armorClass,
            int hitPoints,
            Action action) {

        super(name, level, stats, armorClass, hitPoints, action, new RandomTargetSelector());
        getLabels().add(Label.MONSTER);
    }

    public Monster(
            String name,
            int level,
            Stats stats,
            int armorClass,
            int hitPoints,
            Action action,
            SingleTargetSelector singleTargetSelector) {

        super(name, level, stats, armorClass, hitPoints, action, singleTargetSelector);
        getLabels().add(Label.MONSTER);
    }

}
