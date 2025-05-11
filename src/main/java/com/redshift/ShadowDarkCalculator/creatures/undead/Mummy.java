package com.redshift.ShadowDarkCalculator.creatures.undead;

import com.redshift.ShadowDarkCalculator.actions.PerformAllAction;
import com.redshift.ShadowDarkCalculator.actions.weapons.Weapon;
import com.redshift.ShadowDarkCalculator.creatures.*;
import com.redshift.ShadowDarkCalculator.dice.RollModifier;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.*;

@Slf4j
public class Mummy extends UndeadMonster {

    public Mummy(String name) {
        super(
                name,
                10,
                new Stats(17,10,15,17,15,17),
                13,
                D8.roll() + D8.roll() + D8.roll() + D8.roll() + D8.roll() + D8.roll() + D8.roll() + D8.roll() + D8.roll() + D8.roll() + 2,
                new PerformAllAction(new RotTouch(), new RotTouch(), new RotTouch())
        );
        getLabels().add(Label.BRUTE);
    }

    @Override
    public void takeDamage(int amount, boolean silvered, boolean magical, boolean fire, boolean cold) {
        // Damage for fire or magical only.
        if (fire) {
            // Double damage for fire!
            log.info("{} takes DOUBLE damage from fire!", getName());
            super.takeDamage(amount + amount, silvered, magical, fire, cold);
        } else {
            if (magical) {
                super.takeDamage(amount, silvered, magical, fire, cold);
            } else {
                log.info("{} takes no damage from non-magical, non-fire damage!", getName());
            }
        }

    }

    private static class RotTouch extends Weapon {

        public RotTouch() {
            super("Rot Touch", D10, RollModifier.STRENGTH, 5);
        }

        @Override
        public void perform(Creature actor, List<Creature> enemies, List<Creature> allies) {
            final Creature target = actor.getSingleTargetSelector().get(enemies);

            if (target == null) {
                log.info("{} is skipping their turn... no target!", actor.getName());
            } else {
                boolean attackHits = performSingleTargetAttack(actor, target, name, dice, rollModifier);

                if (attackHits & target.getCurrentHitPoints() != 0) {
                    if (target.getStats().constitutionSave(15)) {
                        log.info("{} SAVES and is NOT drained of health.", target.getName());
                    } else {
                        // HP 0
                        log.info("{} is drained of health and drops to 0 hit points!", target.getName());
                        target.takeDamage(999, false, false, false, false);
                    }
                }
            }
        }
    }

}