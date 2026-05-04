package com.redshift.ShadowDarkCalculator.creatures.monsters.undead;

import com.redshift.ShadowDarkCalculator.actions.DamageType;
import com.redshift.ShadowDarkCalculator.actions.PerformAllActions;
import com.redshift.ShadowDarkCalculator.actions.weapons.Weapon;
import com.redshift.ShadowDarkCalculator.actions.weapons.WeaponBuilder;
import com.redshift.ShadowDarkCalculator.creatures.*;
import com.redshift.ShadowDarkCalculator.creatures.monsters.UndeadMonster;
import com.redshift.ShadowDarkCalculator.dice.Dice;
import com.redshift.ShadowDarkCalculator.dice.RollModifier;
import com.redshift.ShadowDarkCalculator.dice.ZeroDice;
import lombok.extern.slf4j.Slf4j;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.*;

/**
 * A pale, armored undead warrior with sinister intelligence.
 * AC 14 (chainmail), HP 15, ATK 1 bastard sword +3 (1d10) and 1 life drain +3, MV near
 * S +3, D +1, C +2, I +1, W +0, Ch +3, AL C, LV 3
 * Greater Undead. Immune to morale checks. Only damaged by silver or magical sources.
 * Life Drain. 1d4 CON damage. Death if reduced to 0 CON.
 */

@Slf4j
public class Wight extends UndeadMonster {

    public Wight(String name) {
        super(
                name,
                3,
                new Stats(17,12,14,12,10,17),
                14,
                D8.roll() + D8.roll() + D8.roll() + 2,
                new PerformAllActions(
                        WeaponBuilder.BASTARD_SWORD_2H.build(),
                        new LifeDrain()
                )
        );
        getLabels().add(CreatureLabel.FRONT_LINE);
        getLabels().add(CreatureLabel.HUMANOID);
    }

    @Override
    public void takeDamage(int amount, DamageType damageType) {
        // Take only silvered or magical damage!
        final boolean takeDamage = damageType.isSilvered() || damageType.isMagical();

        if (takeDamage) {
            super.takeDamage(amount, damageType);
        } else {
            log.info("{} takes no damage from non-silvered, non-magical damage!", getName());
        }
    }

    private static class LifeDrain extends Weapon {

        public LifeDrain() {
            // Life drain does no damage but only drains life!
            super("Life Drain", new ZeroDice(), RollModifier.STRENGTH);
            addSlashing();
        }

        @Override
        protected boolean performSingleTargetAttack(Creature actor, Creature target, String weaponName, Dice damageDice, RollModifier rollModifier) {
            final boolean attackHits = super.performSingleTargetAttack(actor, target, weaponName, damageDice, rollModifier);

            if (attackHits) {
                int constitutionRemaining = target.getStats().constitutionDrain(D4);
                if (constitutionRemaining == 0) {
                    log.info("{} is drained of constitution and DIES!", target.getName());
                    target.setDead(true);
                } else {
                    log.info("{} is drained of constitution to {}", target.getName(), constitutionRemaining);
                }
            }

            return attackHits;
        }
    }

}