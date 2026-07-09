package com.redshift.ShadowDarkCalculator.creatures.monsters.beasts;

import com.redshift.ShadowDarkCalculator.actions.weapons.Weapon;
import com.redshift.ShadowDarkCalculator.conditions.ParalyzedCondition;
import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.creatures.CreatureLabel;
import com.redshift.ShadowDarkCalculator.creatures.Stats;
import com.redshift.ShadowDarkCalculator.creatures.monsters.Monster;
import com.redshift.ShadowDarkCalculator.dice.RollModifier;
import lombok.extern.slf4j.Slf4j;

import static com.redshift.ShadowDarkCalculator.dice.DifficultyClass.NORMAL;
import static com.redshift.ShadowDarkCalculator.dice.SingleDie.*;

/**
 * Blood-red, feathery centipedes the size of a human arm. Their bite injects a
 * burning poison that cramps muscles.
 * AC 11, HP 4, ATK 1 bite +1 (1d4 + poison), MV near (climb)
 * S -3, D +1, C +0, I -4, W -3, Ch -4, AL N, LV 1
 * Poison. DC 12 CON or paralyzed 1d4 rounds.
 */

@Slf4j
public class GiantCentipede extends Monster {

    public GiantCentipede(String name) {
        super(
                name,
                1,
                new Stats(6, 12, 10, 2, 4, 2),
                11,
                D8.roll(),
                new ParalyzingBite()
        );
        getLabels().add(CreatureLabel.FRONT_LINE);
        getLabels().add(CreatureLabel.NEUTRAL);
    }

    /**
     * ATK 1 bite +1 (1d4 + poison) - Poison. DC 12 CON or paralyzed 1d4 rounds.
     */

    private static class ParalyzingBite extends Weapon {

        public ParalyzingBite() {
            super("Paralyzing Bite", D4, RollModifier.DEXTERITY);
            addPiercing();
        }

        @Override
        protected boolean performSingleTargetAttack(Creature actor, Creature target) {
            final boolean attackHits = super.performSingleTargetAttack(actor, target);

            if (attackHits) {
                if (target.isUnconscious() && !target.hasCondition(ParalyzedCondition.class.getName())) {
                    if (target.getStats().constitutionSave(NORMAL.getDc())) {
                        log.info("{} SAVES and is NOT paralyzed!", target.getName());
                    } else {
                        int rounds = D4.roll();
                        log.info("{} is paralyzed for {} rounds!", target.getName(), rounds);
                        target.addCondition(new ParalyzedCondition(rounds));
                    }
                }
            }

            return attackHits;
        }
    }

}
