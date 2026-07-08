package com.redshift.ShadowDarkCalculator.creatures.players;

import com.redshift.ShadowDarkCalculator.actions.Action;
import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.creatures.CreatureLabel;
import com.redshift.ShadowDarkCalculator.creatures.Stats;
import com.redshift.ShadowDarkCalculator.encounter.Encounter;
import com.redshift.ShadowDarkCalculator.targets.single.FocusFireTargetSelector;
import com.redshift.ShadowDarkCalculator.targets.SingleTargetSelector;

import java.util.List;

/**
 * Class specific player; Ranger.
 */

public class Ranger extends Player {

    public Ranger(
            String name,
            int level,
            Stats stats,
            int armorClass,
            int hitPoints,
            Action action) {

        super(name, level, stats, armorClass, hitPoints, action, new FocusFireTargetSelector());
        getLabels().add(CreatureLabel.RANGER);
        getLabels().add(CreatureLabel.FRONT_LINE);
    }

    public Ranger(
            String name,
            int level,
            Stats stats,
            int armorClass,
            int hitPoints,
            Action action,
            SingleTargetSelector singleTargetSelector) {

        super(name, level, stats, armorClass, hitPoints, action, singleTargetSelector);
        getLabels().add(CreatureLabel.RANGER);
        getLabels().add(CreatureLabel.FRONT_LINE);
    }

    @Override
    public String getClassName() {
        return PlayerClass.RANGER.getClassName();
    }

    @Override
    public void takePreCombatTurn(List<Creature> enemies, List<Creature> allies, Encounter encounter) {
        // TODO: Random chance to have a remidy?
    }

}
