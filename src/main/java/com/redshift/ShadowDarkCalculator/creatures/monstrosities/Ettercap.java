package com.redshift.ShadowDarkCalculator.creatures.monstrosities;

import com.redshift.ShadowDarkCalculator.actions.PerformAllAction;
import com.redshift.ShadowDarkCalculator.actions.PerformOneAction;
import com.redshift.ShadowDarkCalculator.actions.weapons.Weapon;
import com.redshift.ShadowDarkCalculator.conditions.PoisonWebCondition;
import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.creatures.Label;
import com.redshift.ShadowDarkCalculator.creatures.Monster;
import com.redshift.ShadowDarkCalculator.creatures.Stats;
import com.redshift.ShadowDarkCalculator.dice.Dice;
import com.redshift.ShadowDarkCalculator.dice.RollModifier;
import com.redshift.ShadowDarkCalculator.dice.ZeroDice;
import lombok.extern.slf4j.Slf4j;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.*;

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
                        new PerformAllAction(new Bite(),new Bite()),
                        new PoisonWeb()
                )
        );
        getLabels().add(Label.BRUTE);
    }

    public static class Bite extends Weapon {

        public Bite() {
            super("Bite", D6, RollModifier.DEXTERITY);
        }

    }

    public static class PoisonWeb extends Weapon {

        public PoisonWeb() {
            super("Poison Web", new ZeroDice(), RollModifier.STRENGTH);
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