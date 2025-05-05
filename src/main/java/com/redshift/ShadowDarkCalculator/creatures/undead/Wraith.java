package com.redshift.ShadowDarkCalculator.creatures.undead;

import com.redshift.ShadowDarkCalculator.actions.PerformAllAction;
import com.redshift.ShadowDarkCalculator.actions.weapons.Weapon;
import com.redshift.ShadowDarkCalculator.creatures.BaseCreature;
import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.creatures.Stats;
import com.redshift.ShadowDarkCalculator.dice.RollModifier;
import com.redshift.ShadowDarkCalculator.resistance.SimpleDamageResistance;
import com.redshift.ShadowDarkCalculator.targets.RandomTargetSelector;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.*;

@Slf4j
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
                new PerformAllAction(new DeathTouch(), new DeathTouch(), new DeathTouch()),
                new RandomTargetSelector()
        );
    }

    @Override
    public void takeDamage(int amount, boolean silvered, boolean magical) {
        // Take only silvered or magical damage!
        int newAmount = new SimpleDamageResistance().takeDamage(amount, silvered, magical);

        if (newAmount == 0) {
            log.info(getName() + " takes no damage from non-silvered, non-magical damage!");
        } else {
            super.takeDamage(amount, silvered, magical);
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
                log.info(actor.getName() + " is skipping their turn... no target!");
            } else {
                boolean attackHits = performSingleTargetAttack(actor, target, name, dice, rollModifier);

                if (attackHits) {
                    int constitutionRemaining = target.getStats().constitutionDrain(D4);
                    if ( constitutionRemaining == 0) {
                        log.info(target.getName() + " is drained of constitution to " + constitutionRemaining + " and DIES!");
                        target.setDead(true);
                    } else {
                        log.info(target.getName() + " is drained of constitution to " + constitutionRemaining);
                    }
                }
            }
        }
    }

}