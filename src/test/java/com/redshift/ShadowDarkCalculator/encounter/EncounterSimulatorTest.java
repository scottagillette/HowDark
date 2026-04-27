package com.redshift.ShadowDarkCalculator.encounter;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

import com.redshift.ShadowDarkCalculator.creatures.players.Fighter;
import org.junit.jupiter.api.Test;

import com.redshift.ShadowDarkCalculator.actions.weapons.WeaponBuilder;
import com.redshift.ShadowDarkCalculator.creatures.Stats;
import com.redshift.ShadowDarkCalculator.creatures.monsters.goblinoid.Goblin;

/**
 * Tests a combat and asserts outcomes.
 */

class EncounterSimulatorTest {

    @Test
    void simulateEncounterShouldHandleMultipleCreatures() {
        Fighter fighter1 = new Fighter(
            "Fighter 1",
            1,
            new Stats(16, 14, 14, 10, 10, 10),
            15,
            8,
            WeaponBuilder.LONGSWORD.build().addDamageRollBonus(2)
        );

        Fighter fighter2 = new Fighter(
            "Fighter 2",
            1,
            new Stats(16, 14, 14, 10, 10, 10),
            15,
            8,
            WeaponBuilder.LONGSWORD.build().addAttackRollBonus(2)
        );

        Goblin goblin1 = new Goblin("Goblin 1");
        Goblin goblin2 = new Goblin("Goblin 2");

        EncounterSimulator simulator = new EncounterSimulator(
            List.of(fighter1, fighter2),
            List.of(goblin1, goblin2)
        );
        simulator.simulateEncounter();

        assertTrue(
            (simulator.getGroup1Wins() == 1 && simulator.getGroup2Wins() == 0) ||
            (simulator.getGroup1Wins() == 0 && simulator.getGroup2Wins() == 1),
            "Combat should end with exactly one winner"
        );

        boolean group1AllDead = fighter1.isDead() && fighter2.isDead();
        boolean group2AllDead = goblin1.isDead() && goblin2.isDead();

        assertTrue(
            group1AllDead || group2AllDead,
            "One group should be completely defeated"
        );
    }

}