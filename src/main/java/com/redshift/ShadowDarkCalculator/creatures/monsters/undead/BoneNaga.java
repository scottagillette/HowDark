package com.redshift.ShadowDarkCalculator.creatures.monsters.undead;

import com.redshift.ShadowDarkCalculator.actions.PerformAllActions;
import com.redshift.ShadowDarkCalculator.actions.weapons.Weapon;
import com.redshift.ShadowDarkCalculator.creatures.CreatureLabel;
import com.redshift.ShadowDarkCalculator.creatures.Stats;
import com.redshift.ShadowDarkCalculator.creatures.monsters.UndeadMonster;
import com.redshift.ShadowDarkCalculator.dice.MultipleDice;
import com.redshift.ShadowDarkCalculator.dice.RollModifier;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D6;
import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D8;

/**
 * Mindless, skeletal husks of nagas reanimated by sorcery.
 * AC 13, HP 31, ATK 2 bite +5 (2d6), MV near (burrow, climb)
 * S +3, D +2, C +4, I -3, W +0, Ch +4, AL C, LV 6
 * Greater Undead. Immune to morale checks. Only damaged by silver or magical sources.
 */

public class BoneNaga extends UndeadMonster {

    public BoneNaga(String name) {
        super(
                name,
                6,
                new Stats(16,14,18,4,10,18),
                13,
                D8.roll() + D8.roll() + D8.roll() + D8.roll() + D8.roll() + D8.roll() + 4,
                new PerformAllActions(
                        new Weapon("Bite", new MultipleDice(D6, D6), RollModifier.STRENGTH).addPiercing().addAttackRollBonus(2),
                        new Weapon("Bite", new MultipleDice(D6, D6), RollModifier.STRENGTH).addPiercing().addAttackRollBonus(2)
                )
        );
        getLabels().add(CreatureLabel.FRONT_LINE);
        getLabels().add(CreatureLabel.CHAOTIC);
    }

}
