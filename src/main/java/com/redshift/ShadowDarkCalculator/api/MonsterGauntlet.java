package com.redshift.ShadowDarkCalculator.api;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * Runs a party against one of every registered monster type, N simulations each, and prints
 * one aligned row per monster:
 *
 * <pre>
 *   Players:  wins=702,  winsWithDeath=175, arch-mage: wins=298, winsWithDeath=0
 *   Players:  wins=711,  winsWithDeath=103, druid:     wins=289, winsWithDeath=46
 * </pre>
 *
 * Per-action combat logging is suppressed so only the table is printed.
 *
 * <p>Run with:
 * <pre>{@code
 *   ./gradlew gauntlet                       # lost-citadel, 1000 sims, 1 of each monster
 *   ./gradlew gauntlet --args="500"          # 500 sims each
 *   ./gradlew gauntlet --args="1000 diablerie"
 *   ./gradlew gauntlet --args="1000 lost-citadel 8"   # 8 of each monster
 * }</pre>
 *
 * @param args optional: [simulations] [partyBuilder] [monsterCount]
 */

public class MonsterGauntlet {

    public static void main(String[] args) {
        final int simulations = args.length > 0 ? Integer.parseInt(args[0]) : 1000;
        final String partyName = args.length > 1 ? args[1] : "lost-citadel";
        final int monsterCount = args.length > 2 ? Integer.parseInt(args[2]) : 1;

        // Silence the per-action combat logging; we only want the summary table.
        ((Logger) LoggerFactory.getLogger("com.redshift")).setLevel(Level.WARN);

        final FightSimulator simulator = new FightSimulator();

        System.out.printf(
                "[ Gauntlet: %s vs %d of each monster, %d simulations each ]%n%n",
                partyName, monsterCount, simulations);

        // Column widths so the tokens line up. "wins=NNNN," and "winsWithDeath=NNNN,"
        // are padded to a fixed width; the monster name column fits the longest type.
        final int winsWidth = "wins=0000,".length() + 1;
        final int deathWidth = "winsWithDeath=0000,".length() + 1;
        final int nameWidth = MonsterFactory.availableTypes().stream()
                .mapToInt(type -> type.length() + 2) // ":" plus a trailing space
                .max().orElse(20);

        for (String type : MonsterFactory.availableTypes()) {
            final FightConfig config = new FightConfig();
            config.setSimulations(simulations);
            config.setPartyBuilder(partyName);

            final MonsterConfig monster = new MonsterConfig();
            monster.setType(type);
            monster.setCount(monsterCount);
            config.setMonsters(List.of(monster));

            final FightResult result = simulator.run(config);

            final String partyWins = pad("wins=" + result.getPartyWins() + ",", winsWidth);
            final String partyDeaths = pad("winsWithDeath=" + result.getPartyWinsWithDeath() + ",", deathWidth);
            final String monsterName = pad(type + ":", nameWidth);
            final String monsterWins = pad("wins=" + result.getMonsterWins() + ",", winsWidth);
            final String monsterDeaths = "winsWithDeath=" + result.getMonsterWinsWithDeath();

            System.out.println(
                    "Players:  " + partyWins + partyDeaths + monsterName + monsterWins + monsterDeaths);
        }
    }

    private static String pad(String token, int width) {
        return String.format("%-" + width + "s", token);
    }

}
