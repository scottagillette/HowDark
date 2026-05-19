package com.redshift.ShadowDarkCalculator.creatures.monsters.monstrosities;

import com.redshift.ShadowDarkCalculator.actions.PerformAllActions;
import com.redshift.ShadowDarkCalculator.actions.PerformOneAction;
import com.redshift.ShadowDarkCalculator.actions.weapons.Weapon;
import com.redshift.ShadowDarkCalculator.creatures.CreatureLabel;
import com.redshift.ShadowDarkCalculator.creatures.Stats;
import com.redshift.ShadowDarkCalculator.creatures.monsters.Monster;
import com.redshift.ShadowDarkCalculator.dice.MultipleDice;
import com.redshift.ShadowDarkCalculator.dice.RollModifier;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D6;
import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D8;

/**
 * Human-faced lions with bat wings and cruelly spiked tails. They speak halting Thanian and
 * love devouring human flesh.
 * AC 14, HP 29, ATK 2 rend +6 (2d6) or 2 tail spike (far) +4 (1d8), MV double near (fly)
 * S +4, D +2, C +2, I -2, W +1, Ch -2, AL C, LV 6
 * Spikes. Manticores have 4d6 tail spikes. They regrow each day.
 */

public class Manticore extends Monster {

    public Manticore(String name) {
        super(
                name,
                6,
                new Stats(18, 14, 14, 6, 12, 6),
                14,
                D8.roll() + D8.roll() + D8.roll() + D8.roll() + D8.roll() + D8.roll() + 2,
                new PerformOneAction(
                        new PerformAllActions(
                                new Weapon("Rend", new MultipleDice(D6, D6), RollModifier.STRENGTH).addSlashing().addAttackRollBonus(2),
                                new Weapon("Rend", new MultipleDice(D6, D6), RollModifier.STRENGTH).addSlashing().addAttackRollBonus(2)
                        ),
                        new PerformAllActions(
                                new Weapon("Tail Spike", D8, RollModifier.STRENGTH).addPiercing(),
                                new Weapon("Tail Spike", D8, RollModifier.STRENGTH).addPiercing()
                        )
                )
        );
        getLabels().add(CreatureLabel.CHAOTIC);
    }
}
