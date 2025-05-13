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
class PerformOneActionTest {

    @Mock
    private Action action1;

    @Mock
    private Action action2;

    @Test
    void testCanPerform() {
        Mockito.when(action1.canPerform(any(), any(), any())).thenReturn(false);
        Mockito.when(action2.canPerform(any(), any(), any())).thenReturn(true);

        final PerformOneAction oneAction = new PerformOneAction(action1, action2);
        final boolean canPerform = oneAction.canPerform(null, null, null);

        assertTrue(canPerform);
    }

    @Test
    void testCantPerform() {
        Mockito.when(action1.canPerform(any(), any(), any())).thenReturn(false);
        Mockito.when(action2.canPerform(any(), any(), any())).thenReturn(false);

        final PerformOneAction oneAction = new PerformOneAction(action1, action2);
        final boolean canPerform = oneAction.canPerform(null, null, null);

        assertFalse(canPerform);
    }

    @Test
    void testPriority() {
        Mockito.when(action1.getPriority()).thenReturn(1);
        Mockito.when(action2.getPriority()).thenReturn(5);

        final PerformOneAction oneAction = new PerformOneAction(action1, action2);
        final int priority = oneAction.getPriority();

        assertEquals(5, priority);
    }

    @Test
    void testIsLostFalse() {
        Mockito.when(action1.isLost()).thenReturn(false);
        Mockito.when(action2.isLost()).thenReturn(true);

        final PerformOneAction oneAction = new PerformOneAction(action1, action2);
        final boolean lost = oneAction.isLost();

        assertFalse(lost);
    }

    @Test
    void testIsLostTrue() {
        Mockito.when(action1.isLost()).thenReturn(true);
        Mockito.when(action2.isLost()).thenReturn(true);

        final PerformOneAction oneAction = new PerformOneAction(action1, action2);
        final boolean lost = oneAction.isLost();

        assertTrue(lost);
    }

    @Test
    void testPerform() {
        Mockito.when(action1.canPerform(any(), any(), any())).thenReturn(false);
        Mockito.when(action2.canPerform(any(), any(), any())).thenReturn(true);

        //Mockito.when(action1.getPriority()).thenReturn(1); // Not called since it cant be performed.
        Mockito.when(action2.getPriority()).thenReturn(5);

        final PerformOneAction oneAction = new PerformOneAction(action1, action2);
        oneAction.perform(null, null, null);

        Mockito.verify(action1, Mockito.times(0)).perform(any(), any(), any());
        Mockito.verify(action2, Mockito.times(1)).perform(any(), any(), any());
    }

}
