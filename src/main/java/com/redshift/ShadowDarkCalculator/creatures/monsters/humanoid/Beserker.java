package com.redshift.ShadowDarkCalculator.creatures.monsters.humanoid;

import com.redshift.ShadowDarkCalculator.actions.BaseAction;
import com.redshift.ShadowDarkCalculator.actions.PerformAllActions;
import com.redshift.ShadowDarkCalculator.actions.weapons.WeaponBuilder;
import com.redshift.ShadowDarkCalculator.conditions.RageCondition;
import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.creatures.CreatureLabel;
import com.redshift.ShadowDarkCalculator.creatures.Stats;
import com.redshift.ShadowDarkCalculator.creatures.monsters.Monster;
import com.redshift.ShadowDarkCalculator.encounter.Encounter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D8;

/**
 * Howling, battleraging warriors.
 * AC 12 (leather), HP 10, ATK 1 greataxe +2 (1d10) or 1 spear (close/near) +2 (1d6), MV near
 * S +2, D +1, C +1, I +0, W +1, Ch +0, AL N, LV 2
 * Rage. 1/day, immune to morale checks, +1d4 damage (3 rounds).
 */

@Slf4j
public class Beserker extends Monster {

    public Beserker(String name) {
        super(
                name,
                2,
                new Stats(14, 12, 12, 10,12, 10),
                12,
                D8.roll() + D8.roll() + 1,
                new PerformAllActions(
                        new Rage(),
                        WeaponBuilder.GREAT_AXE_1H.build()
                )
        );
        getLabels().add(CreatureLabel.FRONT_LINE);
        getLabels().add(CreatureLabel.HUMANOID);
        getLabels().add(CreatureLabel.NEUTRAL);
    }

    /**
     * Rage. 1/day, immune to morale checks, +1d4 damage (3 rounds).
     */

    private static class Rage extends BaseAction {

        private boolean used;

        private Rage() {
            super("Rage");
        }

        @Override
        public boolean canPerform(Creature actor, List<Creature> enemies, List<Creature> allies) {
            return !used;
        }

        @Override
        public boolean isMagicalWeapon() {
            return false;
        }

        @Override
        public void perform(Creature actor, List<Creature> enemies, List<Creature> allies, Encounter encounter) {
            used = true;
            log.info("{} lets out a vicious battle cry and is Enraged!", actor.getName());
            actor.addCondition(new RageCondition(3));
        }
    }
}
