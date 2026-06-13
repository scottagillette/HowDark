package com.redshift.ShadowDarkCalculator.creatures.monsters.golem;

import com.redshift.ShadowDarkCalculator.actions.DamageType;
import com.redshift.ShadowDarkCalculator.actions.PerformAllActions;
import com.redshift.ShadowDarkCalculator.actions.weapons.Weapon;
import com.redshift.ShadowDarkCalculator.conditions.ExtraDamageDiceCondition;
import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.creatures.CreatureLabel;
import com.redshift.ShadowDarkCalculator.creatures.Stats;
import com.redshift.ShadowDarkCalculator.creatures.monsters.Monster;
import com.redshift.ShadowDarkCalculator.dice.RollModifier;
import lombok.extern.slf4j.Slf4j;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D8;

/**
 * A ghastly monstrosity made of sewn-together corpses.
 * AC 9, HP 35, ATK 3 slam +6 (1d8), MV near
 * S +4, D -1, C +4, I -1, W +1, Ch -3, AL N, LV 7
 * Golem. Immune to damage from fire, cold, or non-magical sources. Healed by electricity.
 * Berserk. When at or below 20 HP, +1 slam attack and slams deal double damage.
 */

@Slf4j
public class FleshGolem extends Monster {

    public FleshGolem(String name) {
        super(
                name,
                7,
                new Stats(18, 8, 18, 8, 12, 4),
                9,
                D8.roll() + D8.roll() + D8.roll() + D8.roll() + D8.roll() + D8.roll() + D8.roll() + 3,
                new PerformAllActions(
                        new Slam().addCrushing().addAttackRollBonus(2),
                        new Slam().addCrushing().addAttackRollBonus(2),
                        new Slam().addCrushing().addAttackRollBonus(2)
                )
        );
        getLabels().add(CreatureLabel.FRONT_LINE);
        getLabels().add(CreatureLabel.NEUTRAL);
    }

    @Override
    public void takeDamage(int amount, DamageType damageType) {
        // Immune to damage from fire, cold, or non-magical sources. Healed by electricity.
        if (damageType.isFire()) {
            log.info("{} is immune to the fire damage!", getName());
        } else if (damageType.isCold()) {
            log.info("{} is immune to the cold damage!", getName());
        } else if (damageType.isLightning()) {
            log.info("{} seems to be healed by electrical damage!", getName());
            healDamage(amount);
        } else if (!damageType.isMagical()) {
            log.info("{} is immune to non-magical damage!", getName());
        } else {
            super.takeDamage(amount, damageType);
        }
    }

    /**
     * Slam: 1d8 +7
     * Berserk. When at or below 20 HP, +1 slam attack and slams deal double damage.
     */

    private static class Slam extends Weapon {

        private Slam() {
            super("Slam", D8, RollModifier.STRENGTH);
        }

        @Override
        protected boolean performSingleTargetAttack(Creature actor, Creature target) {
            final boolean result;

            if (actor.getCurrentHitPoints() < 20) {
                log.info("{} goes berserk!", actor.getName());
                actor.addCondition(new ExtraDamageDiceCondition(1));
                result = super.performSingleTargetAttack(actor, target);
                actor.removeCondition(ExtraDamageDiceCondition.class.getName());
            } else {
                result = super.performSingleTargetAttack(actor, target);
            }

            return result;
        }
    }


}