package com.redshift.ShadowDarkCalculator.creatures.monsters.undead;

import com.redshift.ShadowDarkCalculator.actions.weapons.Weapon;
import com.redshift.ShadowDarkCalculator.conditions.ParalyzedCondition;
import com.redshift.ShadowDarkCalculator.creatures.*;
import com.redshift.ShadowDarkCalculator.creatures.monsters.UndeadMonster;
import com.redshift.ShadowDarkCalculator.dice.Dice;
import com.redshift.ShadowDarkCalculator.dice.RollModifier;
import lombok.extern.slf4j.Slf4j;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.*;
import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D4;

/**
 * Gray-skinned, slavering undead with whipping tongues and flat, reptilian faces.
 * AC 11, HP 11, ATK 1 claw +2 (1d6 + paralyze), MV near
 * S +2, D +1, C +2, I -3, W -1, Ch +0, AL C, LV 2
 * Undead. Immune to morale checks.
 * Paralyze. DC 12 CON or paralyzed 1d4 rounds.
 */

@Slf4j
public class Ghoul extends UndeadMonster {

    public Ghoul(String name) {
        super(
                name,
                2,
                new Stats(14, 12, 14, 4, 8, 10),
                11,
                D8.roll() + D8.roll() + 2,
                new ParalyzingClaw()
        );
        getLabels().add(CreatureLabel.FRONT_LINE);
        getLabels().add(CreatureLabel.HUMANOID);
    }

    private static class ParalyzingClaw extends Weapon {

        public ParalyzingClaw() {
            super("ParalyzingTouch Touch", D6, RollModifier.STRENGTH);
            addSlashing();
        }

        @Override
        protected boolean performSingleTargetAttack(Creature actor, Creature target, String weaponName, Dice damageDice, RollModifier rollModifier) {
            final boolean attackHits = super.performSingleTargetAttack(actor, target, weaponName, damageDice, rollModifier);

            if (attackHits) {
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

            return attackHits;
        }
    }

}