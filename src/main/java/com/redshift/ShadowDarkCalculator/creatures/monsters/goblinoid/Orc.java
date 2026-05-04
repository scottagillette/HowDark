package com.redshift.ShadowDarkCalculator.creatures.monsters.goblinoid;

import com.redshift.ShadowDarkCalculator.actions.BaseAction;
import com.redshift.ShadowDarkCalculator.actions.PerformOneAction;
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
 * A tusked, tall humanoid with gray skin and pointed ears.
 * AC 15 (chainmail + shield), HP 4, ATK 1 greataxe +2 (1d8), MV near
 * S +2, D +0, C +0, I -1, W +0, Ch -1, AL C, LV 1
 * Rage. 1/day, immune to morale checks, +1d4 damage (3 rounds).
 */

@Slf4j
public class Orc extends Monster {

    public Orc(String name) {
        super(
                name,
                2,
                new Stats(14, 10, 10, 8,10, 8),
                15,
                D8.roll(),
                new PerformOneAction(
                        WeaponBuilder.GREAT_AXE_1H.build().setPriority(1),
                        new Rage().setPriority(1)
                )
        );
        getLabels().add(CreatureLabel.FRONT_LINE);
        getLabels().add(CreatureLabel.HUMANOID);
    }

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