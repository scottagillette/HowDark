package com.redshift.ShadowDarkCalculator.creatures.monsters.beasts;

import com.redshift.ShadowDarkCalculator.actions.DamageType;
import com.redshift.ShadowDarkCalculator.actions.PerformAllActions;
import com.redshift.ShadowDarkCalculator.actions.weapons.Weapon;
import com.redshift.ShadowDarkCalculator.conditions.VoidSpiderPoisonedCondition;
import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.creatures.Stats;
import com.redshift.ShadowDarkCalculator.creatures.monsters.Monster;
import com.redshift.ShadowDarkCalculator.dice.Dice;
import com.redshift.ShadowDarkCalculator.dice.RollModifier;
import lombok.extern.slf4j.Slf4j;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D4;
import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D8;

/**
 * Pale, horse-sized arachnids that become ghostly and intangible.
 * AC 13, HP 23, ATK 2 bite +4 (1d8 + poison), MV near (climb)
 * S +3, D +3, C +1, I -1, W +1, Ch -2, AL C, LV 5
 * Impervious. Immune to cold.
 * Phase. Once per round, become corporeal or incorporeal.
 * Poison. DC 12 CON or drop to 0 HP in 1d4 rounds.
 */

@Slf4j
public class VoidSpider extends Monster {

    public VoidSpider(String name) {
        super(
                name,
                5,
                new Stats(16,16,12,8,12,6),
                13,
                D8.roll() + D8.roll() + D8.roll() + D8.roll() + D8.roll() + 1,
                new PerformAllActions(
                        new PoisonBite(),
                        new PoisonBite()
                )
            );

    }

    @Override
    public void takeDamage(int amount, DamageType damageType) {
        // Impervious. Immune to cold.

        if (damageType.isCold()) {
            log.info("{} resists all cold damage!", this.getName());
        } else {
            super.takeDamage(amount, damageType);
        }
    }


    private static class PoisonBite extends Weapon {

        private PoisonBite() {
            super("Bite", D8, RollModifier.DEXTERITY);
            addPiercing().addAttackRollBonus(1);
        }

        @Override
        protected boolean performSingleTargetAttack(Creature actor, Creature target, String weaponName, Dice damageDice, RollModifier rollModifier) {
            final boolean attackHits = super.performSingleTargetAttack(actor, target, weaponName, damageDice, rollModifier);

            if (attackHits) {
                if (target.getCurrentHitPoints() != 0) {
                    if (!target.hasCondition(VoidSpiderPoisonedCondition.class.getName())) {
                        final boolean constitutionSave = target.getStats().constitutionSave(12);
                        if (constitutionSave) {
                            log.info("{} resists the effects of poison.", target.getName());
                        } else {
                            log.info("{} is poisoned and staggers a bit.", target.getName());
                            target.addCondition(new VoidSpiderPoisonedCondition(D4.roll()));
                        }
                    }
                }
            }

            return attackHits;
        }
    }
}
