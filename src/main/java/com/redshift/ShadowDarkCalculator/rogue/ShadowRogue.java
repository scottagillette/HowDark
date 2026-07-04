package com.redshift.ShadowDarkCalculator.rogue;

import java.nio.file.Path;
import java.util.Scanner;

import com.redshift.ShadowDarkCalculator.api.FightConfig;
import com.redshift.ShadowDarkCalculator.api.FightConfigLoader;
import com.redshift.ShadowDarkCalculator.api.FightResult;
import com.redshift.ShadowDarkCalculator.api.FightSimulator;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ShadowRogue {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String inputString;
        Player player = new Player();

        do {
            if(player.getName() == null) {
                log.info("Welcome to shadow rogue!");
                log.info("What is your name?");
                inputString = scanner.nextLine();
                player.setName(inputString);
            }
            log.info(player.getName() + "! You have " + player.getGold() + " gold and " + player.getReputation() + " reputation.");
            log.info("Your party consists of: " + player.getPartyName());
            log.info("Your enemy is: " + player.getEnemyName());
            log.info("Press 1 to go to battle, 2 to select a party, 3 to select an enemy, or type 'exit' to quit.");
            inputString = scanner.nextLine();
            if(inputString.equals("1")) {
                log.info("You have chosen to go to battle!");
                String fightConfig = "simulations: 1\r\n" + //
                                        "\r\n" + //
                                        "partyBuilder: lost-citadel\r\n" + //
                                        "\r\n" + //
                                        "monsters:\r\n" + //
                                        "  - type: storm-giant\r\n" + //
                                        "    name: Storm Giant";
                final FightConfig config = FightConfigLoader.parse(fightConfig);
                final FightResult result = new FightSimulator().run(config);

                if (result.getPartyWins() > result.getMonsterWins()) {
                    log.info("You have won the battle! Plus 50 gold and gain 10 reputation.");
                    // player.setGold(player.getGold() + encounterOutcome.getGoldReward());
                    // player.setReputation(player.getReputation() + encounterOutcome.getReputationReward());
                } else {
                    log.info("You have lost the battle! You pay 10 gold per player and lose 5 reputation.");
                    // player.setGold(player.getGold() - encounterOutcome.getGoldPenalty());
                    // player.setReputation(player.getReputation() - encounterOutcome.getReputationPenalty());
                }
                // Battle logic here
            } else if(inputString.equals("2")) {
                log.info("You have chosen to select a party!");
                // Party selection logic here
            } else if(inputString.equals("3")) {
                log.info("You have chosen to select an enemy!");
                // Enemy selection logic here
            } 
            
        } while (!inputString.equals("exit"));

        scanner.close();
    }
}