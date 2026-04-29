package com.redshift.ShadowDarkCalculator.creatures.monsters.demons;

import com.redshift.ShadowDarkCalculator.actions.PerformAllActions;
import com.redshift.ShadowDarkCalculator.actions.weapons.Weapon;
import com.redshift.ShadowDarkCalculator.creatures.CreatureLabel;
import com.redshift.ShadowDarkCalculator.creatures.monsters.Monster;
import com.redshift.ShadowDarkCalculator.creatures.Stats;
import com.redshift.ShadowDarkCalculator.dice.MultipleDice;
import com.redshift.ShadowDarkCalculator.dice.RollModifier;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D6;
import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D8;

/**
 * As tall as two humans with a pair of curved horns, a shark's grin, and an axe of white bone.
 * AC 13, HP 29, ATK 3 bone axe +5 (2d6), MV near
 * S +4, D +1, C +2, I +0, W +0, Ch +1, AL C, LV 6
 * Shatter. Destroy one non-magic gear instead of dealing damage. // TODO: Not Implemented
 */

public class Dralech extends Monster {

    public Dralech(String name) {
        super(
                name,
                6,
                new Stats(18,12,14,10,10,12),
                13,
                D8.roll() + D8.roll() + D8.roll() + D8.roll() + D8.roll() + D8.roll(),
                new PerformAllActions(
                        new Weapon("Bone Axe", new MultipleDice(D6, D6), RollModifier.STRENGTH).addSlashing().addAttackRollBonus(1),
                        new Weapon("Bone Axe", new MultipleDice(D6, D6), RollModifier.STRENGTH).addSlashing().addAttackRollBonus(1),
                        new Weapon("Bone Axe", new MultipleDice(D6, D6), RollModifier.STRENGTH).addSlashing().addAttackRollBonus(1)
                )
        );
        getLabels().add(CreatureLabel.FRONT_LINE);
    }

}
