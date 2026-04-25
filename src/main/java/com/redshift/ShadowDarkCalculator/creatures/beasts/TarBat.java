package com.redshift.ShadowDarkCalculator.creatures.beasts;

import com.redshift.ShadowDarkCalculator.actions.weapons.Weapon;
import com.redshift.ShadowDarkCalculator.creatures.Monster;
import com.redshift.ShadowDarkCalculator.creatures.Stats;
import com.redshift.ShadowDarkCalculator.dice.RollModifier;
import lombok.extern.slf4j.Slf4j;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.*;

/**
 * A pinwheeling bat that smells of sulfur. Excretes a black, tarry oil.
 * TODO: No Pyro ability implemented.
 */

@Slf4j
public class TarBat extends Monster {

    public TarBat(String name) {
        super(
                name,
                1,
                new Stats(4,16,10,4,12,4),
                13,
                D6.roll(),
                new Weapon("Bite", D4, RollModifier.DEXTERITY)
        );
    }

    @Override
    public void takeDamage(int amount, boolean silvered, boolean magical, boolean fire, boolean cold) {
        if (fire) {
            log.info("{} takes no damage from fire!", getName());
        } else {
            super.takeDamage(amount, silvered, magical, false, cold);
        }
    }

}
