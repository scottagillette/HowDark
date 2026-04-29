package com.redshift.ShadowDarkCalculator.creatures.monsters.undead;

import com.redshift.ShadowDarkCalculator.actions.DamageType;
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
 * Lesser, feral vampires born from the bite of their vampiric sires. Bloodthirsty and savage. They rarely leave
 * a victim alive.
 * AC 13 (leather), HP 25, ATK 2 bite +4 (1d8 + blood drain), MV near (climb)
 * S +3, D +2, C +3, I -1, W +1, Ch +2, AL C, LV 5
 *
 * Greater Undead. Immune to morale checks. Only damaged by silver or magical sources.
 *
 * Blood Drain. Vampire heals 2d6 HP and target permanently loses 1d4 CON. At 0 CON, target dies and rises as a vampire
 * spawn.
 *
 * Vampire. Must sleep in a coffin daily or loses 2d6 HP each day that can't be healed until resting in coffin.
 *
 * Takes 3d8 damage each round while in direct sunlight. Cannot be killed unless pierced through heart with a wooden
 * stake while at 0 HP.
 */

@Slf4j
public class VampireSpawn extends UndeadMonster {

    public VampireSpawn(String name) {
        super(
                name,
                5,
                new Stats(16,14,16,9,12,14),
                13,
                D8.roll() + D8.roll() + D8.roll() + D8.roll() + D8.roll() + 3,
                new PerformAllActions(
                        new Bite(),
                        new Bite()
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

    private static class Bite extends Weapon {

        public Bite() {
            super("Bite", D8, RollModifier.STRENGTH);
            addPiercing().addAttackRollBonus(1);
        }

        @Override
        public void perform(Creature actor, List<Creature> enemies, List<Creature> allies, Encounter encounter) {
            final Creature target = actor.getSingleTargetSelector().get(enemies);

            if (target == null) {
                log.info("{} is skipping their turn... no target!", actor.getName());
            } else {
                boolean attackHits = performSingleTargetAttack(actor, target, getName(), damageDice, rollModifier);

                if (attackHits) {
                    // Heal 2d6
                    actor.healDamage(D6.roll() + D6.roll());

                    // Loose 1d4 CON
                    int constitutionRemaining = target.getStats().constitutionDrain(D4);
                    if (constitutionRemaining == 0) {
                        log.info("{} is drained of constitution to {} and DIES! A new vampire spawn rises!", target.getName(), constitutionRemaining);
                        target.setDead(true);
                        encounter.addFriendlyCreature(actor, new VampireSpawn("Vampire Spawn " + target.getName()));
                    } else {
                        log.info("{} is drained of constitution to {}", target.getName(), constitutionRemaining);
                    }
                }
            }
        }
    }

}
