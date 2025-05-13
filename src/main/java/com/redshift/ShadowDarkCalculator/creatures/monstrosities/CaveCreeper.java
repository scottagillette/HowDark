package com.redshift.ShadowDarkCalculator.creatures.monstrosities;

import com.redshift.ShadowDarkCalculator.actions.PerformAllActions;
import com.redshift.ShadowDarkCalculator.creatures.*;
import com.redshift.ShadowDarkCalculator.dice.RollModifier;
import com.redshift.ShadowDarkCalculator.actions.weapons.Weapon;
import com.redshift.ShadowDarkCalculator.conditions.ParalyzedCondition;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.*;

@Slf4j
public class CaveCreeper extends Monster {

    public CaveCreeper(String name) {
        super(
                name,
                4,
                new Stats(14,14,10,4,12,4),
                12,
                D8.roll() + D8.roll() + D8.roll() + D8.roll(),
                new PerformAllActions(new Bite(), new Tentacles())
        );
        getLabels().add(Label.BRUTE);
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
