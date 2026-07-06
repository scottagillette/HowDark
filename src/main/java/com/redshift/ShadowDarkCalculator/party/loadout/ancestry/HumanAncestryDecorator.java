package com.redshift.ShadowDarkCalculator.party.loadout.ancestry;

import com.redshift.ShadowDarkCalculator.creatures.players.PlayerClass;
import com.redshift.ShadowDarkCalculator.party.loadout.Bonuses;
import lombok.extern.slf4j.Slf4j;

/**
 * Human ancestry bonuses.
 */

@Slf4j
public class HumanAncestryDecorator implements AncestryDecorator {

    @Override
    public void decorate(PlayerClass playerClass, Bonuses bonuses) {
        bonuses.addExtraTalentRoll();
    }
}
