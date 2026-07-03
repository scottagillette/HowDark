package com.redshift.ShadowDarkCalculator.api;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests the YAML -> FightConfig binding and that validation is wired into the parse path.
 */

class FightConfigLoaderTest {

    @Test
    void parsesPartyMonstersAndSimulations() {
        final String yaml = """
                simulations: 250
                party:
                  - name: Borlin
                    class: paladin
                  - name: Alaric
                    class: wizard
                    level: 2
                monsters:
                  - type: goblin
                    count: 4
                  - type: goblin-shaman
                """;

        final FightConfig config = FightConfigLoader.parse(yaml);

        assertEquals(250, config.getSimulations());
        assertEquals(2, config.getParty().size());
        assertEquals(2, config.getMonsters().size());

        // 'class' is a reserved word mapped via @JsonProperty; confirm it binds to playerClass.
        assertEquals("paladin", config.getParty().get(0).getPlayerClass());
        assertEquals("Borlin", config.getParty().get(0).getName());

        // Level defaults to 1 when omitted, and is read when present.
        assertEquals(1, config.getParty().get(0).getLevel());
        assertEquals(2, config.getParty().get(1).getLevel());

        assertEquals("goblin", config.getMonsters().get(0).getType());
        assertEquals(4, config.getMonsters().get(0).getCount());

        // Count defaults to 1 when omitted.
        assertEquals(1, config.getMonsters().get(1).getCount());
    }

    @Test
    void parsesCustomPlayerLoadout() {
        final String yaml = """
                simulations: 10
                party:
                  - name: Borlin
                    class: paladin
                    stats: { str: 18, dex: 13, con: 14, int: 8, wis: 7, cha: 9 }
                    ac: 16
                    hp: 20
                    luckToken: true
                    healingPotions: 2
                    weapons:
                      - type: bastard-sword-1h
                        magical: true
                        attackRollBonus: 1
                    spells:
                      - type: cure-wounds
                        priority: 10
                        spellCheckBonus: 1
                        advantage: true
                monsters:
                  - type: goblin
                """;

        final PartyMemberConfig member = FightConfigLoader.parse(yaml).getParty().get(0);

        // Short stat forms and the ac/hp aliases bind to the full field names.
        assertEquals(18, member.getStats().getStrength());
        assertEquals(9, member.getStats().getCharisma());
        assertEquals(16, member.getArmorClass());
        assertEquals(20, member.getHitPoints());

        assertTrue(member.isLuckToken());
        assertEquals(2, member.getHealingPotions());
        assertTrue(member.hasCustomLoadout());

        final WeaponConfig weapon = member.getWeapons().get(0);
        assertEquals("bastard-sword-1h", weapon.getType());
        assertTrue(weapon.isMagical());
        assertEquals(1, weapon.getAttackRollBonus());

        final SpellConfig spell = member.getSpells().get(0);
        assertEquals("cure-wounds", spell.getType());
        assertEquals(10, spell.getPriority());
        assertEquals(1, spell.getSpellCheckBonus());
        assertTrue(spell.isAdvantage());
    }

    @Test
    void incompleteStatsBlockFailsValidation() {
        // All six stats are required so a partial block can't silently mix in defaults.
        final String yaml = """
                simulations: 10
                party:
                  - class: fighter
                    stats: { str: 18 }
                monsters:
                  - type: goblin
                """;

        final IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () -> FightConfigLoader.parse(yaml));

        assertTrue(exception.getMessage().contains("stats"),
                () -> "expected a stats validation message, got: " + exception.getMessage());
    }

    @Test
    void emptyPartyFailsValidationThroughLoader() {
        final String yaml = """
                simulations: 10
                party: []
                monsters:
                  - type: goblin
                """;

        final IllegalArgumentException exception =
                assertThrows(IllegalArgumentException.class, () -> FightConfigLoader.parse(yaml));

        assertTrue(exception.getMessage().toLowerCase().contains("party"),
                () -> "expected a party validation message, got: " + exception.getMessage());
    }

    @Test
    void zeroSimulationsFailsValidationThroughLoader() {
        final String yaml = """
                simulations: 0
                party:
                  - class: fighter
                monsters:
                  - type: goblin
                """;

        assertThrows(IllegalArgumentException.class, () -> FightConfigLoader.parse(yaml));
    }

    @Test
    void unknownFieldIsRejected() {
        // Jackson fails on unknown properties by default; a typo'd field should not be ignored.
        final String yaml = """
                simulations: 10
                party:
                  - class: fighter
                monsters:
                  - type: goblin
                    kount: 3
                """;

        assertThrows(RuntimeException.class, () -> FightConfigLoader.parse(yaml));
    }

}
