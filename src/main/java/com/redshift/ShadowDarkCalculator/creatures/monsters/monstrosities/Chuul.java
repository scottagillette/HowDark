package com.redshift.ShadowDarkCalculator.creatures.monsters.monstrosities;

import com.redshift.ShadowDarkCalculator.actions.PerformAllActions;
import com.redshift.ShadowDarkCalculator.actions.weapons.Weapon;
import com.redshift.ShadowDarkCalculator.conditions.GrappledCondition;
import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.creatures.CreatureLabel;
import com.redshift.ShadowDarkCalculator.creatures.Stats;
import com.redshift.ShadowDarkCalculator.creatures.monsters.Monster;
import com.redshift.ShadowDarkCalculator.dice.DifficultyClass;
import com.redshift.ShadowDarkCalculator.dice.MultipleDice;
import com.redshift.ShadowDarkCalculator.dice.RollModifier;
import lombok.extern.slf4j.Slf4j;

import static com.redshift.ShadowDarkCalculator.dice.DifficultyClass.HARD;
import static com.redshift.ShadowDarkCalculator.dice.SingleDie.*;
import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D10;

/**
 * Brown, horse-sized lobster bugs with tentacles and pincers.
 * AC 15, HP 25, ATK 2 pincer +4 (1d8 + grab), MV near (swim)
 * S +3, D -1, C +3, I -1, W +1, Ch -2, AL C, LV 5
 * Grab. DC 15 STR or held in pincer. DC 15 STR on turn to break free.
 */

@Slf4j
public class Chuul extends Monster {

    public Chuul(String name) {
        super(
                name,
                5,
                new Stats(16, 8, 16, 4, 12, 6),
                15,
                D8.roll() + D8.roll() + D8.roll() + D8.roll() + D8.roll() + 3,
                new PerformAllActions(
                        new Pincer(),
                        new Pincer()
                )
        );
        getLabels().add(CreatureLabel.FRONT_LINE);
        getLabels().add(CreatureLabel.CHAOTIC);
    }

    /**
     * Grab. DC 15 STR or held in pincer. DC 15 STR on turn to break free.
     */

    private static class Pincer extends Weapon {

        public Pincer() {
            super("Pincer", D8, RollModifier.STRENGTH);
            addCrushing().addAttackRollBonus(1);
        }

        @Override
        protected boolean performSingleTargetAttack(Creature actor, Creature target) {
            boolean attackHits = super.performSingleTargetAttack(actor, target);

            if (attackHits && !target.isUnconscious() && !target.isDead()) {
                if (!target.getStats().strengthSave(HARD.getDc())) {
                    log.info("{} is grappled in the pincer!", target.getName());
                    target.addCondition(new GrappledCondition());
                }
            }

            return attackHits;
        }
    }
}
