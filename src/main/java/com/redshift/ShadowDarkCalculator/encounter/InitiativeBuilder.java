package com.redshift.ShadowDarkCalculator.encounter;

import com.redshift.ShadowDarkCalculator.creatures.Creature;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Calculates initiative order for the two groups.
 */

@Slf4j
public final class InitiativeBuilder {

    private InitiativeBuilder() {
        // Don't create
    }

    public static Map<Integer, Creature> build(List<Creature> group1, List<Creature> group2) {
        Map<Integer, Creature> result = new HashMap<>();

        final int totalCreatures = group1.size() + group2.size();

        while (true) {
            log.info("Rolling initiative...");

            group1.forEach(creature -> {
                result.put(creature.rollInitiative(), creature);
            });
            group2.forEach(creature -> {
                result.put(creature.rollInitiative(), creature);
            });

            if (totalCreatures != result.size()) {
                // TODO: rework initiative so that duplicates don't matter.
                log.info("... Re-rolling initiative");
                result.clear();
            } else {
                // We have unique initiative!
                break;
            }
        }

        return result;
    }
}
