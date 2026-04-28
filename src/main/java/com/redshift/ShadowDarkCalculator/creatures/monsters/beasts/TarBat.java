package com.redshift.ShadowDarkCalculator.creatures.monsters.beasts;

import com.redshift.ShadowDarkCalculator.actions.weapons.Weapon;
import com.redshift.ShadowDarkCalculator.creatures.monsters.Monster;
import com.redshift.ShadowDarkCalculator.creatures.Stats;
import com.redshift.ShadowDarkCalculator.dice.RollModifier;
import lombok.extern.slf4j.Slf4j;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.*;

/**
 * A pinwheeling bat that smells of sulfur. Excretes a black, tarry oil.
 * AC 13, HP 4, ATK 1 bite +3 (1d4), MV near (fly)
 * S -3, D +3, C +0, I -3, W +1, Ch -3, AL N, LV 1
 * Pyro. Seeks open flames, ignites on contact with them to deal 1d4 damage. TODO: Implement Pyro
 * Immune to fire.
 */

@Slf4j
public class TarBat extends Monster {

    public TarBat(String name) {
        super(
                name,
                1,
                new Stats(4,16,10,4,12,4),
                13,
                D6.roll(), // Usually use a D8 for random first level mob.
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
