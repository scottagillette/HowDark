package com.redshift.ShadowDarkCalculator.creatures.undead;

import com.redshift.ShadowDarkCalculator.actions.weapons.Weapon;
import com.redshift.ShadowDarkCalculator.conditions.ParalyzedCondition;
import com.redshift.ShadowDarkCalculator.creatures.*;
import com.redshift.ShadowDarkCalculator.dice.RollModifier;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.*;
import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D4;

@Slf4j
public class Ghoul extends UndeadMonster {

    public Ghoul(String name) {
        super(
                name,
                2,
                new Stats(14, 12, 14, 4, 8, 10),
                11,
                D8.roll() + D8.roll() + 2,
                new ParalyzingClaw()
        );
        getLabels().add(Label.FRONT_LINE);
    }

    private static class ParalyzingClaw extends Weapon {

        public ParalyzingClaw() {
            super("ParalyzingTouch Touch", D6, RollModifier.STRENGTH);
        }

        @Override
        public void perform(Creature actor, List<Creature> enemies, List<Creature> allies) {
            final Creature target = actor.getSingleTargetSelector().get(enemies);

            if (target == null) {
                log.info("{} is skipping their turn... no target!", actor.getName());
            } else {
                boolean attackHits = performSingleTargetAttack(actor, target, name, dice, rollModifier);

                if (attackHits) {
                    if (!target.hasCondition(ParalyzedCondition.class.getName())) {
                        if (!target.getStats().constitutionSave(12)) {
                            int rounds = D4.roll();
                            log.info("{} is paralyzed for {} rounds!", target.getName(), rounds);
                            target.addCondition(new ParalyzedCondition(rounds));
                        } else {
                            log.info("{} SAVES and is NOT paralyzed!", target.getName());
                        }
                    }
                }
            }
        }
    }

}