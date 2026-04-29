package com.redshift.ShadowDarkCalculator.creatures.monsters.demons;

import com.redshift.ShadowDarkCalculator.actions.PerformAllActions;
import com.redshift.ShadowDarkCalculator.actions.PerformOneAction;
import com.redshift.ShadowDarkCalculator.actions.misc.Terrify;
import com.redshift.ShadowDarkCalculator.actions.misc.WakingFear;
import com.redshift.ShadowDarkCalculator.actions.weapons.Weapon;
import com.redshift.ShadowDarkCalculator.creatures.CreatureLabel;
import com.redshift.ShadowDarkCalculator.creatures.monsters.Monster;
import com.redshift.ShadowDarkCalculator.creatures.Stats;
import com.redshift.ShadowDarkCalculator.dice.MultipleDice;
import com.redshift.ShadowDarkCalculator.dice.RollModifier;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D10;
import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D8;

/**
 * A pale, faceless man with elongated limbs and curved talons that rake the ground. Moves in quick stutters during
 * each eye blink.
 * AC 17, HP 61, ATK 3 finger needle +9 (2d10) and 1 terrify, MV near (teleport)
 * S +5, D +7, C +3, I +4, W +4, Ch +5, AL C, LV 13
 * Fearless. Immune to morale checks.
 * Terrify. One target in near DC 15 CHA or paralyzed 1d4 rounds.
 * Waking Nightmare. In place of attacks, all creatures within near DC 15 CHA or flee in a random direction for 1d4 rounds.
 */

public class TheWillowMan extends Monster {

    public TheWillowMan(String name) {
        super(
                name,
                13,
                new Stats(20,24,16,18,18,20),
                17,
                D8.roll() + D8.roll() + D8.roll() + D8.roll() + D8.roll() +
                        D8.roll() + D8.roll() + D8.roll() + D8.roll() + D8.roll() +
                        D8.roll() + D8.roll() + D8.roll() + 3,
                new PerformOneAction(
                        new PerformAllActions(
                                new Weapon("Finger Needle", new MultipleDice(D10, D10), RollModifier.DEXTERITY).addPiercing().addAttackRollBonus(5),
                                new Weapon("Finger Needle", new MultipleDice(D10, D10), RollModifier.DEXTERITY).addPiercing().addAttackRollBonus(5),
                                new Weapon("Finger Needle", new MultipleDice(D10, D10), RollModifier.DEXTERITY).addPiercing().addAttackRollBonus(5),
                                new Terrify()
                        ).setPriority(2),
                        new WakingFear().setPriority(1)
                )
        );
        getLabels().add(CreatureLabel.FRONT_LINE);
    }

}
