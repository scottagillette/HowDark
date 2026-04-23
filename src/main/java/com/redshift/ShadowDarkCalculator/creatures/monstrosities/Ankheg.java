package com.redshift.ShadowDarkCalculator.creatures.monstrosities;

import com.redshift.ShadowDarkCalculator.actions.PerformOneAction;
import com.redshift.ShadowDarkCalculator.actions.weapons.Weapon;
import com.redshift.ShadowDarkCalculator.creatures.*;
import com.redshift.ShadowDarkCalculator.dice.MultipleDice;
import com.redshift.ShadowDarkCalculator.dice.RollModifier;
import lombok.extern.slf4j.Slf4j;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D6;
import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D8;

@Slf4j
public class Ankheg extends Monster {

    public Ankheg(String name) {
        super(
                name,
                3,
                new Stats(14,14,12,7,12,7),
                14,
                D8.roll() + D8.roll() + D8.roll() + 1,
                new PerformOneAction(
                        new Bite().setPriority(2),
                        new AcidSpray().setPriority(1) // Make this less common to not nuke the party to quickly ;)
                )
        );
        getLabels().add(CreatureLabel.FRONT_LINE);
    }

    private static class Bite extends Weapon {

        private Bite() {
            super("Bite", D6, RollModifier.STRENGTH);
            addAttackRollBonus(2); // +2 modifier above STR
        }

    }

    private static class AcidSpray extends Weapon {

        private AcidSpray() {
            super("Acid Spray", new MultipleDice(D6, D6), RollModifier.STRENGTH);
            addAttackRollBonus(2); // +2 modifier above STR
        }
    }

}
