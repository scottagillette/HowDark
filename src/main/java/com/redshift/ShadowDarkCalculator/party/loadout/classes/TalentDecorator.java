package com.redshift.ShadowDarkCalculator.party.loadout.classes;

import com.redshift.ShadowDarkCalculator.creatures.Stats;
import com.redshift.ShadowDarkCalculator.party.loadout.Bonuses;

/**
 * A class of components that can select talents and decorate any bonuses from them.
 */

public interface TalentDecorator {

    /**
     * Decorate the specific bonuses with any that apply for the specific class.
     */

    void decorate(Stats stats, Bonuses bonuses);
}
