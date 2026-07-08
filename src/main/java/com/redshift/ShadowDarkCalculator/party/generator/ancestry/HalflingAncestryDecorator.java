package com.redshift.ShadowDarkCalculator.party.generator.ancestry;

import com.redshift.ShadowDarkCalculator.creatures.players.PlayerClass;
import com.redshift.ShadowDarkCalculator.party.generator.Bonuses;
import lombok.extern.slf4j.Slf4j;

/**
 * Halfling ancestry bonuses.
 */

@Slf4j
public class HalflingAncestryDecorator implements AncestryDecorator {

    @Override
    public void decorate(PlayerClass playerClass, Bonuses bonuses) {
        // TODO: Implement halfling hiding once per day.
    }

}
