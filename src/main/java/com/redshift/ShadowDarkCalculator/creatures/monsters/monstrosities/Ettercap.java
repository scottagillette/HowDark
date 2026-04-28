package com.redshift.ShadowDarkCalculator.creatures.monsters.monstrosities;

import com.redshift.ShadowDarkCalculator.actions.PerformAllActions;
import com.redshift.ShadowDarkCalculator.actions.PerformOneAction;
import com.redshift.ShadowDarkCalculator.actions.weapons.Weapon;
import com.redshift.ShadowDarkCalculator.conditions.PoisonWebCondition;
import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.creatures.CreatureLabel;
import com.redshift.ShadowDarkCalculator.creatures.monsters.Monster;
import com.redshift.ShadowDarkCalculator.creatures.Stats;
import com.redshift.ShadowDarkCalculator.dice.Dice;
import com.redshift.ShadowDarkCalculator.dice.RollModifier;
import com.redshift.ShadowDarkCalculator.dice.ZeroDice;
import lombok.extern.slf4j.Slf4j;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.*;

/**
 * Bipedal, eight-eyed spiderfolk with spindly legs and purple fur.
 * AC 12, HP 14, ATK 2 bite +2 (1d6) or 1 poison web (near) +2, MV near (climb)
 * S +0, D +2, C +1, I +0, W +0, Ch -1, AL C, LV 3
 * Poison Web. One target stuck in place and 1d4 damage/round. DC 12 DEX on turn to escape.
 */

@Slf4j
public class Ettercap extends Monster {

    public Ettercap(String name) {
        super(
                name,
                3,
                new Stats(10,14,12,10,10,8),
                12,
                D8.roll() + D8.roll() + D8.roll() + 1,
                new PerformOneAction(
                        new PerformAllActions(
                                new Weapon("Bite", D6, RollModifier.DEXTERITY),
                                new Weapon("Bite", D6, RollModifier.DEXTERITY)
                        ),
                        new PoisonWeb()
                )
        );
        getLabels().add(CreatureLabel.BACKLINE);
    }

    public static class PoisonWeb extends Weapon {

        public PoisonWeb() {
            super("Poison Web", new ZeroDice(), RollModifier.DEXTERITY);
        }

        @Override
        protected boolean performSingleTargetAttack(Creature actor, Creature target, String weaponName, Dice damageDice, RollModifier rollModifier) {
            final boolean targetHit = super.performSingleTargetAttack(actor, target, weaponName, damageDice, rollModifier);

            if (targetHit) {
                target.addCondition(new PoisonWebCondition());
            }

            return targetHit;
        }
    }

}