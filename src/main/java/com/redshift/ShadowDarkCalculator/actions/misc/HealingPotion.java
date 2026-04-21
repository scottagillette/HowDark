package com.redshift.ShadowDarkCalculator.actions.misc;

import com.redshift.ShadowDarkCalculator.actions.Action;
import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.encounter.CombatSimulator;
import com.redshift.ShadowDarkCalculator.targets.HealerTargetSelector;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D6;

/**
 * An action to heal someone for D6 hps on a turn.
 */

@Slf4j
public class HealingPotion implements Action {

    private int priority = 1;
    private boolean used = false;

    @Override
    public boolean canPerform(Creature actor, List<Creature> enemies, List<Creature> allies) {
        final Creature healingTarget = new HealerTargetSelector().get(allies);
        return (!used && healingTarget != null);
    }

    @Override
    public String getName() {
        return "Healing Potion";
    }

    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    public boolean isLost() {
        return used;
    }

    @Override
    public boolean isMagical() {
        return true;
    }

    @Override
    public void perform(Creature actor, List<Creature> enemies, List<Creature> allies, CombatSimulator simulator) {
        used = true;

        final Creature healingTarget = new HealerTargetSelector().get(allies);

        int damageHealed = D6.roll();

        healingTarget.healDamage(damageHealed);
        log.info(
                "{} heals {} with a healing potion for {} hit points.",
                actor.getName(),
                healingTarget.getName(),
                damageHealed
        );
    }

    @Override
    public Action setPriority(int priority) {
        this.priority = priority;
        return this;
    }
}
