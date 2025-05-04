package com.redshift.ShadowDarkCalculator.creatures;

import com.redshift.ShadowDarkCalculator.dice.RollModifier;
import com.redshift.ShadowDarkCalculator.actions.weapons.Weapon;
import com.redshift.ShadowDarkCalculator.conditions.EngulfedInAcidCondition;
import com.redshift.ShadowDarkCalculator.conditions.ParalyzedCondition;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.*;

@Slf4j
public class GelatinousCube extends BaseCreature {

    public GelatinousCube(String name) {
        super(
                name,
                5,
                new Stats(18,12,14,4,12,4),
                11,
                D8.roll() + D8.roll() + D8.roll() + D8.roll() + D8.roll() + 2,
                new GelatinousCube.AcidTouch()
        );
    }

    public static class AcidTouch extends Weapon {

        public AcidTouch() {
            super("Acid Touch", D8, RollModifier.STRENGTH);
        }

        @Override
        public void perform(Creature actor, List<Creature> enemies, List<Creature> allies) {
            final Creature target = actor.getTargetSelector().get(enemies);

            if (target == null) {
                log.info(actor.getName() + " is skipping their turn... no target!");
            } else {
                boolean attackHits = performSingleTargetAttack(actor, target, name, dice, rollModifier);

                if (attackHits) {
                    // Toxin
                    if (!target.getStats().constitutionSave(15)) {
                        int rounds = D4.roll();
                        log.info(target.getName() + " is paralyzed for " + rounds + " rounds!");
                        target.addCondition(new ParalyzedCondition(rounds));

                        // Engulf... does no check if paralyzed...
                        log.info(target.getName() + " is engulfed by the Gelatinous Cube!");
                        target.addCondition(new EngulfedInAcidCondition(D8));

                    } else {
                        log.info(target.getName() + " SAVES and is NOT paralyzed!");

                        // Engulf.. STR DC 12 check
                        if (!target.getStats().strengthSave(12)) {
                            log.info(target.getName() + " is engulfed by the Gelatinous Cube!");
                            target.addCondition(new EngulfedInAcidCondition(D8));
                        }
                    }
                }
            }
        }
    }

}