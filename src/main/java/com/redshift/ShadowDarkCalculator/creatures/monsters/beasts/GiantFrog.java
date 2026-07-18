package com.redshift.ShadowDarkCalculator.creatures.monsters.beasts;

import com.redshift.ShadowDarkCalculator.actions.weapons.Weapon;
import com.redshift.ShadowDarkCalculator.creatures.CreatureLabel;
import com.redshift.ShadowDarkCalculator.creatures.Stats;
import com.redshift.ShadowDarkCalculator.creatures.monsters.Monster;
import com.redshift.ShadowDarkCalculator.dice.RollModifier;
import lombok.extern.slf4j.Slf4j;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D6;
import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D8;

/**
 * Human-sized frogs with warty skin and long, sticky tongues.
 * AC 12, HP 10, ATK 1 tongue and 1 bite +2 (1d6), MV near (swim)
 * S +2, D +2, C +1, I -3, W +0, Ch -3, AL N, LV 2
 * TODO: Tongue. 1 creature in near DC 12 DEX or pulled to close range.
 */

@Slf4j
public class GiantFrog  extends Monster {

    public GiantFrog(String name) {
        super(
                name,
                2,
                new Stats(14,14,12,4,10,4),
                12,
                D8.roll() + D8.roll() + 1,
                new Weapon("Bite", D6, RollModifier.STRENGTH).addPiercing()
        );
        getLabels().add(CreatureLabel.FRONT_LINE);
        getLabels().add(CreatureLabel.NEUTRAL);
    }

}
