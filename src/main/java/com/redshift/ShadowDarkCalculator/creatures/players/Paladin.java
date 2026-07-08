package com.redshift.ShadowDarkCalculator.creatures.players;

import com.redshift.ShadowDarkCalculator.actions.Action;
import com.redshift.ShadowDarkCalculator.conditions.InspiredCondition;
import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.creatures.CreatureLabel;
import com.redshift.ShadowDarkCalculator.creatures.Stats;
import com.redshift.ShadowDarkCalculator.encounter.Encounter;
import com.redshift.ShadowDarkCalculator.targets.single.FocusFireTargetSelector;
import com.redshift.ShadowDarkCalculator.targets.SingleTargetSelector;

import java.util.List;

/**
 * Class specific player; Paladin.
 */

public class Paladin extends Player {

    public Paladin(
            String name,
            int level,
            Stats stats,
            int armorClass,
            int hitPoints,
            Action action) {

        super(name, level, stats, armorClass, hitPoints, action, new FocusFireTargetSelector());
        addLabels();
    }

    public Paladin(
            String name,
            int level,
            Stats stats,
            int armorClass,
            int hitPoints,
            Action action,
            SingleTargetSelector singleTargetSelector) {

        super(name, level, stats, armorClass, hitPoints, action, singleTargetSelector);
        addLabels();
    }

    private void addLabels() {
        getLabels().add(CreatureLabel.FRONT_LINE);
        getLabels().add(CreatureLabel.PALADIN);
    }

    @Override
    public String getClassName() {
        return PlayerClass.PALADIN.getClassName();
    }

    @Override
    public void takePreCombatTurn(List<Creature> enemies, List<Creature> allies, Encounter encounter) {
        // Inspiring Presence. Allies within near of you rise from dying on a
        // roll of 18-20 and regain HP equal to your CHA bonus (min. 1).
        final int hpBonus = Math.max(1, getStats().getCharismaModifier());

        for (Creature ally : allies) {
            ally.addCondition(new InspiredCondition(hpBonus));
        }
    }

}
