package com.redshift.ShadowDarkCalculator.targets;

import com.redshift.ShadowDarkCalculator.conditions.DevouredCondition;
import com.redshift.ShadowDarkCalculator.creatures.Creature;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(MockitoExtension.class)
public class DeadCreatureTargetSelectorTest {

    @Mock
    private Creature creature1;

    @Mock
    private Creature creature2;

    @InjectMocks
    private DeadCreatureTargetSelector selector;

    @Test
    void testDeadCreatureReturned() {
        Mockito.when(creature1.isDead()).thenReturn(true);
        Mockito.when(creature1.hasCondition(DevouredCondition.class.getName())).thenReturn(false);
        Mockito.when(creature2.isDead()).thenReturn(false);

        final Creature selected = selector.get(List.of(creature1, creature2));

        assertEquals(selected, creature1);
    }

    @Test
    void testDeadCreatureNotReturnedBecauseNotDead() {
        Mockito.when(creature1.isDead()).thenReturn(false);
        Mockito.when(creature2.isDead()).thenReturn(false);

        final Creature selected = selector.get(List.of(creature1, creature2));

        assertNull(selected);
    }

    @Test
    void testDeadCreatureNotReturnedBecauseDevoured() {
        Mockito.when(creature1.isDead()).thenReturn(true);
        Mockito.when(creature1.hasCondition(DevouredCondition.class.getName())).thenReturn(true);
        Mockito.when(creature2.isDead()).thenReturn(true);
        Mockito.when(creature2.hasCondition(DevouredCondition.class.getName())).thenReturn(true);

        final Creature selected = selector.get(List.of(creature1, creature2));

        assertNull(selected);
    }
}
