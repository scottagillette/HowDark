package com.redshift.ShadowDarkCalculator.creatures.monsters.undead;

import com.redshift.ShadowDarkCalculator.actions.weapons.Weapon;
import com.redshift.ShadowDarkCalculator.creatures.CreatureLabel;
import com.redshift.ShadowDarkCalculator.creatures.Stats;
import com.redshift.ShadowDarkCalculator.creatures.monsters.UndeadMonster;
import com.redshift.ShadowDarkCalculator.dice.RollModifier;
import lombok.extern.slf4j.Slf4j;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.*;

/**
 * Lurching and decomposed undead that hunt in mobs.
 * AC 8, HP 11, ATK 1 slam +2 (1d6), MV near
 * S +2, D -2, C +2, I -2, W -2, Ch -3, AL C, LV 2
 * Undead. Immune to morale checks.
 * Relentless. If zombie reduced to 0 HP by a non-magical source, DC 15 CON to go to 1 HP instead.
 */

@Slf4j
public class Zombie extends UndeadMonster {

    private boolean returned = false; // Come back from the dead again?

    public Zombie(String name) {
        super(
                name,
                2,
                new Stats(14, 6, 14, 6, 6, 4),
                8,
                D8.roll() + D8.roll() + 2,
                new Weapon("Slam", D6, RollModifier.STRENGTH)
        );
        getLabels().add(CreatureLabel.FRONT_LINE);
        getLabels().add(CreatureLabel.HUMANOID);
    }

    @Override
    public void takeDamage(int amount, boolean silvered, boolean magical, boolean fire, boolean cold) {
        // Does the Zombie get back up after taking damage!

        super.takeDamage(amount, silvered, magical, fire, cold);

        // If it is now dead from a non-magical source and has not already returned...
        if (isDead() && !magical && !returned) {
            if (this.getStats().constitutionSave(15)) {
                log.info("{} seems to be killed ... but slowly stands back up!", getName());
                setDead(false);
                healDamage(1);
                returned = true;
            }
        }
    }

}