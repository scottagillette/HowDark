package com.redshift.ShadowDarkCalculator.creatures.undead;

import com.redshift.ShadowDarkCalculator.actions.PerformAllActions;
import com.redshift.ShadowDarkCalculator.actions.weapons.Weapon;
import com.redshift.ShadowDarkCalculator.conditions.ParalyzedCondition;
import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.creatures.Label;
import com.redshift.ShadowDarkCalculator.creatures.Stats;
import com.redshift.ShadowDarkCalculator.creatures.UndeadMonster;
import com.redshift.ShadowDarkCalculator.dice.RollModifier;
import com.redshift.ShadowDarkCalculator.targets.RandomTargetSelector;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.*;

@Slf4j
public class Ghast extends UndeadMonster {

    public Ghast(String name) {
        super(
                name,
                4,
                new Stats(17,12,14,10,10,14),
                11,
                D8.roll() + D8.roll() + D8.roll() + D8.roll() + 2,
                new PerformAllActions(new ParalyzingClaw(), new ParalyzingClaw()),
                new RandomTargetSelector()
        );
        getLabels().add(Label.FRONT_LINE);
    }

    private static class ParalyzingClaw extends Weapon {

        public ParalyzingClaw() {
            super("Paralyzing Claw", D8, RollModifier.STRENGTH);
        }

        @Override
        public void perform(Creature actor, List<Creature> enemies, List<Creature> allies) {
            final Creature target = actor.getSingleTargetSelector().get(enemies);

            if (target == null) {
                log.info("{} is skipping their turn... no target!", actor.getName());
            } else {

                // TODO: Note - The Carrion Stench ability is not implemented...

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