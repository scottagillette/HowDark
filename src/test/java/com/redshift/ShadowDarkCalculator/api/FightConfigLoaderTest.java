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
