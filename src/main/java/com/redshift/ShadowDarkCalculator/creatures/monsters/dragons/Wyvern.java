package com.redshift.ShadowDarkCalculator.creatures.monsters.dragons;

import com.redshift.ShadowDarkCalculator.actions.DamageType;
import com.redshift.ShadowDarkCalculator.actions.PerformAllActions;
import com.redshift.ShadowDarkCalculator.actions.weapons.Weapon;
import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.creatures.CreatureLabel;
import com.redshift.ShadowDarkCalculator.creatures.Stats;
import com.redshift.ShadowDarkCalculator.creatures.monsters.Monster;
import com.redshift.ShadowDarkCalculator.dice.MultipleDice;
import com.redshift.ShadowDarkCalculator.dice.RollModifier;
import lombok.extern.slf4j.Slf4j;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D10;
import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D8;

/**
 * Dragon-cousins with a large tail stinger, mottled lizard skin, and leathery wings.
 * AC 15, HP 37, ATK 2 rend +6 (1d8) and 1 stinger +6 (1d6 + poison), MV double near (fly)
 * S +4, D +2, C +1, I -3, W +1, Ch -3, AL N, LV 8
 * Poison. DC 15 CON or take 2d10 damage.
 */

@Slf4j
public class Wyvern extends Monster {

    public Wyvern(String name) {
        super(
                name,
                8,
                new Stats(18, 14, 12, 4, 12, 4),
                15,
                new MultipleDice(D8, D8, D8, D8, D8, D8, D8, D8).roll() + 1,
                new PerformAllActions(
                        new Weapon("Rending Claws", new MultipleDice(D8, D8), RollModifier.STRENGTH)
                                .addMagical()
                                .addSlashing()
                                .addAttackRollBonus(2),
                        new Weapon("Rending Claws", new MultipleDice(D8, D8), RollModifier.STRENGTH)
                                .addMagical()
                                .addSlashing()
                                .addAttackRollBonus(2),
                        new Stinger()
                )
        );
        getLabels().add(CreatureLabel.NEUTRAL);
    }

    /**
     * Stinger +6 (1d6 + poison) Poison. DC 15 CON or take 2d10 damage.
     */

    private static class Stinger extends Weapon {

        public Stinger() {
            super("Stinger", D8, RollModifier.STRENGTH);
            addPiercing();
        }

        @Override
        protected boolean performSingleTargetAttack(Creature actor, Creature target) {
            final boolean attackHits = super.performSingleTargetAttack(actor, target);

            if (attackHits && !target.isUnconscious() && !target.isDead()) {
                if (!target.getStats().constitutionSave(15)) {
                    int damage = new MultipleDice(D10, D10).roll();
                    log.info("{} is poisoned for {} damage!", target.getName(), damage);
                    target.takeDamage(damage, new DamageType().addPoison());
                } else {
                    log.info("{} SAVES and is NOT poisoned!", target.getName());
                }
            }

            return attackHits;
        }
    }
}
