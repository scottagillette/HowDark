package com.redshift.ShadowDarkCalculator.creatures.players;

import com.redshift.ShadowDarkCalculator.actions.Action;
import com.redshift.ShadowDarkCalculator.creatures.BaseCreature;
import com.redshift.ShadowDarkCalculator.creatures.CreatureLabel;
import com.redshift.ShadowDarkCalculator.creatures.Stats;
import com.redshift.ShadowDarkCalculator.targets.single.FocusFireTargetSelector;
import com.redshift.ShadowDarkCalculator.targets.SingleTargetSelector;

/**
 * Useful constructors for characters, not undead, not a monster, focus fire target selector.
 */

public class Player extends BaseCreature {

    public Player(
            String name,
            int level,
            Stats stats,
            int armorClass,
            int hitPoints,
            Action action) {

        super(name, level, stats, armorClass, hitPoints, action, new FocusFireTargetSelector());
        getLabels().add(CreatureLabel.PLAYER);
        getLabels().add(CreatureLabel.HUMANOID);

        setWillFlee(false); // Players do not flee!
    }

    public Player(
            String name,
            int level,
            Stats stats,
            int armorClass,
            int hitPoints,
            Action action,
            SingleTargetSelector singleTargetSelector) {

        super(name, level, stats, armorClass, hitPoints, action, singleTargetSelector);
        getLabels().add(CreatureLabel.PLAYER);
        getLabels().add(CreatureLabel.HUMANOID);

        setWillFlee(false); // Players do not flee!
    }

    @Override
    public String toString() {
        String template = """
                AC %d, HP %d
                %s, AL N, LV %d
                """;

        /**
         * AC 16, HP 39, ATK 2 tentacle
         * (near) +5 (1d8 + curse) or 1 tail +5
         * (3d6), MV near (swim), S +4, D -1,
         * C +3, I +4, W +2, Ch +2, AL C, LV 8
         */
        return String.format(template, getAC(), getMaxHitPoints(), getStats().toString(), getLevel());
    }

}
