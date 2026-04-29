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
 * Flitting, sentient shadows in the vague shape of a human.
 * AC 12, HP 15, ATK 2 touch +2 (1d4 + drain), MV near (fly)
 * S -4, D +2, C +2, I -2, W +0, Ch -1, AL C, LV 3
 * Drain. Target takes 1 STR damage. At 0 STR, target dies and becomes a shadow.
 */

@Slf4j
public class Shadow extends UndeadMonster {

    public Shadow(String name) {
        super(
                name,
                3,
                new Stats(3, 14, 14, 6, 10, 12),
                12,
                D8.roll() + D8.roll() + D8.roll() + 2,
                new PerformAllActions(
                        new DraiingTouch(),
                        new DraiingTouch()
                )
        );
        getLabels().add(CreatureLabel.FRONT_LINE);
    }

    public static class DraiingTouch extends Weapon {

        public DraiingTouch() {
            super("Draining Touch", D4, RollModifier.DEXTERITY);
        }

        @Override
        public void perform(Creature actor, List<Creature> enemies, List<Creature> allies, Encounter encounter) {
            final Creature target = actor.getSingleTargetSelector().get(enemies);

            if (target == null) {
                log.info("{} is skipping their turn... no target!", actor.getName());
            } else {
                boolean attackHits = performSingleTargetAttack(actor, target, getName(), damageDice, rollModifier);

                if (attackHits) {
                    final int currentStrength = target.getStats().strengthDrain(D1);
                    if (currentStrength == 0) {
                        log.info("{} is drained of strength to {} and DIES! A shadow rises from the corpse!", target.getName(), currentStrength);
                        target.setDead(true);
                        encounter.addFriendlyCreature(actor, new Shadow("Shadow of " + target.getName()));
                    } else {
                        log.info("{} is drained of strength to {}", target.getName(), currentStrength);
                    }
                }
            }
        }

    }
}