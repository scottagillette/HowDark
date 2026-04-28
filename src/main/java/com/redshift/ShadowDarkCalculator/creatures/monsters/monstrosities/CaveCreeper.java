package com.redshift.ShadowDarkCalculator.creatures.monsters.monstrosities;

import com.redshift.ShadowDarkCalculator.actions.PerformAllActions;
import com.redshift.ShadowDarkCalculator.creatures.*;
import com.redshift.ShadowDarkCalculator.creatures.monsters.Monster;
import com.redshift.ShadowDarkCalculator.dice.Dice;
import com.redshift.ShadowDarkCalculator.dice.RollModifier;
import com.redshift.ShadowDarkCalculator.actions.weapons.Weapon;
import com.redshift.ShadowDarkCalculator.conditions.ParalyzedCondition;
import lombok.extern.slf4j.Slf4j;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.*;

/**
 * Chittering, green centipedes the size of horses. Their grasping tentacles are coated in a paralytic venom.
 * AC 12, HP 18, ATK 1 bite +3 (1d6) and 1 tentacles +3 (1d8 + toxin), MV near (climb)
 * S +2, D +2, C +0, I -3, W +1, Ch -3, AL N, LV 4
 * Toxin. DC 12 CON or paralyzed 1d4 rounds.
 */

@Slf4j
public class CaveCreeper extends Monster {

    public CaveCreeper(String name) {
        super(
                name,
                4,
                new Stats(14,14,10,4,12,4),
                12,
                D8.roll() + D8.roll() + D8.roll() + D8.roll(),
                new PerformAllActions(
                        new Weapon("Bite", D6, RollModifier.STRENGTH).addAttackRollBonus(1),
                        new Tentacles()
                )
        );
        getLabels().add(CreatureLabel.FRONT_LINE);
    }

    public static class Tentacles extends Weapon {

        public Tentacles() {
            super("Tentacles", D8, RollModifier.STRENGTH);
            addAttackRollBonus(1);
        }

        @Override
        protected boolean performSingleTargetAttack(Creature actor, Creature target, String weaponName, Dice damageDice, RollModifier rollModifier) {
            final boolean targetHit = super.performSingleTargetAttack(actor, target, getName(), damageDice, rollModifier);

            if (targetHit) {
                if (!target.hasCondition(ParalyzedCondition.class.getName())) {
                    if (!target.getStats().constitutionSave(12)) {
                        int rounds = D4.roll();
                        log.info("{} is paralyzed for {} rounds!", target.getName(), rounds);
                        target.addCondition(new ParalyzedCondition(rounds));
                    } else {
                        log.info("{} SAVES and is NOT paralyzed!", target.getName());
                    }
                }
            }
            return targetHit;
        }

    }

}
