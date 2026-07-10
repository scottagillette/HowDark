package com.redshift.ShadowDarkCalculator.creatures.monsters.humanoid;

import com.redshift.ShadowDarkCalculator.actions.PerformAllActions;
import com.redshift.ShadowDarkCalculator.actions.weapons.Weapon;
import com.redshift.ShadowDarkCalculator.creatures.CreatureLabel;
import com.redshift.ShadowDarkCalculator.creatures.Stats;
import com.redshift.ShadowDarkCalculator.creatures.monsters.Monster;
import com.redshift.ShadowDarkCalculator.dice.MultipleDice;
import com.redshift.ShadowDarkCalculator.dice.RollModifier;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D4;
import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D8;

/**
 * A black-cloaked, skulking killer.
 * AC 15 (leather), HP 38, ATK 2 poisoned dagger (close/near) +6 (2d4), MV near (climb)
 * S +2, D +4, C +2, I +2, W +3, Ch +3, AL C, LV 8
 * TODO: Execute. Deals x3 damage against surprised targets.
 */

public class Assassin  extends Monster {

    public Assassin(String name) {
        super(
                name,
                8,
                new Stats(14, 18, 14, 14,16, 16),
                15,
                D8.roll() + D8.roll() + D8.roll() + D8.roll() +
                        D8.roll() + D8.roll() + D8.roll() + D8.roll() + 2,
                new PerformAllActions(
                        new Weapon("Poison Dagger", new MultipleDice(D4, D4), RollModifier.DEXTERITY).addPiercing().addAttackRollBonus(2),
                        new Weapon("Poison Dagger", new MultipleDice(D4, D4), RollModifier.DEXTERITY).addPiercing().addAttackRollBonus(2)
                )
        );
        getLabels().add(CreatureLabel.FRONT_LINE);
        getLabels().add(CreatureLabel.HUMANOID);
        getLabels().add(CreatureLabel.CHAOTIC);
    }

}
