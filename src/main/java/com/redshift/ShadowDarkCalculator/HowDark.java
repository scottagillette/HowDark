package com.redshift.ShadowDarkCalculator;

import com.redshift.ShadowDarkCalculator.creatures.undead.Skeleton;
import com.redshift.ShadowDarkCalculator.encounter.CombatSimulator;

import com.redshift.ShadowDarkCalculator.party.TheCrabCrushersBuilder;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
public class HowDark {

    public static void main(String[] args) {
        int group1Wins = 0;
        int group1WinsWithDeath = 0;

        int group2Wins = 0;
        int group2WinsWithDeath = 0;

        for (int i = 0; i < 1000; i++) {

            log.info("[ Fight: " + (i + 1) + " ]");

            final CombatSimulator simulator = new CombatSimulator(
                    //new TheWolfPackBuilder().build(),
                    new TheCrabCrushersBuilder().build(),

//					List.of(new Ogre("'Ulak The Crusher' Ogre")

//					List.of(new CaveCreeper("Cave Creeper"))
//					List.of(new CaveCreeper("Cave Creeper 1"), new CaveCreeper("Cave Creeper 2"))

//					List.of(new GelatinousCube("'Gelatinous Cube'"))

//					List.of(new HillGiant("Brundo the Crusher")
//					List.of(new HillGiant("Brundo the Crusher"), new HillGiant("Gunda the Smasher"))

//					List.of(new Goblin("Grek"), new Goblin("Pek"), new Goblin("Mek"), new Goblin("Rek")

                    List.of(
                            new Skeleton("Skeleton 1"),
                            new Skeleton("Skeleton 2"),
                            new Skeleton("Skeleton 3"),
                            new Skeleton("Skeleton 4")
                    )

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
            );

            simulator.simulateFight();

            group1Wins = group1Wins + simulator.getGroup1Wins();
            group1WinsWithDeath = group1WinsWithDeath + simulator.getGroup1WinsWithDeath();

            group2Wins = group2Wins + simulator.getGroup2Wins();
            group2WinsWithDeath = group2WinsWithDeath + simulator.getGroup2WinsWithDeath();
        }

        log.info("[ Outcome ]");

        log.info("Group 1: wins=" + group1Wins + ", winsWithDeath=" + group1WinsWithDeath);
        log.info("Group 2: wins=" + group2Wins + ", winsWithDeath=" + group2WinsWithDeath);
    }

}