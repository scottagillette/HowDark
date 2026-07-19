package com.redshift.ShadowDarkCalculator.creatures.monsters.monstrosities;

import com.redshift.ShadowDarkCalculator.actions.DamageType;
import com.redshift.ShadowDarkCalculator.actions.PerformAllActions;
import com.redshift.ShadowDarkCalculator.actions.weapons.Weapon;
import com.redshift.ShadowDarkCalculator.creatures.CreatureLabel;
import com.redshift.ShadowDarkCalculator.creatures.Stats;
import com.redshift.ShadowDarkCalculator.creatures.monsters.Monster;
import com.redshift.ShadowDarkCalculator.dice.RollModifier;
import com.redshift.ShadowDarkCalculator.resistance.NonMagicalImmunity;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D6;
import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D8;

/**
 * Leering, winged fiends that look like stone statues. They can hold
 * perfectly still for long stretches of time.
 * AC 16, HP 20, ATK 2 claw +3 (1d6), MV near (fly)
 * S +3, D +1, C +2, I +0, W +1, Ch -1, AL C, LV 4
 * Impervious. Only damaged bymagical sources.
 */

public class Gargoyle extends Monster {

    public Gargoyle(String name) {
        super(
                name,
                4,
                new Stats(16,12,14,10,12,8),
                16,
                D8.roll() + D8.roll() + D8.roll() + D8.roll()  + 2,
                new PerformAllActions(
                        new Weapon("Claws", D6, RollModifier.STRENGTH).addSlashing(),
                        new Weapon("Claws", D6, RollModifier.STRENGTH).addSlashing()
                )
        );
        getLabels().add(CreatureLabel.FRONT_LINE);
        getLabels().add(CreatureLabel.CHAOTIC);
    }

    @Override
    public void takeDamage(int amount, DamageType damageType) {
        // Only magical damage!
        final int damage = new NonMagicalImmunity().calculateDamage(this, amount, damageType);
        if (damage != 0) {
            super.takeDamage(damage, damageType);
        }
    }

}
