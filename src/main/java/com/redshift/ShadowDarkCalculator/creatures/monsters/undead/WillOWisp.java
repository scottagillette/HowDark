package com.redshift.ShadowDarkCalculator.creatures.monsters.undead;

import com.redshift.ShadowDarkCalculator.actions.weapons.Weapon;
import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.creatures.CreatureLabel;
import com.redshift.ShadowDarkCalculator.creatures.Stats;
import com.redshift.ShadowDarkCalculator.creatures.monsters.UndeadMonster;
import com.redshift.ShadowDarkCalculator.dice.RollModifier;
import lombok.extern.slf4j.Slf4j;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.*;

/**
 * A bobbing marsh light animated by an evil spirit. It tries to lead the living into danger.
 * AC 13, HP 10, ATK 1 life drain +3, MV near (fly)
 * S -3, D +3, C +1, I -1, W -1, Ch -2, AL C, LV 2
 * Life Drain. 1d4 CON damage. Death if reduced to 0 CON.
 */

@Slf4j
public class WillOWisp extends UndeadMonster {

    public WillOWisp(String name) {
        super(
                name,
                2,
                new Stats(4,16,12,8,8,6),
                13,
                D8.roll() + D8.roll() + 1,
                new LifeDrain()
        );
        getLabels().add(CreatureLabel.CHAOTIC);
    }

    private static class LifeDrain extends Weapon {

        public LifeDrain() {
            super("Life Drain", D0, RollModifier.DEXTERITY);
        }

        @Override
        protected boolean performSingleTargetAttack(Creature actor, Creature target) {
            final boolean attackHits = super.performSingleTargetAttack(actor, target);

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
