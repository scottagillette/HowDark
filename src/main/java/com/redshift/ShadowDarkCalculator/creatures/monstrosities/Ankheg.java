package com.redshift.ShadowDarkCalculator.creatures.monstrosities;

import com.redshift.ShadowDarkCalculator.actions.PerformOneAction;
import com.redshift.ShadowDarkCalculator.actions.weapons.Weapon;
import com.redshift.ShadowDarkCalculator.creatures.BaseCreature;
import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.creatures.Stats;
import com.redshift.ShadowDarkCalculator.dice.MultipleDice;
import com.redshift.ShadowDarkCalculator.dice.RollModifier;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D6;
import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D8;

@Slf4j
public class Ankheg extends BaseCreature {

    public Ankheg(String name) {
        super(
                name,
                3,
                new Stats(14,14,12,7,12,7),
                14,
                D8.roll() + D8.roll() + D8.roll() + 1,
                new PerformOneAction(new Bite(), new AcidSpray())
        );
    }

    private static class Bite extends Weapon {

        private Bite() {
            super("Bite", D6, RollModifier.STRENGTH, 2);
        }

    }

    private static class AcidSpray extends Weapon {

        private AcidSpray() {
            super("Acid Spray", new MultipleDice(D6, D6), RollModifier.STRENGTH, 2);
        }

        @Override
        public void perform(Creature actor, List<Creature> enemies, List<Creature> allies) {
            log.info(actor.getName() + " sprays ACID!");
            performMultipleTargetAttack(actor, enemies, name, dice, rollModifier);
        }
    }
}
