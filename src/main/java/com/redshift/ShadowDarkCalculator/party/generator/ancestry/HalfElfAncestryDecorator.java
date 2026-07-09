package com.redshift.ShadowDarkCalculator.party.generator.ancestry;

import com.redshift.ShadowDarkCalculator.creatures.players.PlayerClass;
import com.redshift.ShadowDarkCalculator.party.generator.Bonuses;
import lombok.extern.slf4j.Slf4j;

/**
 * Half-Elf ancestry bonuses.
 */

@Slf4j
public class HalfElfAncestryDecorator implements AncestryDecorator {

    @Override
    public void decorate(PlayerClass playerClass, Bonuses bonuses) {
        bonuses.addAdvantageOnTalentRoll();
    }
}
