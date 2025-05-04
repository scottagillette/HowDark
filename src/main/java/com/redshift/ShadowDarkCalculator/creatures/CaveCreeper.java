package com.redshift.ShadowDarkCalculator.creatures;

import com.redshift.ShadowDarkCalculator.actions.PerformAllAction;
import com.redshift.ShadowDarkCalculator.dice.RollModifier;
import com.redshift.ShadowDarkCalculator.actions.weapons.Weapon;
import com.redshift.ShadowDarkCalculator.conditions.ParalyzedCondition;

import java.util.List;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.*;

public class CaveCreeper extends BaseCreature {

    public CaveCreeper(String name) {
        super(
                name,
                4,
                new Stats(14,14,10,4,12,4),
                12,
                D8.roll() + D8.roll() + D8.roll() + D8.roll(),
                new PerformAllAction(new Bite(), new Tentacles())
        );
    }

    public static class Bite extends Weapon {

        public Bite() {
            super("Bite", D6, RollModifier.STRENGTH);
        }

    }

    public static class Tentacles extends Weapon {

        public Tentacles() {
            super("Tentacles", D8, RollModifier.STRENGTH);
        }

        @Override
        public void perform(Creature actor, List<Creature> enemies, List<Creature> allies) {
            final Creature target = actor.getTargetSelector().get(enemies);

            if (target == null) {
                System.out.println(actor.getName() + " is skipping their turn... no target!");
            } else {
                boolean attackHits = performSingleTargetAttack(actor, target, name, dice, rollModifier);

                if (attackHits) {
                    if (!target.getStats().constitutionSave(12)) {
                        int rounds = D4.roll();
                        System.out.println(target.getName() + " is paralyzed for " + rounds + " rounds!");
                        target.addCondition(new ParalyzedCondition(rounds));
                    } else {
                        System.out.println(target.getName() + " SAVES and is NOT paralyzed!");
                    }
                }
            }
        }
    }
}
