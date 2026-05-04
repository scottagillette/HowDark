package com.redshift.ShadowDarkCalculator.creatures.monsters.beasts;

import com.redshift.ShadowDarkCalculator.actions.weapons.Weapon;
import com.redshift.ShadowDarkCalculator.conditions.ParalyzedCondition;
import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.creatures.Stats;
import com.redshift.ShadowDarkCalculator.creatures.monsters.Monster;
import com.redshift.ShadowDarkCalculator.dice.Dice;
import com.redshift.ShadowDarkCalculator.dice.RollModifier;
import lombok.extern.slf4j.Slf4j;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D4;
import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D8;

/**
 * Bulbous abdomen and eight, spindly legs. Dwells high in trees or caves and ambushes prey,
 * capturing them to eat later.
 * AC 13, HP 13, ATK 1 bite +3 (1d4 + poison), MV near (climb)
 * S +2, D +3, C +0, I -2, W +1, Ch -2, AL N, LV 3
 * Poison. DC 12 CON or paralyzed 1d4 hours.
 */

@Slf4j
public class GiantSpider extends Monster {

    public GiantSpider(String name) {
        super(
                name,
                3,
                new Stats(14,16,10,6,12,6),
                13,
                D8.roll() + D8.roll() + D8.roll(),
                new PoisonBite()
        );

    }

    private static class PoisonBite extends Weapon {

        private PoisonBite() {
            super("Poison Bite", D4, RollModifier.DEXTERITY);
            addPiercing();
        }

        @Override
        protected boolean performSingleTargetAttack(Creature actor, Creature target, String weaponName, Dice damageDice, RollModifier rollModifier) {
            final boolean attackHits = super.performSingleTargetAttack(actor, target, weaponName, damageDice, rollModifier);

            if (attackHits) {
                final boolean constitutionSave = target.getStats().constitutionSave(12);
                if (constitutionSave) {
                    log.info("{} resists the effects of poison.", target.getName());
                } else {
                    log.info("{} is poisoned and is paralyzed!", target.getName());
                    target.addCondition(new ParalyzedCondition(999)); // Technically D4 hours.
                }
            }

            return attackHits;
        }
    }
}
