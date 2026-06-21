package com.redshift.ShadowDarkCalculator.encounter;

import com.redshift.ShadowDarkCalculator.creatures.Creature;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D20;

/**
 * Given two groups of creatures; roll initiative and proceed with combat until one group remains!
 */

@Getter
@Slf4j
public class EncounterSimulator implements Encounter {

    private int delaySeconds;

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
        // Create a copy of the lists so they can be mutated.
        this.group1 = new ArrayList<>(group1);
        this.group2 = new ArrayList<>(group2);
    }

    @Override
    public int getDelay() {
        return delaySeconds;
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

        // Give each creature a pre-combat phase.
        group1.forEach(creature -> creature.takePreCombatTurn(group2, group1, this));
        group2.forEach(creature -> creature.takePreCombatTurn(group1, group2, this));

        int round = 1;

        boolean continueBattle = true;

        while (continueBattle) {
            log.info("[ Round: {} ]", round);

            //log.info("Enter Something:");
            //Scanner myObj = new Scanner(System.in);
            //myObj.nextLine();

            // Sort creatures each turn since creatures may have been added
            sortedCreaturesInitiative = initiativeMap.keySet()
                    .stream()
                    .sorted(Comparator.reverseOrder())
                    .toList();

            for (Double initiative : sortedCreaturesInitiative) {
                final Creature creature = initiativeMap.get(initiative);

                if (!creature.isDead() && !creature.hasFled()) {
                    // Check Morale
                    final boolean creatureFlees = creatureFlees(creature, alliesMap.get(creature), D20.roll());

                    if (creatureFlees) {
                        creature.flee();
                    } else {
                        creature.takeTurn(enemiesMap.get(creature), alliesMap.get(creature), this);
                    }
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

    @Override
    public int setDelay(int delaySeconds) {
        this.delaySeconds = delaySeconds;
        return delaySeconds;
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

    private boolean creatureFlees(Creature creature, List<Creature> allies, int roll) {
        // Enemies who are half their number OR half HP single monster.
        // Flee if fail a DC 15 WIS check (highest WIS modifier)

        if (creature.willFlee()) {
            int wisdomModifier = -999;

            if (allies.size() == 1) {
                wisdomModifier = creature.getStats().getWisdomModifier();

                if (creature.isBloodied()) {
                    return (roll + wisdomModifier < 15);
                } else {
                    return false;
                }
            } else {
                int deadOrFledCount = 0;

                for (Creature ally : allies) {
                    if (ally.isDead() || ally.hasFled()) {
                        deadOrFledCount++;
                    } else {
                        wisdomModifier = Math.max(wisdomModifier, ally.getStats().getWisdomModifier());
                    }
                }

                if (deadOrFledCount >= (allies.size() / 2)) {
                    return (roll + wisdomModifier < 15);
                } else {
                    return false;
                }
            }
        } else {
            return false;
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
                .filter(creature -> !creature.hasFled())
                .toList()
                .size();

        int group2Remaining = group2.stream()
                .filter(creature -> !creature.isUnconscious())
                .filter(creature -> !creature.isDead())
                .filter(creature -> !creature.hasFled())
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
