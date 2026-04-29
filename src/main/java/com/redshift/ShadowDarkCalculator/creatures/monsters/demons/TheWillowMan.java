package com.redshift.ShadowDarkCalculator.creatures.monsters.demons;

import com.redshift.ShadowDarkCalculator.actions.Action;
import com.redshift.ShadowDarkCalculator.actions.BaseAction;
import com.redshift.ShadowDarkCalculator.actions.PerformAllActions;
import com.redshift.ShadowDarkCalculator.actions.PerformOneAction;
import com.redshift.ShadowDarkCalculator.actions.weapons.Weapon;
import com.redshift.ShadowDarkCalculator.conditions.FearCondition;
import com.redshift.ShadowDarkCalculator.conditions.ParalyzedCondition;
import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.creatures.CreatureLabel;
import com.redshift.ShadowDarkCalculator.creatures.monsters.Monster;
import com.redshift.ShadowDarkCalculator.creatures.Stats;
import com.redshift.ShadowDarkCalculator.dice.MultipleDice;
import com.redshift.ShadowDarkCalculator.dice.RollModifier;
import com.redshift.ShadowDarkCalculator.encounter.Encounter;
import com.redshift.ShadowDarkCalculator.targets.AliveAwakeNotUndeadTargetSelector;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.*;

/**
 * A pale, faceless man with elongated limbs and curved talons that rake the ground. Moves in quick stutters during
 * each eye blink.
 * AC 17, HP 61, ATK 3 finger needle +9 (2d10) and 1 terrify, MV near (teleport)
 * S +5, D +7, C +3, I +4, W +4, Ch +5, AL C, LV 13
 * Fearless. Immune to morale checks.
 * Terrify. One target in near DC 15 CHA or paralyzed 1d4 rounds.
 * Waking Nightmare. In place of attacks, all creatures within near DC 15 CHA or flee in a random direction for 1d4 rounds.
 */

@Slf4j
public class TheWillowMan extends Monster {

    public TheWillowMan(String name) {
        super(
                name,
                13,
                new Stats(20,24,16,18,18,20),
                17,
                D8.roll() + D8.roll() + D8.roll() + D8.roll() + D8.roll() +
                        D8.roll() + D8.roll() + D8.roll() + D8.roll() + D8.roll() +
                        D8.roll() + D8.roll() + D8.roll() + 3,
                new PerformOneAction(
                        new PerformAllActions(
                                new Weapon("Finger Needle", new MultipleDice(D10, D10), RollModifier.DEXTERITY).addPiercing().addAttackRollBonus(5),
                                new Weapon("Finger Needle", new MultipleDice(D10, D10), RollModifier.DEXTERITY).addPiercing().addAttackRollBonus(5),
                                new Weapon("Finger Needle", new MultipleDice(D10, D10), RollModifier.DEXTERITY).addPiercing().addAttackRollBonus(5),
                                new Terrify()
                        ).setPriority(2),
                        new WakingNightmare().setPriority(1)
                )
        );
        getLabels().add(CreatureLabel.FRONT_LINE);
    }

    private static class Terrify extends BaseAction implements Action {

        public Terrify() {
            super("Terrify");
        }

        @Override
        public boolean canPerform(Creature actor, List<Creature> enemies, List<Creature> allies) {
            // Not dead, unconscious or undead targets.
            final List<Creature> targets = new AliveAwakeNotUndeadTargetSelector().getTargets(enemies, enemies.size());
            return !targets.isEmpty();
        }

        @Override
        public boolean isMagicalWeapon() {
            return false;
        }

        @Override
        public void perform(Creature actor, List<Creature> enemies, List<Creature> allies, Encounter encounter) {
            // One target in near DC 15 CHA or paralyzed 1d4 rounds.

            final List<Creature> targets = new AliveAwakeNotUndeadTargetSelector().getTargets(enemies, enemies.size());

            final Creature target = targets.getFirst();
            if (target.getStats().charismaSave(15)) {
                log.info("{} resists the Terrify effect from {}.", target.getName(), actor.getName());
            } else {
                log.info("{} is terrified and is paralyzed by {}.", target.getName(), actor.getName());
                target.addCondition(new ParalyzedCondition(D4.roll()));
            }

        }

    }

    private static class WakingNightmare extends BaseAction implements Action {

        private WakingNightmare() {
            super("Waking Nightmare");
        }

        @Override
        public boolean canPerform(Creature actor, List<Creature> enemies, List<Creature> allies) {
            // Not dead, unconscious or undead targets.
            final List<Creature> targets = new AliveAwakeNotUndeadTargetSelector().getTargets(enemies, enemies.size());
            return !targets.isEmpty();
        }

        @Override
        public boolean isMagicalWeapon() {
            return false;
        }

        @Override
        public void perform(Creature actor, List<Creature> enemies, List<Creature> allies, Encounter encounter) {
            // All creatures within near DC 15 CHA or flee in a random direction for 1d4 rounds.

            final List<Creature> targets = new AliveAwakeNotUndeadTargetSelector().getTargets(enemies, enemies.size());

            log.info("{} invokes Waking Fear to all living creatures!", actor.getName());

            targets.forEach(enemy -> {
                if (enemy.getStats().charismaSave(15)) {
                    log.info("{} resists the fear.", enemy.getName());
                } else {
                    log.info("{} runs in fear from {}", enemy.getName(), actor.getName());
                    enemy.addCondition(new FearCondition(D4.roll()));
                }
            });
        }

    }
}
