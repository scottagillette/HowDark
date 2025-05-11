package com.redshift.ShadowDarkCalculator.creatures.oozes;

import com.redshift.ShadowDarkCalculator.creatures.*;
import com.redshift.ShadowDarkCalculator.dice.RollModifier;
import com.redshift.ShadowDarkCalculator.actions.weapons.Weapon;
import com.redshift.ShadowDarkCalculator.conditions.EngulfedInAcidCondition;
import com.redshift.ShadowDarkCalculator.conditions.ParalyzedCondition;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.*;

@Slf4j
public class GelatinousCube extends Monster {

    public GelatinousCube(String name) {
        super(
                name,
                5,
                new Stats(18,12,14,4,12,4),
                11,
                D8.roll() + D8.roll() + D8.roll() + D8.roll() + D8.roll() + 2,
                new GelatinousCube.AcidTouch()
        );
        getLabels().add(Label.BRUTE);
    }

    public static class AcidTouch extends Weapon {

        public AcidTouch() {
            super("Acid Touch", D8, RollModifier.STRENGTH);
        }

        @Override
        public void perform(Creature actor, List<Creature> enemies, List<Creature> allies) {
            final Creature target = actor.getSingleTargetSelector().get(enemies);

            if (target == null) {
                log.info("{} is skipping their turn... no target!", actor.getName());
            } else {
                boolean attackHits = performSingleTargetAttack(actor, target, name, dice, rollModifier);

                if (attackHits) {
                    // Toxin
                    if (!target.getStats().constitutionSave(15)) {
                        int rounds = D4.roll();
                        log.info("{} is paralyzed for {} rounds!", target.getName(), rounds);
                        target.addCondition(new ParalyzedCondition(rounds));

                        // Engulf... does no check if paralyzed...
                        log.info("{} is paralyzed and is automatically engulfed by the Gelatinous Cube!", target.getName());
                        target.addCondition(new EngulfedInAcidCondition(D8));

                    } else {
                        log.info("{} SAVES and is NOT paralyzed!", target.getName());

                        // Engulf.. STR DC 12 check
                        if (!target.getStats().strengthSave(12)) {
                            log.info("{} fails a STR save and is engulfed by the Gelatinous Cube!", target.getName());
                            target.addCondition(new EngulfedInAcidCondition(D8));
                        }
                    }
                }
            }
        }
    }

}