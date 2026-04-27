package com.redshift.ShadowDarkCalculator.targets;

import com.redshift.ShadowDarkCalculator.creatures.Creature;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Test the RandomTargetSelector
 */

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class RandomTargetSelectorTest {

    @Mock
    private Creature creature1;

    @Mock
    private Creature creature2;

    @Mock
    private Creature creature3;

    @Test
    void testConsciousReturned() {
        Mockito.when(creature1.isUnconscious()).thenReturn(true); // Excluded
        Mockito.when(creature2.isUnconscious()).thenReturn(false);
        Mockito.when(creature3.isUnconscious()).thenReturn(false);

        Mockito.when(creature1.isDead()).thenReturn(false);
        Mockito.when(creature2.isDead()).thenReturn(true); // Excluded
        Mockito.when(creature3.isDead()).thenReturn(false);

        final RandomTargetSelector selector = new RandomTargetSelector();
        final Creature target = selector.get(List.of(creature1, creature2, creature3));

        assertNotNull(target);
        assertEquals(target, creature3);
    }

    @Test
    void testUnconsciousReturned() {
        Mockito.when(creature1.isUnconscious()).thenReturn(true); // Selected
        Mockito.when(creature2.isUnconscious()).thenReturn(false);

        Mockito.when(creature1.isDead()).thenReturn(false);
        Mockito.when(creature2.isDead()).thenReturn(true); // Excluded

        final RandomTargetSelector selector = new RandomTargetSelector();
        final Creature target = selector.get(List.of(creature1, creature2));

        assertNotNull(target);
        assertEquals(target, creature1);
    }
}
