package com.redshift.ShadowDarkCalculator.targets;

import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.creatures.Label;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test the LivingTargetSelector
 */

@ExtendWith(MockitoExtension.class)
class LivingTargetSelectorTest {

    @Mock
    private Creature creature1;

    @Mock
    private Creature creature2;

    @Mock
    private Creature creature3;

    @Test
    void testEmptyList() {
        Mockito.when(creature1.isUnconscious()).thenReturn(false);
        Mockito.when(creature2.isUnconscious()).thenReturn(true);
//        Mockito.when(creature3.isUnconscious()).thenReturn(false);

        Mockito.when(creature1.isDead()).thenReturn(true);
//        Mockito.when(creature2.isDead()).thenReturn(false);
//        Mockito.when(creature3.isDead()).thenReturn(false);

        Mockito.when(creature1.getLabels()).thenReturn(Set.of());
        Mockito.when(creature2.getLabels()).thenReturn(Set.of());
        Mockito.when(creature3.getLabels()).thenReturn(Set.of(Label.UNDEAD));

        final LivingTargetSelector livingTargetSelector = new LivingTargetSelector();
        final List<Creature> targets = livingTargetSelector.getTargets(List.of(creature1, creature2, creature3), 1);

        assertEquals(0, targets.size());
    }

    @Test
    void testOneInList() {
        Mockito.when(creature1.isUnconscious()).thenReturn(false);
        Mockito.when(creature2.isUnconscious()).thenReturn(false); // Return creature 2!
//        Mockito.when(creature3.isUnconscious()).thenReturn(false);

        Mockito.when(creature1.isDead()).thenReturn(true);
        Mockito.when(creature2.isDead()).thenReturn(false); // Return creature 2!
//        Mockito.when(creature3.isDead()).thenReturn(false);

        Mockito.when(creature1.getLabels()).thenReturn(Set.of());
        Mockito.when(creature2.getLabels()).thenReturn(Set.of());
        Mockito.when(creature3.getLabels()).thenReturn(Set.of(Label.UNDEAD));

        final LivingTargetSelector livingTargetSelector = new LivingTargetSelector();
        final List<Creature> targets = livingTargetSelector.getTargets(List.of(creature1, creature2, creature3), 1);

        assertEquals(1, targets.size());
        assertEquals(targets.getFirst(), creature2);
    }

}
