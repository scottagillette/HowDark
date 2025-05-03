package com.redshift.ShadowDarkCalculator.creatures;

import com.redshift.ShadowDarkCalculator.actions.Action;
import com.redshift.ShadowDarkCalculator.targets.FocusFireTargetSelector;
import com.redshift.ShadowDarkCalculator.targets.SingleTargetSelector;

public class Character extends BaseCreature {

    public Character(String name, int level, Stats stats, int armorClass, int hitPoints, Action action) {
        super(name, level, false, false, stats, armorClass, hitPoints, action, new FocusFireTargetSelector());
    }

    public Character(String name, int level, Stats stats, int armorClass, int hitPoints, Action action, SingleTargetSelector singleTargetSelector) {
        super(name, level, false, false, stats, armorClass, hitPoints, action, singleTargetSelector);
    }
}
