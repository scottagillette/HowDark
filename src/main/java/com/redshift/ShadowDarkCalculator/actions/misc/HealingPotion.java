package com.redshift.ShadowDarkCalculator.actions.misc;

import com.redshift.ShadowDarkCalculator.actions.Action;
import com.redshift.ShadowDarkCalculator.actions.BaseAction;
import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.encounter.Encounter;
import com.redshift.ShadowDarkCalculator.targets.HealTargetSelector;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D6;

/**
 * An action to heal someone for D6 hps on a turn.
 */

@Slf4j
public class HealingPotion extends BaseAction implements Action {

    private boolean used = false;

    public HealingPotion() {
        super("Healing Potion");
    }

    @Override
    public boolean canPerform(Creature actor, List<Creature> enemies, List<Creature> allies) {
        final Creature healingTarget = new HealTargetSelector().get(allies);
        return (!used && healingTarget != null);
    }

    @Override
    public boolean isMagicalWeapon() {
        return false;
    }

    @Override
    public void perform(Creature actor, List<Creature> enemies, List<Creature> allies, Encounter encounter) {
        final Creature healingTarget = new HealTargetSelector().get(allies);

        int damageHealed = D6.roll();
        healingTarget.healDamage(damageHealed);

        used = true;

        log.info(
                "{} heals {} with a healing potion for {} hit points.",
                actor.getName(),
                healingTarget.getName(),
                damageHealed
        );
    }

}
