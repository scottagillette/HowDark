package com.redshift.ShadowDarkCalculator.encounter;

import com.redshift.ShadowDarkCalculator.creatures.Creature;
import lombok.Getter;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class CombatSimulator {

    private final List<Creature> group1;
    private final List<Creature> group2;

    private int group1Wins = 0;
    private int group1WinsWithDeath = 0;

    private int group2Wins = 0;
    private int group2WinsWithDeath = 0;

    public CombatSimulator(List<Creature> group1, List<Creature> group2) {
        this.group1 = group1;
        this.group2 = group2;
    }

    public void simulateFight() {

        int totalCreatures = group1.size() + group2.size();

        // Put each creature in the map with their available enemies and another for allies.

        final Map<Creature, List<Creature>> enemiesMap = new HashMap<>();

        group1.forEach(creature -> enemiesMap.put(creature, group2));
        group2.forEach(creature -> enemiesMap.put(creature, group1));

        final Map<Creature, List<Creature>> alliesMap = new HashMap<>();

        group1.forEach(creature -> alliesMap.put(creature, group1));
        group2.forEach(creature -> alliesMap.put(creature, group2));

        // Sort all by initiative; re-roll if there are ties.

        final Map<Integer, Creature> initiativeMap = new HashMap<>();

        while (true) {
            group1.forEach(creature -> {
                initiativeMap.put(creature.getInitiative(), creature);
            });
            group2.forEach(creature -> {
                initiativeMap.put(creature.getInitiative(), creature);
            });
            if (totalCreatures != initiativeMap.size()) {
                initiativeMap.clear();
            } else {
                break;
            }
        }

        boolean stopBattle = false;

        final List<Integer> sortedCreaturesInitiative = initiativeMap.keySet().stream().sorted(Comparator.reverseOrder()).toList();

        int i = 1;

        while (!stopBattle) {

            System.out.println("[ Round: " + i + " ]");

            for (Integer initiative : sortedCreaturesInitiative) {
                final Creature creature = initiativeMap.get(initiative);
                creature.takeTurn(enemiesMap.get(creature), alliesMap.get(creature));
            }

            i++;

            stopBattle = shouldStopBattle();
        }

        calculateDeath();

        reportFightSummary();
    }

    private void calculateDeath() {
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

    private boolean shouldStopBattle() {
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

        final boolean stopBattle = group1Remaining == 0 || group2Remaining == 0;

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

        return stopBattle;
    }

    private void reportFightSummary() {
        System.out.println("---------------------");
        group1.forEach(creature -> {
            System.out.println(creature.getName() + " hit points " + creature.getCurrentHitPoints() + "/" + creature.getMaxHitPoints() + " status=" + creature.getStatus());
        });

        group2.forEach(creature -> {
            System.out.println(creature.getName() + " hit points " + creature.getCurrentHitPoints() + "/" + creature.getMaxHitPoints() + " status=" + creature.getStatus());
        });
        System.out.println("---------------------");
    }

}
