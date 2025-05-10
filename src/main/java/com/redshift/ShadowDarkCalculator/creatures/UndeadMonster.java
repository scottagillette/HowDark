package com.redshift.ShadowDarkCalculator.creatures;

import com.redshift.ShadowDarkCalculator.actions.Action;
import com.redshift.ShadowDarkCalculator.targets.RandomTargetSelector;
import com.redshift.ShadowDarkCalculator.targets.SingleTargetSelector;

public class UndeadMonster extends Monster {

    public UndeadMonster(
            String name,
            int level,
            Stats stats,
            int armorClass,
            int hitPoints,
            Action action) {

        super(name, level, stats, armorClass, hitPoints, action, new RandomTargetSelector());
        getLabels().add(Label.UNDEAD);
    }

    public UndeadMonster(
            String name,
            int level,
            Stats stats,
            int armorClass,
            int hitPoints,
            Action action,
            SingleTargetSelector singleTargetSelector) {

        super(name, level, stats, armorClass, hitPoints, action, singleTargetSelector);
        getLabels().add(Label.UNDEAD);
    }

}
