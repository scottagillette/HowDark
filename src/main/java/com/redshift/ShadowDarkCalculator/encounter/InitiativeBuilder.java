package com.redshift.ShadowDarkCalculator.encounter;

import com.redshift.ShadowDarkCalculator.creatures.Creature;
import lombok.extern.slf4j.Slf4j;

import java.util.Comparator;
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

    public static Map<Double, Creature> build(List<Creature> group1, List<Creature> group2) {
        final Map<Double, Creature> result = new HashMap<>();

        final int totalCreatures = group1.size() + group2.size();

        while (true) {
            group1.forEach(creature -> {
                double initiative = creature.rollInitiative() + ((double) creature.getStats().getDexterityModifier() / 10);
                result.put(initiative, creature);
            });
            group2.forEach(creature -> {
                double initiative = creature.rollInitiative() + ((double) creature.getStats().getDexterityModifier() / 10);
                result.put(initiative, creature);
            });

            if (totalCreatures != result.size()) {
                // Re-Roll since we have duplicates
                result.clear();
            } else {
                // We have unique initiative!
                break;
            }
        }

        logInitiativeOrder(result);

        return result;
    }

    private static void logInitiativeOrder(Map<Double, Creature> result) {
        final List<Double> sortedCreaturesInitiative = result.keySet()
                .stream()
                .sorted(Comparator.reverseOrder())
                .toList();

        log.info("Initiative order:");
        for (Double initiative : sortedCreaturesInitiative) {
            final Creature creature = result.get(initiative);
            log.info("  {} {}", creature.getName(), initiative);
        }
    }
}
