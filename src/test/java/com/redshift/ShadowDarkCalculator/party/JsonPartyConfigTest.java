package com.redshift.ShadowDarkCalculator.party;

import com.redshift.ShadowDarkCalculator.creatures.Creature;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonPartyConfigTest {

    @Test
    void build_fromJson_shouldReturnCreatures() {
        // Build the expected party using the builder
        List<Creature> expected = new TheCrabCrushersBuilderv2().build();

        String json = """
            [
              {
                "name": "Karn Crabcrusher",
                "level": 1,
                "stats": {
                  "strength": 15,
                  "dexterity": 14,
                  "constitution": 14,
                  "intelligence": 14,
                  "wisdom": 11,
                  "charisma": 14
                },
                "armorClass": 15,
                "hitPoints": 7,
                "actions": [
                  {
                    "type": "weapon",
                    "weapon": "BASTARD_SWORD_1H",
                    "attackBonus": 3,
                    "damageBonus": 2
                  }
                ],
                "targetSelector": "FOCUS_FIRE",
                "labels": ["BRUTE", "PLAYER"]
              },
              {
                "name": "Kabsal Argent",
                "level": 1,
                "stats": {
                  "strength": 15,
                  "dexterity": 9,
                  "constitution": 13,
                  "intelligence": 10,
                  "wisdom": 17,
                  "charisma": 12
                },
                "armorClass": 12,
                "hitPoints": 7,
                "actions": [
                  {"type": "weapon", "weapon": "LONGSWORD", "attackBonus": 1},
                  {"type": "spell", "name": "TURN_UNDEAD", "bonus": 1, "priority": 4},
                  {"type": "spell", "name": "CURE_WOUNDS", "bonus": 1, "priority": 2},
                  {"type": "spell", "name": "SHIELD_OF_FAITH", "bonus": 1}
                ],
                "targetSelector": "FOCUS_FIRE",
                "labels": ["HEALER", "PLAYER"]
              },
              {
                "name": "Alderon",
                "level": 1,
                "stats": {
                  "strength": 8,
                  "dexterity": 13,
                  "constitution": 12,
                  "intelligence": 16,
                  "wisdom": 10,
                  "charisma": 8
                },
                "armorClass": 11,
                "hitPoints": 2,
                "actions": [
                  {"type": "weapon", "weapon": "STAFF"},
                  {"type": "spell", "name": "SLEEP", "bonus": 1, "priority": 3},
                  {"type": "spell", "name": "MAGIC_MISSILE", "bonus": 1, "advantage": true, "priority": 2},
                  {"type": "spell", "name": "BURNING_HANDS", "bonus": 1, "advantage": true, "priority": 4}
                ],
                "targetSelector": "FOCUS_FIRE",
                "labels": ["BACKLINE", "CASTER", "PLAYER"]
              },
              {
                "name": "Fennick Quickfoot",
                "level": 1,
                "stats": {
                  "strength": 10,
                  "dexterity": 10,
                  "constitution": 14,
                  "intelligence": 8,
                  "wisdom": 10,
                  "charisma": 8
                },
                "armorClass": 14,
                "hitPoints": 6,
                "actions": [
                  {"type": "weapon", "weapon": "CROSSBOW"}
                ],
                "targetSelector": "FOCUS_FIRE",
                "labels": ["BACKLINE", "PLAYER"]
              }
            ]
            """;

        JsonPartyConfig config = new JsonPartyConfig(json);
        List<Creature> actual = config.build();

        // Expect same number of creatures
        assertEquals(expected.size(), actual.size(), "Creature count should match parsed JSON");
    }
}
