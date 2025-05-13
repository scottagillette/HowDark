package com.redshift.ShadowDarkCalculator.actions;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

/**
 * Test the PerformAllActions
 */

@ExtendWith(MockitoExtension.class)
class PerformAllActionsTest {

    @Mock
    private Action action1;

    @Mock
    private Action action2;

    @Test
    void testCanPerform() {
        Mockito.when(action1.canPerform(any(), any(), any())).thenReturn(false);
        Mockito.when(action2.canPerform(any(), any(), any())).thenReturn(true);

        final PerformAllActions allActions = new PerformAllActions(action1, action2);
        final boolean canPerform = allActions.canPerform(null, null, null);

        assertTrue(canPerform);
    }

    @Test
    void testCantPerform() {
        Mockito.when(action1.canPerform(any(), any(), any())).thenReturn(false);
        Mockito.when(action2.canPerform(any(), any(), any())).thenReturn(false);

        final PerformAllActions allActions = new PerformAllActions(action1, action2);
        final boolean canPerform = allActions.canPerform(null, null, null);

        assertFalse(canPerform);
    }

    @Test
    void testPriority() {
        Mockito.when(action1.getPriority()).thenReturn(1);
        Mockito.when(action2.getPriority()).thenReturn(5);

        final PerformAllActions allActions = new PerformAllActions(action1, action2);
        final int priority = allActions.getPriority();

        assertEquals(5, priority);
    }

    @Test
    void testIsLostFalse() {
        Mockito.when(action1.isLost()).thenReturn(false);
        Mockito.when(action2.isLost()).thenReturn(true);

        final PerformAllActions allActions = new PerformAllActions(action1, action2);
        final boolean lost = allActions.isLost();

        assertFalse(lost);
    }

    @Test
    void testIsLostTrue() {
        Mockito.when(action1.isLost()).thenReturn(true);
        Mockito.when(action2.isLost()).thenReturn(true);

        final PerformAllActions allActions = new PerformAllActions(action1, action2);
        final boolean lost = allActions.isLost();

        assertTrue(lost);
    }

    @Test
    void testPerform() {
        Mockito.when(action1.canPerform(any(), any(), any())).thenReturn(false);
        Mockito.when(action2.canPerform(any(), any(), any())).thenReturn(true);

        final PerformAllActions allActions = new PerformAllActions(action1, action2);
        allActions.perform(null, null, null);

        Mockito.verify(action1, Mockito.times(0)).perform(any(), any(), any());
        Mockito.verify(action2, Mockito.times(1)).perform(any(), any(), any());
    }

}
