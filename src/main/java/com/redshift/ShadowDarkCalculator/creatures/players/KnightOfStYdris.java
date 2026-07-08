package com.redshift.ShadowDarkCalculator.creatures.players;

import com.redshift.ShadowDarkCalculator.actions.Action;
import com.redshift.ShadowDarkCalculator.creatures.CreatureLabel;
import com.redshift.ShadowDarkCalculator.creatures.Stats;
import com.redshift.ShadowDarkCalculator.targets.single.FocusFireTargetSelector;
import com.redshift.ShadowDarkCalculator.targets.SingleTargetSelector;

/**
 * Class specific player; Knight of St Ydris.
 */

public class KnightOfStYdris  extends Player {

    public KnightOfStYdris(
            String name,
            int level,
            Stats stats,
            int armorClass,
            int hitPoints,
            Action action) {

        super(name, level, stats, armorClass, hitPoints, action, new FocusFireTargetSelector());
        addLabels();
    }

    public KnightOfStYdris(
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
        getLabels().add(CreatureLabel.CASTER);
        getLabels().add(CreatureLabel.FRONT_LINE);
        getLabels().add(CreatureLabel.KNIGHT_OF_ST_YDRIS);
    }

    @Override
    public String getClassName() {
        return PlayerClass.KNIGHT_OF_ST_YDRIS.getClassName();
    }

}
