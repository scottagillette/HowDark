package com.redshift.ShadowDarkCalculator.conditions;

import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.dice.Dice;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EngulfedInAcidCondition implements Condition {

    private final Dice damageDice;

    public EngulfedInAcidCondition(Dice damageDice) {
        this.damageDice = damageDice;
    }

    @Override
    public boolean canAct() {
        return true; // You can act to try to end this condition.
    }

    @Override
    public boolean hasEnded(Creature creature) {
        if (creature.canAct()) {
            // DC 12 STR to end...
            return creature.getStats().strengthSave(12);
        } else {
            return false;
        }
    }

    @Override
    public void perform(Creature creature) {
        final int damage = damageDice.roll();
        log.info(creature.getName() + " has been burned by ACID for " + damage);
        creature.takeDamage(damage, false, false);
    }
}
