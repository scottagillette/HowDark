package com.redshift.ShadowDarkCalculator.creatures.monsters.dragons;

import com.redshift.ShadowDarkCalculator.actions.BaseAction;
import com.redshift.ShadowDarkCalculator.actions.PerformAllActions;
import com.redshift.ShadowDarkCalculator.actions.PerformOneAction;
import com.redshift.ShadowDarkCalculator.actions.weapons.Weapon;
import com.redshift.ShadowDarkCalculator.conditions.BlindedCondition;
import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.creatures.monsters.Monster;
import com.redshift.ShadowDarkCalculator.creatures.Stats;
import com.redshift.ShadowDarkCalculator.dice.MultipleDice;
import com.redshift.ShadowDarkCalculator.dice.RollModifier;
import com.redshift.ShadowDarkCalculator.encounter.Encounter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.*;

/**
 * This black, wingless beast slithers through dank swamps.
 * AC 16, HP 58, ATK 3 rend +8 (2d10) or 1 smog breath. MV double near (burrow, swim)
 * S +5, D +3, C +4, I +4, W +3, Ch +3, AL C, LV 12
 * Smog Breath. Fills a near-sized cube extending from dragon. DC 15 CON or 2d10 damage and blinded for 1 round.
 */

@Slf4j
public class SwampDragon extends Monster {

    public SwampDragon(String name) {
        super(
                name,
                12,
                new Stats(20,16,18,18,16,16),
                16,
                new MultipleDice(D8, D8, D8, D8, D8, D8, D8, D8, D8, D8, D8, D8).roll() + 4,
                new PerformOneAction(
                        new PerformAllActions(
                                new Weapon("Rend", new MultipleDice(D10, D10), RollModifier.STRENGTH).addAttackRollBonus(3),
                                new Weapon("Rend", new MultipleDice(D10, D10), RollModifier.STRENGTH).addAttackRollBonus(3),
                                new Weapon("Rend", new MultipleDice(D10, D10), RollModifier.STRENGTH).addAttackRollBonus(3)
                        ).setPriority(2),
                        new SmogBreath().setPriority(1) // Every third attack is smog breath!
                )
        );
    }


    /**
     * Smog Breath. Fills a near-sized cube extending from dragon. DC 15 CON or 2d10 damage and blinded for 1 round.
     */

    public static class SmogBreath extends BaseAction {

        public SmogBreath() {
            super("Smog Breath");
        }

        @Override
        public boolean canPerform(Creature actor, List<Creature> enemies, List<Creature> allies) {
            return true; // Can always be performed... yikes good luck players!
        }

        @Override
        public boolean isMagicalWeapon() {
            return false;
        }

        @Override
        public void perform(Creature actor, List<Creature> enemies, List<Creature> allies, Encounter encounter) {
            log.info("{} breaths acrid smog across the battle field!", actor.getName());

            enemies.forEach(creature -> {
                if (creature.isUnconscious() || creature.isDead()) {
                    // No damage
                } else {
                    if (creature.getStats().constitutionSave(15)) {
                        log.info("{} makes a CON save and takes no damage from Smog Breath!", creature.getName());
                    } else {
                        final int damage = new MultipleDice(D10, D10).roll();
                        log.info("{} is damaged by Smog Breath for {} damage and is blinded for 1 round!", creature.getName(), damage);
                        creature.takeDamage(damage, false, false, true, false);
                        creature.addCondition(new BlindedCondition(1));
                    }
                }
            });
        }
    }
}
