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
 * A desiccated, linen-wrapped zombie. It was created with an intricate embalming ritual
 * used only upon the most worthy warriors or rulers.
 * AC 13, HP 47, ATK 3 rot touch +8 (1d10 + necrosis), MV near
 * S +3, D +0, C +2, I +3, W +2, Ch +3, AL C, LV 10
 * Supreme Undead. Immune to morale checks. Only damaged by magical sources.
 * Desiccated. Can be damaged by fire. Takes x2 damage from it.
 * Necrosis. DC 15 CON or drop to 0 HP. Healing spells are DC 15 to cast on target while at 0 HP due to this effect.
 */

@Slf4j
public class Mummy extends UndeadMonster {

    public Mummy(String name) {
        super(
                name,
                10,
                new Stats(17,10,15,17,15,17),
                13,
                D8.roll() + D8.roll() + D8.roll() + D8.roll() + D8.roll() + D8.roll() + D8.roll() + D8.roll() + D8.roll() + D8.roll() + 2,
                new PerformAllActions(
                        new RotTouch(),
                        new RotTouch(),
                        new RotTouch()
                )
        );
        getLabels().add(CreatureLabel.FRONT_LINE);
        getLabels().add(CreatureLabel.HUMANOID);
    }

    @Override
    public void takeDamage(int amount, boolean silvered, boolean magical, boolean fire, boolean cold, boolean piercing) {
        // Damage for fire or magical only.
        if (fire) {
            // Double damage for fire!
            log.info("{} takes DOUBLE damage from fire!", getName());
            super.takeDamage(amount + amount, silvered, magical, fire, cold, piercing);
        } else {
            if (magical) {
                super.takeDamage(amount, silvered, magical, fire, cold, piercing);
            } else {
                log.info("{} takes no damage from non-magical, non-fire damage!", getName());
            }
        }

    }

    private static class RotTouch extends Weapon {

        public RotTouch() {
            super("Rot Touch", D10, RollModifier.STRENGTH, false);
            addAttackRollBonus(5);
        }

        @Override
        protected boolean performSingleTargetAttack(Creature actor, Creature target, String weaponName, Dice damageDice, RollModifier rollModifier) {
            final boolean attackHits = super.performSingleTargetAttack(actor, target, weaponName, damageDice, rollModifier);

            if (attackHits & target.getCurrentHitPoints() != 0) {
                if (target.getStats().constitutionSave(15)) {
                    log.info("{} SAVES and is NOT drained of health.", target.getName());
                } else {
                    // HP 0
                    log.info("{} is drained of health and drops to 0 hit points!", target.getName());
                    target.takeDamage(999, false, false, false, false, false);
                }
            }

            return attackHits;
        }
    }

}