package com.redshift.ShadowDarkCalculator.party.generator.ancestry;

import com.redshift.ShadowDarkCalculator.creatures.players.PlayerClass;
import com.redshift.ShadowDarkCalculator.party.generator.Bonuses;
import lombok.extern.slf4j.Slf4j;

/**
 * Dwarf ancestry bonuses.
 */

@Slf4j
public class DwarfAncestryDecorator implements AncestryDecorator {

    @Override
    public void decorate(PlayerClass playerClass, Bonuses bonuses) {
        bonuses.addHitPointAdvantageRoll();
        bonuses.addHitPointsBonus();
        bonuses.addHitPointsBonus();
    }
}
