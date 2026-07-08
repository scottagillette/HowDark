package com.redshift.ShadowDarkCalculator.party.generator.actions;

import com.redshift.ShadowDarkCalculator.actions.Action;
import com.redshift.ShadowDarkCalculator.creatures.Stats;
import com.redshift.ShadowDarkCalculator.party.generator.Bonuses;

public interface ActionBuilder {

    /**
     * Return an action buildout for a given class.
     */

    Action build(Stats stats, Bonuses bonuses);

}
