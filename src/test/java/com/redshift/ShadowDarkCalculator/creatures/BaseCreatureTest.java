package com.redshift.ShadowDarkCalculator.creatures;

import com.redshift.ShadowDarkCalculator.actions.DamageType;
import com.redshift.ShadowDarkCalculator.actions.weapons.WeaponBuilder;
import com.redshift.ShadowDarkCalculator.conditions.MageArmorCondition;
import com.redshift.ShadowDarkCalculator.conditions.ShieldOfFaithCondition;
import com.redshift.ShadowDarkCalculator.conditions.SleepingCondition;
import com.redshift.ShadowDarkCalculator.conditions.UnconsciousCondition;
import com.redshift.ShadowDarkCalculator.creatures.monsters.Monster;
import com.redshift.ShadowDarkCalculator.creatures.players.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

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
    void testUnconsciousPlayer() {
        player.addCondition(new UnconsciousCondition());
        player.addCondition(new MageArmorCondition(10, 14)); // Make sure it doesn't get AC from this...

        assertTrue(player.isUnconscious());
        assertFalse(player.canAct());
        assertEquals(player.getAC(), 0);
        assertEquals(player.getStatus(), "Unconscious");
    }

    @Test
    void testUnconsciousMonster() {
        monster.addCondition(new UnconsciousCondition());
        monster.addCondition(new ShieldOfFaithCondition()); // Make sure it doesn't get AC from this...

        assertTrue(monster.isUnconscious());
        assertFalse(monster.canAct());
        assertEquals(monster.getAC(), 0);
        assertEquals(monster.getStatus(), "Unconscious");
    }

    @Test
    void testWounded() {
        monster.takeDamage(5, new DamageType());
        assertTrue(monster.isWounded());

        monster.healDamage(5);
        assertFalse(monster.isWounded());
    }

    @Test
    void testBloodied() {
        monster.takeDamage(5, new DamageType());
        assertTrue(monster.isBloodied());

        monster.healDamage(1);
        assertFalse(monster.isBloodied());
    }

    @Test
    void testTakingDamageWakesUpCreature() {
        monster.addCondition(new SleepingCondition());

        monster.takeDamage(1, new DamageType());
        assertFalse(monster.hasCondition(SleepingCondition.class.getName()));
        assertTrue(monster.canAct());
    }

    @Test
    void testTakingDamageKillsMonster() {
        monster.takeDamage(10, new DamageType());
        assertTrue(monster.isDead());
        assertTrue(monster.isBloodied());
        assertFalse(monster.canAct());
    }

    @Test
    void testTakingDamageDoesNotKillPlayer() {
        player.takeDamage(10, new DamageType());
        assertFalse(player.isDead());
        assertTrue(player.hasCondition(UnconsciousCondition.class.getName()));
        assertFalse(player.canAct());
    }

    @Test
    void testAc() {
        assertEquals(monster.getAC(), 10);
        monster.addCondition(new ShieldOfFaithCondition());
        assertEquals(monster.getAC(), 12);
        monster.addCondition(new UnconsciousCondition());
        assertEquals(monster.getAC(), 0);

        player.addCondition(new MageArmorCondition(10, 14));
        assertEquals(player.getAC(), 14);
    }
}
