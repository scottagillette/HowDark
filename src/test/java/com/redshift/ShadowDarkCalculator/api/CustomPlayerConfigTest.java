package com.redshift.ShadowDarkCalculator.api;

import com.redshift.ShadowDarkCalculator.creatures.Creature;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests that a PartyMemberConfig with custom fields actually changes the built creature,
 * and that anything omitted still falls back to the class archetype.
 */

class CustomPlayerConfigTest {

    @Test
    void overridesAreAppliedToTheCreature() {
        final PartyMemberConfig config = new PartyMemberConfig();
        config.setName("Borlin");
        config.setPlayerClass("paladin");
        config.setArmorClass(16);
        config.setHitPoints(20);
        config.setLuckToken(true);

        final StatsConfig stats = new StatsConfig();
        stats.setStrength(18);
        stats.setDexterity(13);
        stats.setConstitution(14);
        stats.setIntelligence(8);
        stats.setWisdom(7);
        stats.setCharisma(9);
        config.setStats(stats);

        final WeaponConfig weapon = new WeaponConfig();
        weapon.setType("bastard-sword-1h");
        weapon.setMagical(true);
        config.setWeapons(List.of(weapon));

        final Creature creature = PlayerFactory.create(config);

        assertEquals(16, creature.getAC());
        assertEquals(20, creature.getMaxHitPoints());
        assertEquals(14, creature.getStats().getConstitution());
        assertTrue(creature.hasLuckToken());

        // The configured loadout replaces the class default (a non-magical sword).
        assertTrue(creature.getAction().isMagicalWeapon());
    }

    @Test
    void derivedHitPointsUseCustomConstitution() {
        final PartyMemberConfig config = new PartyMemberConfig();
        config.setPlayerClass("fighter");
        config.setLevel(3);

        final StatsConfig stats = new StatsConfig();
        stats.setStrength(16);
        stats.setDexterity(14);
        stats.setConstitution(18); // +4 modifier instead of the archetype's +1.
        stats.setIntelligence(8);
        stats.setWisdom(11);
        stats.setCharisma(10);
        config.setStats(stats);

        // Fighter: 8 base, then 2 levels of (5 hit die avg + 4 CON) = 26.
        assertEquals(26, PlayerFactory.create(config).getMaxHitPoints());
    }

    @Test
    void defaultsStillApplyWhenNothingIsOverridden() {
        final Creature wizard = PlayerFactory.create("wizard", "Alaric", 1);

        assertEquals(11, wizard.getAC());
        assertEquals(5, wizard.getMaxHitPoints());
        assertFalse(wizard.hasLuckToken());
        assertFalse(wizard.getAction().isMagicalWeapon());
    }

    @Test
    void unknownWeaponAndSpellTypesThrowWithAvailableNames() {
        final WeaponConfig weapon = new WeaponConfig();
        weapon.setType("chainsaw");

        final IllegalArgumentException weaponError =
                assertThrows(IllegalArgumentException.class, () -> WeaponFactory.create(weapon));
        assertTrue(weaponError.getMessage().contains("Available"));

        final SpellConfig spell = new SpellConfig();
        spell.setType("fireball");

        final IllegalArgumentException spellError =
                assertThrows(IllegalArgumentException.class, () -> SpellFactory.create(spell));
        assertTrue(spellError.getMessage().contains("Available"));
    }

    @Test
    void customPartyRunsAFightEndToEnd() {
        // The custom loadout path has to survive a real simulation, not just construction.
        final String yaml = """
                simulations: 5
                party:
                  - name: Torvin
                    class: priest
                    weapons:
                      - type: mace
                        priority: 1
                    spells:
                      - type: cure-wounds
                        priority: 10
                    healingPotions: 1
                monsters:
                  - type: goblin
                    count: 2
                """;

        final FightResult result = new FightSimulator().run(FightConfigLoader.parse(yaml));

        assertEquals(5, result.getPartyWins() + result.getMonsterWins());
    }

}
