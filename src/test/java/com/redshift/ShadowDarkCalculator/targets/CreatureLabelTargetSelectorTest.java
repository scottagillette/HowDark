package com.redshift.ShadowDarkCalculator.targets;

import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.creatures.CreatureLabel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test the LivingTargetSelector
 */

@ExtendWith(MockitoExtension.class)
class CreatureLabelTargetSelectorTest {

    @Mock
    private Creature creature1;

    @Mock
    private Creature creature2;

    @Test
    void testSelector() {
        Mockito.when(creature1.getLabels()).thenReturn(Set.of(CreatureLabel.WITCH, CreatureLabel.BACKLINE));
        Mockito.when(creature2.getLabels()).thenReturn(Set.of(CreatureLabel.WIZARD, CreatureLabel.CASTER));

        List<Creature> creatures = new CreatureLabelTargetSelector(CreatureLabel.WITCH)
                .getTargets(List.of(creature1, creature2), 2);

        assertEquals(creatures.size(), 1);
        assertEquals(creatures.getFirst(), creature1);
    }
}
