package com.redshift.ShadowDarkCalculator.creatures.players;

import com.redshift.ShadowDarkCalculator.actions.Action;
import com.redshift.ShadowDarkCalculator.creatures.CreatureLabel;
import com.redshift.ShadowDarkCalculator.creatures.Stats;
import com.redshift.ShadowDarkCalculator.targets.single.FocusFireTargetSelector;
import com.redshift.ShadowDarkCalculator.targets.SingleTargetSelector;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D6;

/**
 * Class specific player; Necromancer.
 */

public class Necromancer extends Player {

    public Necromancer(
            String name,
            int level,
            Stats stats,
            int armorClass,
            int hitPoints,
            Action action) {

        super(name, level, stats, armorClass, hitPoints, action, new FocusFireTargetSelector());

        getLabels().add(CreatureLabel.NECROMANCER);
        getLabels().add(CreatureLabel.CASTER);
        getLabels().add(CreatureLabel.BACKLINE);

        // River of Death ...and you roll a d6 for your death timer instead of a d4.
        setDyingDice(D6);
    }

    public Necromancer(
            String name,
            int level,
            Stats stats,
            int armorClass,
            int hitPoints,
            Action action,
            SingleTargetSelector singleTargetSelector) {

        super(name, level, stats, armorClass, hitPoints, action, singleTargetSelector);
        getLabels().add(CreatureLabel.NECROMANCER);
        getLabels().add(CreatureLabel.CASTER);
        getLabels().add(CreatureLabel.BACKLINE);

        // River of Death ...and you roll a d6 for your death timer instead of a d4.
        setDyingDice(D6);
    }

}
