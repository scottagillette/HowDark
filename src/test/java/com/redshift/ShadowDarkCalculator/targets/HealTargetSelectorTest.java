package com.redshift.ShadowDarkCalculator.targets;

import com.redshift.ShadowDarkCalculator.creatures.Creature;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test the HealTargetSelector
 */

@ExtendWith(MockitoExtension.class)
class HealTargetSelectorTest {

    @Mock
    private Creature creature1;

    @Mock
    private Creature creature2;

    @Mock
    private Creature creature3;


    @Test
    void testUnconscious() {
        Mockito.when(creature1.isUnconscious()).thenReturn(false);
        Mockito.when(creature2.isUnconscious()).thenReturn(false);
        Mockito.when(creature3.isUnconscious()).thenReturn(true);

//        Mockito.when(creature1.isDead()).thenReturn(false); // Not called
//        Mockito.when(creature2.isDead()).thenReturn(false); // Not called
        Mockito.when(creature3.isDead()).thenReturn(false);
//
//        Mockito.when(creature1.isWounded()).thenReturn(true); // Not called
//        Mockito.when(creature2.isWounded()).thenReturn(false); // Not called
//        Mockito.when(creature3.isWounded()).thenReturn(true); // Not called

        final HealTargetSelector healTargetSelector = new HealTargetSelector();
        final Creature target = healTargetSelector.get(List.of(creature1, creature2, creature3));

        assertEquals(target, creature3);
    }

    @Test
    void testWounded() {
        Mockito.when(creature1.isUnconscious()).thenReturn(true);
        Mockito.when(creature2.isUnconscious()).thenReturn(false);
        Mockito.when(creature3.isUnconscious()).thenReturn(false);

        Mockito.when(creature1.isDead()).thenReturn(true);
//        Mockito.when(creature2.isDead()).thenReturn(false); // Not called
        Mockito.when(creature3.isDead()).thenReturn(false);

        Mockito.when(creature1.isWounded()).thenReturn(false);
        Mockito.when(creature2.isWounded()).thenReturn(false);
        Mockito.when(creature3.isWounded()).thenReturn(true);

        final HealTargetSelector healTargetSelector = new HealTargetSelector();
        final Creature target = healTargetSelector.get(List.of(creature1, creature2, creature3));

        assertEquals(target, creature3);
    }

    @Test
    void testNoWoundedOrUnconscious() {
        Mockito.when(creature1.isUnconscious()).thenReturn(false);
        Mockito.when(creature2.isUnconscious()).thenReturn(false);
        Mockito.when(creature3.isUnconscious()).thenReturn(false);

//        Mockito.when(creature1.isDead()).thenReturn(false); // Not called
//        Mockito.when(creature2.isDead()).thenReturn(false); // Not called
//        Mockito.when(creature3.isDead()).thenReturn(false); // Not called

        Mockito.when(creature1.isWounded()).thenReturn(false);
        Mockito.when(creature2.isWounded()).thenReturn(false);
        Mockito.when(creature3.isWounded()).thenReturn(false);

        final HealTargetSelector healTargetSelector = new HealTargetSelector();
        final Creature target = healTargetSelector.get(List.of(creature1, creature2, creature3));

        assertEquals(target, null);
    }
}
