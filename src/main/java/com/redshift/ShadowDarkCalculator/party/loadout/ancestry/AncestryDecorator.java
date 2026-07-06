package com.redshift.ShadowDarkCalculator.party.loadout.ancestry;

import com.redshift.ShadowDarkCalculator.creatures.players.PlayerClass;
import com.redshift.ShadowDarkCalculator.party.loadout.Bonuses;

public interface AncestryDecorator {

    /**
     * Decorate the specific bonuses with any that apply for the specific ancestry and class specified.
     */

    void decorate(PlayerClass playerClass, Bonuses bonuses);
}
