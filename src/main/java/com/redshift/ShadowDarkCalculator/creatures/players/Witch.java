package com.redshift.ShadowDarkCalculator.creatures.players;

import com.redshift.ShadowDarkCalculator.actions.Action;
import com.redshift.ShadowDarkCalculator.creatures.CreatureLabel;
import com.redshift.ShadowDarkCalculator.creatures.Stats;
import com.redshift.ShadowDarkCalculator.targets.single.FocusFireTargetSelector;
import com.redshift.ShadowDarkCalculator.targets.SingleTargetSelector;

/**
 * Class specific player; Witch.
 */

public class Witch extends Player {

    public Witch(
            String name,
            int level,
            Stats stats,
            int armorClass,
            int hitPoints,
            Action action) {

        super(name, level, stats, armorClass, hitPoints, action, new FocusFireTargetSelector());
        getLabels().add(CreatureLabel.WITCH);
        getLabels().add(CreatureLabel.CASTER);
        getLabels().add(CreatureLabel.BACKLINE);
    }

    public Witch(
            String name,
            int level,
            Stats stats,
            int armorClass,
            int hitPoints,
            Action action,
            SingleTargetSelector singleTargetSelector) {

        super(name, level, stats, armorClass, hitPoints, action, singleTargetSelector);
        getLabels().add(CreatureLabel.WITCH);
        getLabels().add(CreatureLabel.CASTER);
        getLabels().add(CreatureLabel.BACKLINE);
    }

    @Override
    public String toString() {
        final String value = super.toString();
        final String template = "%s, Witch\n%s";
        return String.format(template, getName(), value);
    }
}
