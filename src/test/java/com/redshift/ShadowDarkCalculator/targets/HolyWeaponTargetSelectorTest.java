package com.redshift.ShadowDarkCalculator.targets;

import com.redshift.ShadowDarkCalculator.actions.weapons.WeaponBuilder;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;

/**
 * Test the HolyWeaponTargetSelector
 */

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class HolyWeaponTargetSelectorTest {

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

    @Mock
    private Creature creature6;

    @Test
    void testSelection() {
        Mockito.when(creature1.isUnconscious()).thenReturn(true); // Excluded
        Mockito.when(creature2.isUnconscious()).thenReturn(false);
        Mockito.when(creature3.isUnconscious()).thenReturn(false);
        Mockito.when(creature4.isUnconscious()).thenReturn(false);
        Mockito.when(creature5.isUnconscious()).thenReturn(false);
        Mockito.when(creature6.isUnconscious()).thenReturn(false);

        Mockito.when(creature1.isDead()).thenReturn(false);
        Mockito.when(creature2.isDead()).thenReturn(true); // Excluded
        Mockito.when(creature3.isDead()).thenReturn(false);
        Mockito.when(creature4.isDead()).thenReturn(false);
        Mockito.when(creature5.isDead()).thenReturn(false);
        Mockito.when(creature6.isDead()).thenReturn(false);

        Mockito.when(creature1.getAction()).thenReturn(WeaponBuilder.CLUB.build());
        Mockito.when(creature2.getAction()).thenReturn(WeaponBuilder.CLUB.build());
        Mockito.when(creature3.getAction()).thenReturn(WeaponBuilder.CLUB.build().addMagical()); // Excluded
        Mockito.when(creature4.getAction()).thenReturn(WeaponBuilder.CLUB.build());
        Mockito.when(creature5.getAction()).thenReturn(WeaponBuilder.CLUB.build());
        Mockito.when(creature6.getAction()).thenReturn(WeaponBuilder.CLUB.build());

        Mockito.when(creature1.getLabels()).thenReturn(Set.of(CreatureLabel.FRONT_LINE));
        Mockito.when(creature2.getLabels()).thenReturn(Set.of(CreatureLabel.FRONT_LINE));
        Mockito.when(creature3.getLabels()).thenReturn(Set.of(CreatureLabel.FRONT_LINE));
        Mockito.when(creature4.getLabels()).thenReturn(Set.of()); // Excluded
        Mockito.when(creature5.getLabels()).thenReturn(Set.of(CreatureLabel.FRONT_LINE));
        Mockito.when(creature6.getLabels()).thenReturn(Set.of(CreatureLabel.FRONT_LINE));

        Mockito.when(creature1.hasCondition(anyString())).thenReturn(true);
        Mockito.when(creature2.hasCondition(anyString())).thenReturn(true);
        Mockito.when(creature3.hasCondition(anyString())).thenReturn(true);
        Mockito.when(creature4.hasCondition(anyString())).thenReturn(true);
        Mockito.when(creature5.hasCondition(anyString())).thenReturn(true);
        Mockito.when(creature6.hasCondition(anyString())).thenReturn(false); // Selected

        final HolyWeaponTargetSelector selector = new HolyWeaponTargetSelector();
        final Creature target = selector.get(List.of(creature1, creature2, creature3, creature4, creature5, creature6));

        assertEquals(creature6, target);
    }
}
