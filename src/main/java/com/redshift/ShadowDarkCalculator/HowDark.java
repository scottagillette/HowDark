package com.redshift.ShadowDarkCalculator;

import com.redshift.ShadowDarkCalculator.creatures.monsters.monstrosities.Werewolf;
import com.redshift.ShadowDarkCalculator.encounter.EncounterSimulator;
import com.redshift.ShadowDarkCalculator.party.DiableriePartyBuilder;

import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * Application to run X number of combat simulations between two groups of creatures.
 */

@Slf4j
public class HowDark {

    public static void main(String[] args) {
        int group1Wins = 0;
        int group1WinsWithDeath = 0;

        int group2Wins = 0;
        int group2WinsWithDeath = 0;

        for (int i = 0; i < 1000; i++) {

            log.info("[ Fight: {} ]", i + 1);

            final EncounterSimulator simulator = new EncounterSimulator(
//                    new TheWolfPackBuilder().build(),
//                    new TheCrabCrushersBuilderv1().build(),
                    new DiableriePartyBuilder().build(),
                    List.of(new Werewolf("Dark Werewolf"))
//                    List.of(new Knight("Knight 1"), new Knight("Knight 2"))
//                    List.of(new Reaver("Reaver 1"))
//                    List.of(new GiantRat("Giant Rat 1"), new GiantRat("Giant Rat 2"), new GiantRat("Giant Rat 3"), new GiantRat("Giant Rat 4"))
//                    List.of(new GiantSnake("Snake of Doom"))
//                    List.of(new GiantSpider("Giant Black Widow Spider"))
//                    List.of(new Troll("Green Troll"))
//                    List.of(new Orc("Orc 1"), new Orc("Orc 2"), new Orc("Orc 3"), new Orc("Orc 4"))
//                    List.of(new Bandit("Bandit 1"), new Bandit("Bandit 2"), new Bandit("Bandit 3"), new Bandit("Bandit 4"))
//                    List.of(new Hexling("Hexling 1"), new Hexling("Hexling 2"), new Hexling("Hexling 3"), new Hexling("Hexling 4"))
//                    List.of(new VoidSpider("Void Spider of the Deep"))
//                    List.of(new TheWillowMan("The Willow Man"))
//                    List.of(new MarrowFiend("Death Fiend"))
//                    List.of(new Dralech("Death Bringer"))
//                    new NewbiesPartyBuilder().build(),
//                    List.of(
//                            new Bittermold("Bittermold 1"),
//                            new Bittermold("Bittermold 2"),
//                            new Bittermold("Bittermold 3"),
//                            new Bittermold("Bittermold 4"),
//                            new Bittermold("Bittermold 5"),
//                            new Bittermold("Bittermold 6"),
//                            new Bittermold("Bittermold 7")
//                    )
//                    List.of(new Mimic("Mimic"))
//                    List.of(new Ogre("'Ulak The Crusher' Ogre"))
//                    List.of(new CaveCreeper("Cave Creeper"))
//                    List.of(new CaveCreeper("Cave Creeper 1"), new CaveCreeper("Cave Creeper 2"))
//                    List.of(new GelatinousCube("Gelatinous Cube"))
//                    List.of(new HillGiant("Brundo the Crusher")
//                    List.of(new HillGiant("Brundo the Crusher"), new HillGiant("Gunda the Smasher"))
//                    List.of(new Goblin("Grek"), new Goblin("Pek"), new Goblin("Mek"), new Goblin("Rek"))
//                    List.of(new GoblinBoss("Boss Grek"), new Goblin("Pek"), new Goblin("Mek"), new GoblinShaman("Rek the shaman"))
//                    List.of(new Ettercap("Ettercap 1"), new Ettercap("Ettercap 2"))
//                    List.of(
//                            new Skeleton("Skeleton 1"),
//                            new Skeleton("Skeleton 2"),
//                            new Zombie("Skeleton 3")
//                    )
//                    List.of(
//                            new Zombie("Zombie 1"),
//                            new Zombie("Zombie 2"),
//                            new Zombie("Zombie 3"),
//                            new Zombie("Zombie 4")
//                    )
//                    List.of(
//                            new Ghoul("Blood drenched Ghoul"),
//                            new Ghoul("Single limb Ghoul"),
//                            new Ghoul("Foul smell Ghoul"),
//                            new Ghoul("Faceless Ghoul")
//                    )
//                    List.of(new Wight("Armored Wight"))
//                    List.of(new Ghast("Ghast"))
//                    List.of(new Shadow("Shadow 1"), new Shadow("Shadow 2"))
//                    List.of(new Bugbear("Bugbear 1"), new Bugbear("Bugbear 2"))
//                    List.of(new Wraith("Wraith"))
//                    List.of(new Mummy("Mummy of Death"))
//                    List.of(new VampireSpawn("Vampire Spawn"))
//                    List.of(new OwlBear("Rabid Owlbear"))
//                    List.of(new Ankheg("Ankheg of Death"))
//                    List.of(new FireDragon("Red Fire Dragon of death"))
//                    List.of(new TarBat("Bat 1"), new TarBat("Bat 2"), new TarBat("Bat 3"), new TarBat("Bat 4"))
//                    List.of(new SwampDragon("Black dragon of death"))
//                    List.of(new Cultist("Cultist 1"), new Cultist("Cultist 2"), new Cultist("Cultist 3"), new Cultist("Cultist 4"), new Cultist("Cultist 5"))
//                    List.of(new Apprentice("Apprentice 1"), new Apprentice("Apprentice 2"), new Apprentice("Apprentice 3"), new Apprentice("Apprentice 4"))
                    );

            simulator.setDelay(0);
            simulator.simulateEncounter();

            group1Wins = group1Wins + simulator.getGroup1Wins();
            group1WinsWithDeath = group1WinsWithDeath + simulator.getGroup1WinsWithDeath();

            group2Wins = group2Wins + simulator.getGroup2Wins();
            group2WinsWithDeath = group2WinsWithDeath + simulator.getGroup2WinsWithDeath();
        }

        log.info("[ Outcome ]");

        log.info("Players:  wins={}, winsWithDeath={}", group1Wins, group1WinsWithDeath);
        log.info("Monsters: wins={}, winsWithDeath={}", group2Wins, group2WinsWithDeath);
    }

}