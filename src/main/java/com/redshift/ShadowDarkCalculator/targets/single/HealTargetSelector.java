package com.redshift.ShadowDarkCalculator.targets.single;

import com.redshift.ShadowDarkCalculator.conditions.DiseasedCondition;
import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.dice.SingleDie;
import com.redshift.ShadowDarkCalculator.targets.SingleTargetSelector;

import java.util.List;

/**
 * Returns a single target for a healing; unconscious targets only.
 */

public class HealTargetSelector implements SingleTargetSelector {

    @Override
    public Creature get(List<Creature> targetOptions) {
        final List<Creature> unconsciousNotDead = targetOptions.stream()
                .filter(Creature::isUnconscious)
                .filter(creature -> !creature.isDead())
                .filter(creature -> !creature.hasFled())
                // Don't bother healing someone with diseased condition!
                .filter(creature -> !creature.hasCondition(DiseasedCondition.class.getName()))
                .toList();

        if (unconsciousNotDead.isEmpty()) {
             return null; // Don't bother healing wounded... wait until unconscious!
        } else {
            int selectionIndex = new SingleDie(unconsciousNotDead.size()).roll();
            return unconsciousNotDead.get(selectionIndex - 1); // Randomly choose an unconscious creature
        }
    }

}
