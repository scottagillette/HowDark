package com.redshift.ShadowDarkCalculator.creatures.monsters.goblinoid;

import com.redshift.ShadowDarkCalculator.actions.weapons.WeaponBuilder;
import com.redshift.ShadowDarkCalculator.creatures.CreatureLabel;
import com.redshift.ShadowDarkCalculator.creatures.monsters.Monster;
import com.redshift.ShadowDarkCalculator.creatures.Stats;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D8;

/**
 * A sturdy, tall goblin with russet skin. Militant and strategic.
 * AC 15 (chainmail + shield), HP 10, ATK 1 longsword +3 (1d8) or 1 longbow (far) +0 (1d8), MV near
 * S +3, D +0, C +1, I +2, W +1, Ch +1, AL C, LV 2
 * Phalanx. +1 to attacks and AC when in close range of an allied hobgoblin. // TODO: Not Implemented
 */

public class Hobgoblin extends Monster {

    public Hobgoblin(String name) {
        super(
                name,
                2,
                new Stats(16,10,12,14,12,12),
                15,
                D8.roll() + D8.roll() + 1,
                WeaponBuilder.LONGSWORD.build()
                // Skip longbow
        );
        getLabels().add(CreatureLabel.FRONT_LINE);
        getLabels().add(CreatureLabel.HUMANOID);
    }
}
