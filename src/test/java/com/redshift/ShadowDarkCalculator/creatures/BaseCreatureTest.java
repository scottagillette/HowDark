package com.redshift.ShadowDarkCalculator.creatures;

import com.redshift.ShadowDarkCalculator.actions.weapons.WeaponBuilder;
import com.redshift.ShadowDarkCalculator.conditions.ShieldOfFaithCondition;
import com.redshift.ShadowDarkCalculator.conditions.SleepingCondition;
import com.redshift.ShadowDarkCalculator.conditions.UnconciousCondition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test the BaseCreature class.
 */

@ExtendWith(MockitoExtension.class)
public class BaseCreatureTest {

    private Monster monster;
    private Player player;

    @BeforeEach
    public void setup() {
        monster = new Monster(
                "Foo",
                1,
                new Stats(10,10,10,10,10,10),
                10,
                10,
                WeaponBuilder.CLUB.build()
        );

        player = new Player(
                "Foo",
                1,
                new Stats(10,10,10,10,10,10),
                10,
                10,
                WeaponBuilder.CLUB.build()
        );
    }

    @Test
    void testUnconsciousMonster() {
        monster.addCondition(new UnconciousCondition());
        monster.addCondition(new ShieldOfFaithCondition()); // Make sure it doesn't get AC from this...

        assertTrue(monster.isUnconscious());
        assertFalse(monster.canAct());
        assertEquals(monster.getAC(), 0);
        assertEquals(monster.getStatus(), "Unconscious");
    }

    @Test
    void testWounded() {
        monster.takeDamage(5, false, false, false, false);
        assertTrue(monster.isWounded());

        monster.healDamage(5);
        assertFalse(monster.isWounded());
    }

    @Test
    void testBloodied() {
        monster.takeDamage(5, false, false, false, false);
        assertTrue(monster.isBloodied());

        monster.healDamage(1);
        assertFalse(monster.isBloodied());
    }

    @Test
    void testTakingDamageWakesUpCreature() {
        monster.addCondition(new SleepingCondition());

        monster.takeDamage(1, false, false, false, false);
        assertFalse(monster.hasCondition(SleepingCondition.class.getName()));
        assertTrue(monster.canAct());
    }

    @Test
    void testTakingDamageKillsMonster() {
        monster.takeDamage(10, false, false, false, false);
        assertTrue(monster.isDead());
        assertTrue(monster.isBloodied());
        assertFalse(monster.canAct());
    }

    @Test
    void testTakingDamageDoesNotKillPlayer() {
        player.takeDamage(10, false, false, false, false);
        assertFalse(player.isDead());
        assertTrue(player.hasCondition(UnconciousCondition.class.getName()));
        assertFalse(player.canAct());
    }

    @Test
    void testAc() {
        assertEquals(monster.getAC(), 10);
        monster.addCondition(new ShieldOfFaithCondition());
        assertEquals(monster.getAC(), 12);
        monster.addCondition(new UnconciousCondition());
        assertEquals(monster.getAC(), 0);
    }
}
