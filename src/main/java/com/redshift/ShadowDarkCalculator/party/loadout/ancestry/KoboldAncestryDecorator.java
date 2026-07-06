package com.redshift.ShadowDarkCalculator.party.loadout.ancestry;

import com.redshift.ShadowDarkCalculator.creatures.players.PlayerClass;
import com.redshift.ShadowDarkCalculator.party.loadout.Bonuses;
import lombok.extern.slf4j.Slf4j;

/**
 * Kobold ancestry bonuses.
 */

@Slf4j
public class KoboldAncestryDecorator implements AncestryDecorator {

    @Override
    public void decorate(PlayerClass playerClass, Bonuses bonuses) {
        if (playerClass.isCaster()) {
            bonuses.addSpellCastingBonus();
        } else {
            bonuses.addLuckToken();
        }
    }
}
