package com.redshift.ShadowDarkCalculator.actions.items;

import com.redshift.ShadowDarkCalculator.actions.Action;
import com.redshift.ShadowDarkCalculator.actions.BaseAction;
import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.encounter.Encounter;
import com.redshift.ShadowDarkCalculator.targets.single.HealTargetSelector;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D6;

/**
 * Ranger: DC 15 INT Curative. Equivalent to a Potion of Healing/
 */

@Slf4j
public class Curative extends BaseAction implements Action {

    private boolean used = false;
    private boolean advantage = false;

    public Curative() {
        super("Curative");
    }

    public Action addAdvantage() {
        this.advantage = true;
        return this;
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

        int roll = actor.getStats().intelligenceRoll();
        if (advantage) {
            roll = Math.max(roll, actor.getStats().intelligenceRoll());
        }

        if (roll >= 15) {
            int damageHealed = D6.roll();
            healingTarget.healDamage(damageHealed);
            log.info(
                    "{} heals {} with a curative for {} hit points.",
                    actor.getName(),
                    healingTarget.getName(),
                    damageHealed
            );
        } else {
            log.info("{} attempts to create a curative but fails!", actor.getName());
            used = true;
        }
    }

}
