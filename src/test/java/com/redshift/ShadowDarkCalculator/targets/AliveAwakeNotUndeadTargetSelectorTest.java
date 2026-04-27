package com.redshift.ShadowDarkCalculator.targets;

import com.redshift.ShadowDarkCalculator.conditions.SleepingCondition;
import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.creatures.CreatureLabel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Test the AliveAwakeNotUndeadTargetSelector
 */

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AliveAwakeNotUndeadTargetSelectorTest {

    @Mock
    private Creature creature1;

    @Mock
    private Creature creature2;

    @Mock
    private Creature creature3;

    @Mock
    private Creature creature4;

    @Mock
    private Creature creature5;

    @Test
    void testValidTargets() {
        Mockito.when(creature1.isUnconscious()).thenReturn(false);
        Mockito.when(creature2.isUnconscious()).thenReturn(true);
        Mockito.when(creature3.isUnconscious()).thenReturn(false);
        Mockito.when(creature4.isUnconscious()).thenReturn(false);
        Mockito.when(creature5.isUnconscious()).thenReturn(false); // Selected creature

        Mockito.when(creature1.isDead()).thenReturn(true);
        Mockito.when(creature2.isDead()).thenReturn(false);
        Mockito.when(creature3.isDead()).thenReturn(false);
        Mockito.when(creature4.isDead()).thenReturn(false);
        Mockito.when(creature5.isDead()).thenReturn(false); // Selected creature

        Mockito.when(creature1.getLabels()).thenReturn(Set.of());
        Mockito.when(creature2.getLabels()).thenReturn(Set.of());
        Mockito.when(creature3.getLabels()).thenReturn(Set.of(CreatureLabel.UNDEAD));
        Mockito.when(creature4.getLabels()).thenReturn(Set.of());
        Mockito.when(creature5.getLabels()).thenReturn(Set.of()); // Selected creature

        Mockito.when(creature1.hasCondition(SleepingCondition.class.getName())).thenReturn(false);
        Mockito.when(creature2.hasCondition(SleepingCondition.class.getName())).thenReturn(false);
        Mockito.when(creature3.hasCondition(SleepingCondition.class.getName())).thenReturn(false);
        Mockito.when(creature4.hasCondition(SleepingCondition.class.getName())).thenReturn(true);
        Mockito.when(creature5.hasCondition(SleepingCondition.class.getName())).thenReturn(false); // Selected creature

        final AliveAwakeNotUndeadTargetSelector aliveAwakeNotUndeadTargetSelector = new AliveAwakeNotUndeadTargetSelector();
        final List<Creature> targets = aliveAwakeNotUndeadTargetSelector.getTargets(List.of(creature1, creature2, creature3, creature4, creature5), 5);

        assertEquals(1, targets.size());
        assertEquals(targets.getFirst(), creature5);
    }

}
