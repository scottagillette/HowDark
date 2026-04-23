package com.redshift.ShadowDarkCalculator.actions.misc;

import com.redshift.ShadowDarkCalculator.actions.Action;
import com.redshift.ShadowDarkCalculator.actions.BaseAction;
import com.redshift.ShadowDarkCalculator.conditions.FearCondition;
import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.encounter.Encounter;
import com.redshift.ShadowDarkCalculator.targets.LivingTargetSelector;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D4;

/**
 * All creatures within near DC 15 CHA or flee in a random direction for 1d4 rounds.
 */

@Slf4j
public class WakingFear extends BaseAction implements Action {

    public WakingFear() {
        super("Waking Fear");
    }

    @Override
    public boolean canPerform(Creature actor, List<Creature> enemies, List<Creature> allies) {
        // Not dead, unconscious or undead targets.
        final List<Creature> targets = new LivingTargetSelector().getTargets(enemies, enemies.size());
        return !targets.isEmpty();
    }

    @Override
    public boolean isMagicalWeapon() {
        return false;
    }

    @Override
    public void perform(Creature actor, List<Creature> enemies, List<Creature> allies, Encounter encounter) {
        // All creatures within near DC 15 CHA or flee in a random direction for 1d4 rounds.

        final List<Creature> targets = new LivingTargetSelector().getTargets(enemies, enemies.size());

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
