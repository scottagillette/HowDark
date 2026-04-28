package com.redshift.ShadowDarkCalculator.creatures.monsters.undead;

import com.redshift.ShadowDarkCalculator.actions.PerformAllActions;
import com.redshift.ShadowDarkCalculator.actions.weapons.Weapon;
import com.redshift.ShadowDarkCalculator.creatures.*;
import com.redshift.ShadowDarkCalculator.creatures.monsters.UndeadMonster;
import com.redshift.ShadowDarkCalculator.dice.RollModifier;
import com.redshift.ShadowDarkCalculator.encounter.Encounter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

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
    public void takeDamage(int amount, boolean silvered, boolean magical, boolean fire, boolean cold) {
        // Damage for fire or magical only.
        if (fire) {
            // Double damage for fire!
            log.info("{} takes DOUBLE damage from fire!", getName());
            super.takeDamage(amount + amount, silvered, magical, fire, cold);
        } else {
            if (magical) {
                super.takeDamage(amount, silvered, magical, fire, cold);
            } else {
                log.info("{} takes no damage from non-magical, non-fire damage!", getName());
            }
        }

    }

    private static class RotTouch extends Weapon {

        public RotTouch() {
            super("Rot Touch", D10, RollModifier.STRENGTH);
            addAttackRollBonus(5);
        }

        @Override
        public void perform(Creature actor, List<Creature> enemies, List<Creature> allies, Encounter encounter) {
            final Creature target = actor.getSingleTargetSelector().get(enemies);

            if (target == null) {
                log.info("{} is skipping their turn... no target!", actor.getName());
            } else {
                boolean attackHits = performSingleTargetAttack(actor, target, getName(), damageDice, rollModifier);

                if (attackHits & target.getCurrentHitPoints() != 0) {
                    if (target.getStats().constitutionSave(15)) {
                        log.info("{} SAVES and is NOT drained of health.", target.getName());
                    } else {
                        // HP 0
                        log.info("{} is drained of health and drops to 0 hit points!", target.getName());
                        target.takeDamage(999, false, false, false, false);
                    }
                }
            }
        }
    }

}