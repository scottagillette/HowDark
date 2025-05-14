package com.redshift.ShadowDarkCalculator.creatures;

import com.redshift.ShadowDarkCalculator.actions.weapons.WeaponBuilder;
import com.redshift.ShadowDarkCalculator.conditions.UnconciousCondition;
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

    @Test
    public void testDeadMonster() {
        final Monster monster = new Monster(
                "Foo",
                1,
                new Stats(10,10,10,10,10,10),
                10,
                10,
                WeaponBuilder.CLUB.build()
        );

        monster.takeDamage(10, false, false, false, false);

        assertTrue(monster.isDead());
        assertEquals("Dead", monster.getStatus());
        assertEquals(0, monster.getCurrentHitPoints());
        assertFalse(monster.hasCondition(UnconciousCondition.class.getName()));
        assertTrue(monster.getLabels().contains(Label.MONSTER));
        assertFalse(monster.canAct());
        assertEquals(1, monster.getLevel());
        assertFalse(monster.isUnconscious());
        assertEquals("Foo", monster.getName());
        assertEquals(10, monster.getAC());

        assertThrows(IllegalStateException.class, () -> {
            monster.takeTurn(List.of(), List.of()); // Dead can't take a turn!
        });

        // Now make sure healing a dead creature does nothing

        monster.healDamage(10);

        assertTrue(monster.isDead());
        assertEquals("Dead", monster.getStatus());
        assertEquals(0, monster.getCurrentHitPoints());


    }
}
