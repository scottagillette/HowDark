package com.redshift.ShadowDarkCalculator.creatures;

import com.redshift.ShadowDarkCalculator.actions.PerformAllAction;
import com.redshift.ShadowDarkCalculator.actions.weapons.Weapon;
import com.redshift.ShadowDarkCalculator.dice.RollModifier;
import com.redshift.ShadowDarkCalculator.targets.RandomTargetSelector;

import java.util.List;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.*;

public class Wraith extends BaseCreature {

    public Wraith(String name) {
        super(
                name,
                8,
                true,
                true,
                new Stats(3,18,10,10,10,16),
                14,
                D8.roll() + D8.roll() + D8.roll() + D8.roll() + D8.roll() + D8.roll() + D8.roll() + D8.roll(),
                new PerformAllAction(new Wraith.DeathTouch(), new Wraith.DeathTouch(), new Wraith.DeathTouch()),
                new RandomTargetSelector()
        );
    }

    @Override
    public void takeDamage(int amount, boolean silvered, boolean magical) {
        // Take only silvered or magical damage!
        if (silvered || magical) {
            super.takeDamage(amount, silvered, magical);
        } else {
            System.out.println(getName() + " takes no damage from non-silvered, non-magical damage!");
        }
    }

    public static class DeathTouch extends Weapon {

        public DeathTouch() {
            super("Death Touch", D10, RollModifier.DEXTERITY, 2);
        }

        @Override
        public void perform(Creature actor, List<Creature> enemies, List<Creature> allies) {
            final Creature target = actor.getTargetSelector().get(enemies);

            if (target == null) {
                System.out.println(actor.getName() + " is skipping their turn... no target!");
            } else {
                boolean attackHits = performSingleTargetAttack(actor, target, name, dice, rollModifier);

                if (attackHits) {
                    int constitutionRemaining = target.getStats().constitutionDrain(D4);
                    if ( constitutionRemaining == 0) {
                        System.out.println(target.getName() + " is drained of constitution to " + constitutionRemaining + " and DIES!");
                        target.setDead(true);
                    } else {
                        System.out.println(target.getName() + " is drained of constitution to " + constitutionRemaining);
                    }
                }
            }
        }
    }

}