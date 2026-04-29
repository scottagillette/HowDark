package com.redshift.ShadowDarkCalculator.creatures.monsters.undead;

import com.redshift.ShadowDarkCalculator.actions.PerformAllActions;
import com.redshift.ShadowDarkCalculator.actions.weapons.Weapon;
import com.redshift.ShadowDarkCalculator.conditions.ParalyzedCondition;
import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.creatures.CreatureLabel;
import com.redshift.ShadowDarkCalculator.creatures.Stats;
import com.redshift.ShadowDarkCalculator.creatures.monsters.UndeadMonster;
import com.redshift.ShadowDarkCalculator.dice.Dice;
import com.redshift.ShadowDarkCalculator.dice.RollModifier;
import lombok.extern.slf4j.Slf4j;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.*;

/**
 * Greater ghouls who retain the intelligence they had in life.
 * AC 11, HP 20, ATK 2 claw +4 (1d8 + paralyze), MV near
 * S +3, D +1, C +2, I +0, W +0, Ch +2, AL C, LV 4
 * Undead. Immune to morale checks.
 * Carrion Stench. Living creatures DC 12 CON the first time within near or DISADV on attacks and spellcasting
 * for 5 rounds. // TODO: Not Implemented
 * Paralyze. DC 12 CON or paralyzed 1d4 rounds.
 */

@Slf4j
public class Ghast extends UndeadMonster {

    public Ghast(String name) {
        super(
                name,
                4,
                new Stats(17,12,14,10,10,14),
                11,
                D8.roll() + D8.roll() + D8.roll() + D8.roll() + 2,
                new PerformAllActions(
                        new ParalyzingClaw(),
                        new ParalyzingClaw()
                )
        );
        getLabels().add(CreatureLabel.FRONT_LINE);
        getLabels().add(CreatureLabel.HUMANOID);
    }

    private static class ParalyzingClaw extends Weapon {

        public ParalyzingClaw() {
            super("Paralyzing Claw", D8, RollModifier.STRENGTH);
        }

        @Override
        protected boolean performSingleTargetAttack(Creature actor, Creature target, String weaponName, Dice damageDice, RollModifier rollModifier) {
            final boolean attackHits = super.performSingleTargetAttack(actor, target, weaponName, damageDice, rollModifier);

            // TODO: Note - The Carrion Stench ability is not implemented...

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