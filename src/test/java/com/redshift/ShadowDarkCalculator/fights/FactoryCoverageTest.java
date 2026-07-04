package com.redshift.ShadowDarkCalculator.fights;

import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.creatures.monsters.config.MonsterFactory;
import com.redshift.ShadowDarkCalculator.creatures.players.config.SpellConfig;
import com.redshift.ShadowDarkCalculator.creatures.players.config.SpellFactory;
import com.redshift.ShadowDarkCalculator.creatures.players.config.WeaponConfig;
import com.redshift.ShadowDarkCalculator.creatures.players.config.WeaponFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * Guards that every registered monster type and player class can actually be constructed.
 * Method references are compile-checked, but a constructor that fails at runtime (or a bad
 * default stat block) would only surface when that entry is selected; these loops surface it.
 */

class FactoryCoverageTest {

    @Test
    void everyRegisteredMonsterTypeConstructs() {
        assertFalse(MonsterFactory.availableTypes().isEmpty(), "expected registered monster types");

        for (String type : MonsterFactory.availableTypes()) {
            final Creature monster = assertDoesNotThrow(
                    () -> MonsterFactory.create(type, "Test " + type),
                    () -> "monster type failed to construct: " + type);
            assertNotNull(monster, () -> "null monster for type: " + type);
        }
    }

    @Test
    void everyAvailableWeaponConstructs() {
        assertFalse(WeaponFactory.availableWeapons().isEmpty(), "expected available weapons");

        for (String type : WeaponFactory.availableWeapons()) {
            final WeaponConfig config = new WeaponConfig();
            config.setType(type);

            assertNotNull(assertDoesNotThrow(
                    () -> WeaponFactory.create(config),
                    () -> "weapon type failed to construct: " + type));
        }
    }

    @Test
    void everyAvailableSpellConstructs() {
        assertFalse(SpellFactory.availableSpells().isEmpty(), "expected available spells");

        for (String type : SpellFactory.availableSpells()) {
            final SpellConfig config = new SpellConfig();
            config.setType(type);

            assertNotNull(assertDoesNotThrow(
                    () -> SpellFactory.create(config),
                    () -> "spell type failed to construct: " + type));
        }
    }

}
