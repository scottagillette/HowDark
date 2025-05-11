package com.redshift.ShadowDarkCalculator.creatures.goblinoid;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D4;
import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D8;

import com.redshift.ShadowDarkCalculator.actions.PerformOneAction;
import com.redshift.ShadowDarkCalculator.actions.spells.SingleTargetDamageSpell;
import com.redshift.ShadowDarkCalculator.actions.weapons.WeaponBuilder;
import com.redshift.ShadowDarkCalculator.conditions.DisadvantagedCondition;
import com.redshift.ShadowDarkCalculator.creatures.BaseCreature;
import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.creatures.Label;
import com.redshift.ShadowDarkCalculator.creatures.Monster;
import com.redshift.ShadowDarkCalculator.creatures.Stats;
import com.redshift.ShadowDarkCalculator.dice.MultipleDice;
import com.redshift.ShadowDarkCalculator.dice.RollModifier;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class GoblinShaman extends Monster {

    public GoblinShaman(String name) {
        super(
            name,
            4,
            new Stats(10, 12, 12, 9, 14, 12),
            12,
            D8.roll() + D8.roll() + D8.roll() + D8.roll() + 1,
            new PerformOneAction(WeaponBuilder.STAFF.build(), new StinkBomb())
        );
        getLabels().add(Label.CASTER);
    }

    public static class StinkBomb extends SingleTargetDamageSpell {

        public StinkBomb() {
            super(
                "Stink Bomb",
                12,
                RollModifier.WISDOM,
                new MultipleDice(D4, D4),
                false
            );
        }

        @Override
        public void perform(
            Creature actor,
            List<Creature> enemies,
            List<Creature> allies
        ) {
            final Creature target = actor
                .getSingleTargetSelector()
                .get(enemies);

            if (target == null) {
                log.info(
                    actor.getName() + " is skipping their turn... no target!"
                );
            } else {
                performSingleTargetSpellAttack(
                    actor,
                    target,
                    this,
                    difficultyClass,
                    damageDice,
                    rollModifier,
                    actor.isDisadvantaged()
                );

                if (!target.getStats().constitutionSave(12)) {
                    log.info(
                        target.getName() +
                        " is disadvantaged on their next attack/check!"
                    );
                    target.addCondition(new DisadvantagedCondition());
                } else {
                    log.info(
                        target.getName() + " SAVES and is NOT disadvantaged!"
                    );
                }
            }
        }
    }
}
