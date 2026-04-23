package com.redshift.ShadowDarkCalculator.encounter;

import com.redshift.ShadowDarkCalculator.creatures.Creature;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

/**
 * Given two groups of creatures; roll initiative and proceed with combat until one group remains!
 */

@Getter
@Slf4j
public class EncounterSimulator implements Encounter {

    private final List<Creature> group1;
    private final List<Creature> group2;

    private int group1Wins = 0;
    private int group1WinsWithDeath = 0;

    private int group2Wins = 0;
    private int group2WinsWithDeath = 0;

    private Map<Double, Creature> initiativeMap;
    private List<Double> sortedCreaturesInitiative;

    private final Map<Creature, List<Creature>> enemiesMap = new HashMap<>();
    private final Map<Creature, List<Creature>> alliesMap = new HashMap<>();

    private Double nextNewCreatureInitiative = Double.valueOf(-100);

    public EncounterSimulator(List<Creature> group1, List<Creature> group2) {
        this.group1 = group1;
        this.group2 = group2;
    }

    /**
     * Simulate an encounter between two groups of creatures.
     */

    public void simulateEncounter() {
        // Put each creature in the map with their available enemies and another for allies.
        group1.forEach(creature -> enemiesMap.put(creature, group2));
        group2.forEach(creature -> enemiesMap.put(creature, group1));

        group1.forEach(creature -> alliesMap.put(creature, group1));
        group2.forEach(creature -> alliesMap.put(creature, group2));

        // Sort all by initiative
        initiativeMap = InitiativeBuilder.build(group1, group2);

        int round = 1;

        boolean continueBattle = true;

        while (continueBattle) {
            log.info("[ Round: {} ]", round);

            // Sort creatures each turn since creatures may have been added
            sortedCreaturesInitiative = initiativeMap.keySet()
                    .stream()
                    .sorted(Comparator.reverseOrder())
                    .toList();

            for (Double initiative : sortedCreaturesInitiative) {
                final Creature creature = initiativeMap.get(initiative);
                if (!creature.isDead()) {
                    creature.takeTurn(enemiesMap.get(creature), alliesMap.get(creature), this);
                }
            }

            round++;
            continueBattle = shouldContinueBattle();
        }

        calculateDead();
        reportSummary();
    }

    @Override
    public void addFriendlyCreature(Creature actor, Creature newCreature) {
        if (group1.contains(actor)) {
            group1.add(newCreature);
            alliesMap.put(newCreature, group1);
            enemiesMap.put(newCreature, group2);
        }
        if (group2.contains(actor)) {
            group2.add(newCreature);
            alliesMap.put(newCreature, group2);
            enemiesMap.put(newCreature, group1);
        }

        initiativeMap.put(nextNewCreatureInitiative, newCreature); // Creature will act next round!
        nextNewCreatureInitiative = nextNewCreatureInitiative - 1; // Decrement the next new creature initiative value.
    }

    private void calculateDead() {
        int group1Remaining = group1.stream()
                .filter(Creature::canAct)
                .toList()
                .size();

        int group2Remaining = group2.stream()
                .filter(Creature::canAct)
                .toList()
                .size();

        if (group1Remaining == 0) {
            group1.forEach(creature -> creature.setDead(true));
        }

        if (group2Remaining == 0) {
            group2.forEach(creature -> creature.setDead(true));
        }
    }

    private void reportSummary() {
        log.info("---------------------------------------------------------------");

        group1.forEach(creature -> {
            log.info(
                    "{}: status={}, hitPoints={}/{}",
                    creature.getName(),
                    creature.getStatus(),
                    creature.getCurrentHitPoints(),
                    creature.getMaxHitPoints()
            );
        });

        group2.forEach(creature -> {
            log.info(
                    "{}: status={}, hitPoints={}/{}",
                    creature.getName(),
                    creature.getStatus(),
                    creature.getCurrentHitPoints(),
                    creature.getMaxHitPoints()
            );
        });

        log.info("---------------------------------------------------------------");
    }

    private boolean shouldContinueBattle() {
        int group1Remaining = group1.stream()
                .filter(creature -> !creature.isUnconscious())
                .filter(creature -> !creature.isDead())
                .toList()
                .size();

        int group2Remaining = group2.stream()
                .filter(creature -> !creature.isUnconscious())
                .filter(creature -> !creature.isDead())
                .toList()
                .size();

        final boolean continueBattle = group1Remaining != 0 && group2Remaining != 0;

        if (group1Remaining == 0) {
            group2Wins++;

            final List<Creature> group2NotDead = group2.stream().filter(creature -> !creature.isDead()).toList();

            if (group2NotDead.size() != group2.size()) {
                group2WinsWithDeath++;
            }
        }

        if (group2Remaining == 0) {
            group1Wins++;

            final List<Creature> group1NotDead = group1.stream().filter(creature -> !creature.isDead()).toList();

            if (group1NotDead.size() != group1.size()) {
                group1WinsWithDeath++;
            }
        }

        return continueBattle;
    }
}
