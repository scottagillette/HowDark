package com.redshift.ShadowDarkCalculator.creatures.monsters.demons;

import com.redshift.ShadowDarkCalculator.actions.weapons.Weapon;
import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.creatures.CreatureLabel;
import com.redshift.ShadowDarkCalculator.creatures.Stats;
import com.redshift.ShadowDarkCalculator.creatures.monsters.Monster;
import com.redshift.ShadowDarkCalculator.dice.RollModifier;
import com.redshift.ShadowDarkCalculator.encounter.Encounter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.*;

/**
 * A whispering, writhing shadow that coils and snaps like a whip.
 * AC 12, HP 11, ATK 1 chill touch +2 (1d6 + energy drain), MV near
 * S +0, D +2, C +2, I +0, W +1, Ch +0, AL C, LV 2
 * Energy Drain. 1d4 CON damage. If reduced to 0, become hexling.
 */

@Slf4j
public class Hexling extends Monster {

    public Hexling(String name) {
        super(
                name,
                1,
                new Stats(10,14,14,10,12,10),
                12,
                D8.roll() + D8.roll() + 2,
                new ChillTouch()
        );
        getLabels().add(CreatureLabel.FRONT_LINE);
    }

    private static class ChillTouch extends Weapon {

        public ChillTouch() {
            super("Chill Touch", D6, RollModifier.DEXTERITY);
        }

        @Override
        public void perform(Creature actor, List<Creature> enemies, List<Creature> allies, Encounter encounter) {
            final Creature target = actor.getSingleTargetSelector().get(enemies);

            if (target == null) {
                log.info("{} is skipping their turn... no target!", actor.getName());
            } else {
                boolean attackHits = performSingleTargetAttack(actor, target, getName(), damageDice, rollModifier);

                if (attackHits) {
                    // Loose 1d4 CON
                    int constitutionRemaining = target.getStats().constitutionDrain(D4);
                    if (constitutionRemaining == 0) {
                        log.info("{} is drained of constitution and DIES! A new vampire spawn rises!", target.getName());
                        target.setDead(true);
                        encounter.addFriendlyCreature(actor, new Hexling("Hexling " + target.getName()));
                    } else {
                        log.info("{} is drained of constitution to {}", target.getName(), constitutionRemaining);
                    }
                }
            }
        }
    }
}
