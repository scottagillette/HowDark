package com.redshift.ShadowDarkCalculator.creatures.monsters.monstrosities;

import com.redshift.ShadowDarkCalculator.actions.DamageType;
import com.redshift.ShadowDarkCalculator.actions.PerformAllActions;
import com.redshift.ShadowDarkCalculator.actions.weapons.Weapon;
import com.redshift.ShadowDarkCalculator.creatures.CreatureLabel;
import com.redshift.ShadowDarkCalculator.creatures.Stats;
import com.redshift.ShadowDarkCalculator.creatures.monsters.Monster;
import com.redshift.ShadowDarkCalculator.dice.RollModifier;
import com.redshift.ShadowDarkCalculator.resistance.NonSilveredNonMagicalImmunity;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D6;
import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D8;

/**
 * A slinking, rat-faced humanoid covered in mangy fur.
 * AC 13 (leather), HP 14, ATK 2 bite +2 (1d6), MV near (climb)
 * S +1, D +2, C +1, I -1, W +1, Ch -1, AL C, LV 3
 * Impervious. Only damaged by silver or magic sources.
 * Lycanthropy. If 12 or more damage from the same wererat, contract lycanthropy.
 */

public class Wererat extends Monster {

    public Wererat(String name) {
        super(
                name,
                3,
                new Stats(12,14,12,8,12,8),
                13,
                D8.roll() + D8.roll() + D8.roll() + 1,
                new PerformAllActions(
                        new Weapon("Bite", D6, RollModifier.DEXTERITY).addPiercing(),
                        new Weapon("Bite", D6, RollModifier.DEXTERITY).addPiercing()
                )
        );
        getLabels().add(CreatureLabel.FRONT_LINE);
        getLabels().add(CreatureLabel.HUMANOID);
        getLabels().add(CreatureLabel.CHAOTIC);
    }

    @Override
    public void takeDamage(int amount, DamageType damageType) {
        final int damage = new NonSilveredNonMagicalImmunity().calculateDamage(this, amount, damageType);
        if (damage != 0) {
            super.takeDamage(amount, damageType);
        }
    }

}
