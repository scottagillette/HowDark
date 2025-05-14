package com.redshift.ShadowDarkCalculator.targets;

import com.redshift.ShadowDarkCalculator.creatures.Creature;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test the FocusFireTargetSelector
 */

@ExtendWith(MockitoExtension.class)
class FocusFireTargetSelectorTest {

    @Mock
    private Creature creature1;

    @Mock
    private Creature creature2;

    @Mock
    private Creature creature3;

    @Mock
    private Creature creature4;

    @Test
    void testSelectWounded() {
        Mockito.when(creature1.isUnconscious()).thenReturn(true);
        Mockito.when(creature2.isUnconscious()).thenReturn(false);
        Mockito.when(creature3.isUnconscious()).thenReturn(false);
        Mockito.when(creature4.isUnconscious()).thenReturn(false);

//        Mockito.when(creature1.isDead()).thenReturn(false);
        Mockito.when(creature2.isDead()).thenReturn(true);
        Mockito.when(creature3.isDead()).thenReturn(false);
        Mockito.when(creature4.isDead()).thenReturn(false);

//        Mockito.when(creature1.isWounded()).thenReturn(true);
//        Mockito.when(creature2.isWounded()).thenReturn(true);
        Mockito.when(creature3.isWounded()).thenReturn(false);
        Mockito.when(creature4.isWounded()).thenReturn(true); // Must select

        final FocusFireTargetSelector focusFireTargetSelector = new FocusFireTargetSelector();
        final Creature target = focusFireTargetSelector.get(List.of(creature1, creature2, creature3, creature4));

        assertEquals(target, creature4);
    }

    @Test
    void testSelectConsciousNotDeadIfNoWounded() {
        Mockito.when(creature1.isUnconscious()).thenReturn(true);
        Mockito.when(creature2.isUnconscious()).thenReturn(false);
        Mockito.when(creature3.isUnconscious()).thenReturn(false);
        Mockito.when(creature4.isUnconscious()).thenReturn(false);

//        Mockito.when(creature1.isDead()).thenReturn(false);
        Mockito.when(creature2.isDead()).thenReturn(true);
        Mockito.when(creature3.isDead()).thenReturn(false);
        Mockito.when(creature4.isDead()).thenReturn(false);

//        Mockito.when(creature1.isWounded()).thenReturn(true);
//        Mockito.when(creature2.isWounded()).thenReturn(true);
        Mockito.when(creature3.isWounded()).thenReturn(false); // Can select
        Mockito.when(creature4.isWounded()).thenReturn(false); // Can select

        final FocusFireTargetSelector focusFireTargetSelector = new FocusFireTargetSelector();
        final Creature target = focusFireTargetSelector.get(List.of(creature1, creature2, creature3, creature4));

        assertTrue(target == creature3 || target == creature4);
    }

    @Test
    void testSelectNoCreature() {
        Mockito.when(creature1.isUnconscious()).thenReturn(false);
        Mockito.when(creature2.isUnconscious()).thenReturn(true);
        Mockito.when(creature3.isUnconscious()).thenReturn(false);
        Mockito.when(creature4.isUnconscious()).thenReturn(true);

        Mockito.when(creature1.isDead()).thenReturn(true);
//        Mockito.when(creature2.isDead()).thenReturn(false);
        Mockito.when(creature3.isDead()).thenReturn(true);
//        Mockito.when(creature4.isDead()).thenReturn(false);

        final FocusFireTargetSelector focusFireTargetSelector = new FocusFireTargetSelector();
        final Creature target = focusFireTargetSelector.get(List.of(creature1, creature2, creature3, creature4));

        assertNull(target);
    }
}
