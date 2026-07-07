package com.redshift.ShadowDarkCalculator.party.loadout.actions;

import com.redshift.ShadowDarkCalculator.actions.Action;
import com.redshift.ShadowDarkCalculator.creatures.Stats;
import com.redshift.ShadowDarkCalculator.creatures.players.PlayerClass;
import com.redshift.ShadowDarkCalculator.party.loadout.Bonuses;

import java.util.HashMap;
import java.util.Map;

/**
 * Given a player class and stats randomly create actions with the appropriate bonuses.
 */

public class ActionSelector {

    private final Map<PlayerClass, ActionBuilder> actionBuilders = new HashMap<>();

    public ActionSelector() {
        actionBuilders.put(PlayerClass.BARD, new BardActionBuilder());
        actionBuilders.put(PlayerClass.FIGHTER, new FighterActionBuilder());
        actionBuilders.put(PlayerClass.PALADIN, new PaladinActionBuilder());
        actionBuilders.put(PlayerClass.PRIEST, new PriestActionBuilder());
        actionBuilders.put(PlayerClass.THIEF, new ThiefActionBuilder());
        actionBuilders.put(PlayerClass.WIZARD, new WizardActionBuilder());
    }

    /**
     * Build appropriate action(s) for the given class and class stats using the specified
     * bonuses.
     */

    public Action selectActions(PlayerClass playerClass, Stats stats, Bonuses bonuses) {
        final ActionBuilder actionBuilder = actionBuilders.get(playerClass);
        return actionBuilder.build(stats, bonuses);
    }

}
