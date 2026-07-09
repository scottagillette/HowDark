package com.redshift.ShadowDarkCalculator.creatures.monsters.monstrosities;

import com.redshift.ShadowDarkCalculator.actions.DamageType;
import com.redshift.ShadowDarkCalculator.actions.weapons.Weapon;
import com.redshift.ShadowDarkCalculator.conditions.DisadvantageToAttackCondition;
import com.redshift.ShadowDarkCalculator.conditions.SpellResilience;
import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.creatures.CreatureLabel;
import com.redshift.ShadowDarkCalculator.creatures.Stats;
import com.redshift.ShadowDarkCalculator.creatures.monsters.Monster;
import com.redshift.ShadowDarkCalculator.dice.RollModifier;
import lombok.extern.slf4j.Slf4j;

import static com.redshift.ShadowDarkCalculator.dice.DifficultyClass.HARD;
import static com.redshift.ShadowDarkCalculator.dice.SingleDie.*;

/**
 * Immortal women with coiling snakes for hair and scaled skin.
 * AC 14, HP 38, ATK 1 snake bite +6 (1d6 + poison), MV near
 * S +2, D +1, C +2, I +2, W +3, Ch +4, AL C, LV 8
 * Godborn. Hostile spells targeting the medusa are DC 15 to cast.
 * Petrify. Any creature (including medusa) who looks directly at medusa, DC 15 CON or petrified.
 * Poison. DC 15 CON or go to 0 HP.
 */

@Slf4j
public class Medusa extends Monster {

    public Medusa(String name) {
        super(
                name,
                8,
                new Stats(14,12,14,14,16,18),
                12,
                D8.roll() + D8.roll() + D8.roll() + D8.roll() +
                        D8.roll() + D8.roll() + D8.roll() + D8.roll() + 2,
                new SnakeBite()
        );
        getLabels().add(CreatureLabel.FRONT_LINE);
        getLabels().add(CreatureLabel.CHAOTIC);
        addCondition(new SpellResilience(HARD.getDc())); // Hostile spells targeting the medusa are DC 15 to cast.
        addCondition(new DisadvantageToAttackCondition()); // Averting their eyes when attacking!
    }

    /**
     * ATK 1 snake bite +6 (1d6 + poison)
     * Poison. DC 15 CON or go to 0 HP.
     */

    private static class SnakeBite extends Weapon {

        public SnakeBite() {
            super("Snake Bite", D6, RollModifier.STRENGTH);
            addPiercing().addAttackRollBonus(4);
        }

        @Override
        protected boolean performSingleTargetAttack(Creature actor, Creature target) {
            final boolean targetHits = super.performSingleTargetAttack(actor, target);

            if (targetHits && !target.isUnconscious()) { // Don't poison unconscious targets
                if (!target.getStats().constitutionSave(HARD.getDc())) {
                    log.info("{} is poisoned and falls to the ground.", target.getName());
                    target.takeDamage(target.getCurrentHitPoints(), new DamageType().addPoison());
                } else {
                    log.info("{} SAVES and is NOT poisoned!", target.getName());
                }
            }

            return targetHits;
        }

    }

}
