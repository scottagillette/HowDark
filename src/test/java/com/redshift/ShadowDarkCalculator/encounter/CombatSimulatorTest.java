package com.redshift.ShadowDarkCalculator.encounter;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import com.redshift.ShadowDarkCalculator.actions.weapons.WeaponBuilder;
import com.redshift.ShadowDarkCalculator.creatures.Label;
import com.redshift.ShadowDarkCalculator.creatures.Player;
import com.redshift.ShadowDarkCalculator.creatures.Stats;
import com.redshift.ShadowDarkCalculator.creatures.goblinoid.Goblin;
import com.redshift.ShadowDarkCalculator.creatures.undead.Skeleton;
import com.redshift.ShadowDarkCalculator.party.TheCrabCrushersBuilderv2;

class CombatSimulatorTest {

    @Test
    void simulateFight_shouldEndWithWinner() {
        // Create a simple fighter
        Player fighter = new Player(
            "Test Fighter",
            1,
            new Stats(16, 14, 14, 10, 10, 10),
            15, // AC
            8,  // HP
            WeaponBuilder.LONGSWORD.build(2) // +2 attack bonus
        );
        fighter.getLabels().add(Label.BRUTE);

        // Create a goblin opponent
        Goblin goblin = new Goblin("Test Goblin");

        // Set up the combat
        CombatSimulator simulator = new CombatSimulator(
            List.of(fighter),
            List.of(goblin)
        );

        // Run the simulation
        simulator.simulateFight();

        // Verify that exactly one side won
        assertTrue(
            (simulator.getGroup1Wins() == 1 && simulator.getGroup2Wins() == 0) ||
            (simulator.getGroup1Wins() == 0 && simulator.getGroup2Wins() == 1),
            "Combat should end with exactly one winner"
        );

        // Verify that at least one creature is dead
        assertTrue(
            fighter.isDead() || goblin.isDead(),
            "At least one creature should be dead at the end of combat"
        );
    }

    @Test
    void simulateFight_shouldHandleMultipleCreatures() {
        // Create two fighters
        Player fighter1 = new Player(
            "Fighter 1",
            1,
            new Stats(16, 14, 14, 10, 10, 10),
            15,
            8,
            WeaponBuilder.LONGSWORD.build(2)
        );
        fighter1.getLabels().add(Label.BRUTE);

        Player fighter2 = new Player(
            "Fighter 2",
            1,
            new Stats(16, 14, 14, 10, 10, 10),
            15,
            8,
            WeaponBuilder.LONGSWORD.build(2)
        );
        fighter2.getLabels().add(Label.BRUTE);

        // Create two goblins
        Goblin goblin1 = new Goblin("Goblin 1");
        Goblin goblin2 = new Goblin("Goblin 2");

        // Set up the combat
        CombatSimulator simulator = new CombatSimulator(
            List.of(fighter1, fighter2),
            List.of(goblin1, goblin2)
        );

        // Run the simulation
        simulator.simulateFight();

        // Verify that exactly one side won
        assertTrue(
            (simulator.getGroup1Wins() == 1 && simulator.getGroup2Wins() == 0) ||
            (simulator.getGroup1Wins() == 0 && simulator.getGroup2Wins() == 1),
            "Combat should end with exactly one winner"
        );

        // Verify that at least one group is completely dead
        boolean group1AllDead = fighter1.isDead() && fighter2.isDead();
        boolean group2AllDead = goblin1.isDead() && goblin2.isDead();
        assertTrue(
            group1AllDead || group2AllDead,
            "One group should be completely defeated"
        );
    }

    @Test
    void simulateFight_crabCrushersVsSkeletons() {
        int group1Wins = 0;
        int group1WinsWithDeath = 0;

        int group2Wins = 0;
        int group2WinsWithDeath = 0;
            
        for (int i = 0; i < 100; i++) {
           //log.info("[ Fight: {} ]", i + 1);
            // Create the Crab Crushers party
            List<com.redshift.ShadowDarkCalculator.creatures.Creature> crabCrushers = new TheCrabCrushersBuilderv2().build();

            // Create four skeletons
            List<com.redshift.ShadowDarkCalculator.creatures.Creature> skeletons = List.of(
                new Skeleton("Skeleton 1"),
                new Skeleton("Skeleton 2"),
                new Skeleton("Skeleton 3"),
                new Skeleton("Skeleton 4")
            );

            // Set up the combat
            CombatSimulator simulator = new CombatSimulator(crabCrushers, skeletons);

            // Run the simulation
            simulator.simulateFight();

            // Verify that exactly one side won
            assertTrue(
                (simulator.getGroup1Wins() == 1 && simulator.getGroup2Wins() == 0) ||
                (simulator.getGroup1Wins() == 0 && simulator.getGroup2Wins() == 1),
                "Combat should end with exactly one winner"
            );

            // Verify that one group is completely defeated
            boolean crabCrushersAllDead = crabCrushers.stream().allMatch(com.redshift.ShadowDarkCalculator.creatures.Creature::isDead);
            boolean skeletonsAllDead = skeletons.stream().allMatch(com.redshift.ShadowDarkCalculator.creatures.Creature::isDead);
            assertTrue(
                crabCrushersAllDead || skeletonsAllDead,
                "One group should be completely defeated"
            );

            // Additional assertions specific to this encounter
            if (simulator.getGroup1Wins() == 1) {
                assertTrue(simulator.getGroup1WinsWithDeath() <= simulator.getGroup1Wins(),
                    "Wins with death should not exceed total wins for Crab Crushers");
            } else {
                assertTrue(simulator.getGroup2WinsWithDeath() <= simulator.getGroup2Wins(),
                    "Wins with death should not exceed total wins for Skeletons");
            }
            
            group1Wins = group1Wins + simulator.getGroup1Wins();
            group1WinsWithDeath = group1WinsWithDeath + simulator.getGroup1WinsWithDeath();

            group2Wins = group2Wins + simulator.getGroup2Wins();
            group2WinsWithDeath = group2WinsWithDeath + simulator.getGroup2WinsWithDeath();
        }

       // log.info("[ Outcome ]");
       // log.info("Players:  wins={}, winsWithDeath={}", group1Wins, group1WinsWithDeath);
       // log.info("Monsters: wins={}, winsWithDeath={}", group2Wins, group2WinsWithDeath);
    }
}