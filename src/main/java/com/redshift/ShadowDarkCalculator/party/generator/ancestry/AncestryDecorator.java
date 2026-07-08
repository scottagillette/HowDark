package com.redshift.ShadowDarkCalculator.party.generator.ancestry;

import com.redshift.ShadowDarkCalculator.creatures.players.PlayerClass;
import com.redshift.ShadowDarkCalculator.party.generator.Bonuses;

public interface AncestryDecorator {

    /**
     * Decorate the specific bonuses with any that apply for the specific ancestry and class specified.
     */

    void decorate(PlayerClass playerClass, Bonuses bonuses);
}
