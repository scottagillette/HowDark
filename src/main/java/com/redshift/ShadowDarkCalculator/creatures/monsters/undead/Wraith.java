package com.redshift.ShadowDarkCalculator.creatures.monsters.undead;

import com.redshift.ShadowDarkCalculator.actions.PerformAllActions;
import com.redshift.ShadowDarkCalculator.actions.weapons.Weapon;
import com.redshift.ShadowDarkCalculator.creatures.*;
import com.redshift.ShadowDarkCalculator.creatures.monsters.UndeadMonster;
import com.redshift.ShadowDarkCalculator.dice.Dice;
import com.redshift.ShadowDarkCalculator.dice.RollModifier;
import lombok.extern.slf4j.Slf4j;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.*;

/**
 * A shadowy spirit seething with anger and malice. Its presence is unsettling to animals.
 * AC 14, HP 36, ATK 3 death touch +6 (1d10 + life drain), MV near (fly)
 * S -4, D +4, C +0, I +0, W +0, Ch +3, AL C, LV 8
 * Greater Undead. Immune to morale checks. Only damaged by silver or magical sources.
 * Incorporeal. In place of attacks, become corporeal or incorporeal.
 * Life Drain. 1d4 CON damage. Death if reduced to 0 CON.
 */

@Slf4j
public class Wraith extends UndeadMonster {

    public Wraith(String name) {
        super(
                name,
                8,
                new Stats(3,18,10,10,10,16),
                14,
                D8.roll() + D8.roll() + D8.roll() + D8.roll() + D8.roll() + D8.roll() + D8.roll() + D8.roll(),
                new PerformAllActions(
                        new DeathTouch(),
                        new DeathTouch(),
                        new DeathTouch()
                )
        );
        getLabels().add(CreatureLabel.FRONT_LINE);
    }

    @Override
    public void takeDamage(int amount, boolean silvered, boolean magical, boolean fire, boolean cold) {
        // Take only silvered or magical damage!
        final boolean takeDamage = silvered || magical;

        if (takeDamage) {
            super.takeDamage(amount, silvered, magical, fire, cold);
        } else {
            log.info("{} takes no damage from non-silvered, non-magical damage!", getName());
        }
    }

    private static class DeathTouch extends Weapon {

        public DeathTouch() {
            super("Death Touch", D10, RollModifier.DEXTERITY);
            addAttackRollBonus(2);
        }

        @Override
        protected boolean performSingleTargetAttack(Creature actor, Creature target, String weaponName, Dice damageDice, RollModifier rollModifier) {
            final boolean attackHits = super.performSingleTargetAttack(actor, target, weaponName, damageDice, rollModifier);

            if (attackHits) {
                int constitutionRemaining = target.getStats().constitutionDrain(D4);
                if (constitutionRemaining == 0) {
                    log.info("{} is drained of constitution to {} and DIES!", target.getName(), constitutionRemaining);
                    target.setDead(true);
                } else {
                    log.info("{} is drained of constitution to {}", target.getName(), constitutionRemaining);
                }
            }

            return attackHits;
        }
    }

}