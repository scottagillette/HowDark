package com.redshift.ShadowDarkCalculator.creatures.undead;

import com.redshift.ShadowDarkCalculator.actions.weapons.Weapon;
import com.redshift.ShadowDarkCalculator.creatures.BaseCreature;
import com.redshift.ShadowDarkCalculator.creatures.Stats;
import com.redshift.ShadowDarkCalculator.dice.RollModifier;
import com.redshift.ShadowDarkCalculator.targets.RandomTargetSelector;
import lombok.extern.slf4j.Slf4j;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.*;

@Slf4j
public class Zombie extends BaseCreature {

    private boolean returned = false;

    public Zombie(String name) {
        super(
                name,
                2,
                true,
                true,
                new Stats(14, 6, 14, 6, 6, 4),
                8,
                D8.roll() + D8.roll() + 2,
                new Slam(),
                new RandomTargetSelector()
        );
    }

    @Override
    public void takeDamage(int amount, boolean silvered, boolean magical, boolean fire, boolean cold) {
        // Does the Zombie get back up after taking damage!

        super.takeDamage(amount, silvered, magical, fire, cold);

        // If it is now dead from a non-magical source and has not already returned...
        if (isDead() && !magical && !returned) {
            if (this.getStats().constitutionSave(15)) {
                log.info(getName() + " seems to be killed ... but slowly stands back up!");
                setDead(false);
                healDamage(1);
                returned = true;
            }
        }
    }

    private static class Slam extends Weapon {

        public Slam() {
            super("Slam", D6, RollModifier.STRENGTH);
        }

    }
}