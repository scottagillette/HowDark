package com.redshift.ShadowDarkCalculator.creatures.undead;

import com.redshift.ShadowDarkCalculator.actions.PerformAllAction;
import com.redshift.ShadowDarkCalculator.actions.weapons.Weapon;
import com.redshift.ShadowDarkCalculator.creatures.BaseCreature;
import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.creatures.Stats;
import com.redshift.ShadowDarkCalculator.dice.RollModifier;
import com.redshift.ShadowDarkCalculator.targets.RandomTargetSelector;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.*;

@Slf4j
public class Mummy extends BaseCreature {

    public Mummy(String name) {
        super(
                name,
                10,
                true,
                true,
                new Stats(17,10,15,17,15,17),
                13,
                D8.roll() + D8.roll() + D8.roll() + D8.roll() + D8.roll() + D8.roll() + D8.roll() + D8.roll() + D8.roll() + D8.roll() + 2,
                new PerformAllAction(new RotTouch(), new RotTouch(), new RotTouch()),
                new RandomTargetSelector()
        );
    }

    @Override
    public void takeDamage(int amount, boolean silvered, boolean magical, boolean fire, boolean cold) {
        // Damage for fire or magical only.
        if (fire) {
            // Double damage for fire!
            log.info(getName() + " takes DOUBLE damage from fire!");
            super.takeDamage(amount + amount, silvered, magical, fire, cold);
        } else {
            if (magical) {
                super.takeDamage(amount, silvered, magical, fire, cold);
            } else {
                log.info(getName() + " takes no damage from non-magical, non-fire damage!");
            }
        }

    }

    private static class RotTouch extends Weapon {

        public RotTouch() {
            super("Rot Touch", D10, RollModifier.STRENGTH, 5);
        }

        @Override
        public void perform(Creature actor, List<Creature> enemies, List<Creature> allies) {
            final Creature target = actor.getTargetSelector().get(enemies);

            if (target == null) {
                log.info(actor.getName() + " is skipping their turn... no target!");
            } else {
                boolean attackHits = performSingleTargetAttack(actor, target, name, dice, rollModifier);

                if (attackHits & target.getCurrentHitPoints() != 0) {
                    if (target.getStats().constitutionSave(15)) {
                        log.info(target.getName() + " SAVES and is NOT drained of health.");
                    } else {
                        // HP 0
                        log.info(target.getName() + " is drained of health and drops to 0 hit points!");
                        target.takeDamage(999, false, false, false, false);
                    }
                }
            }
        }
    }

}