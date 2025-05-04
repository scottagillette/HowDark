package com.redshift.ShadowDarkCalculator;

import com.redshift.ShadowDarkCalculator.creatures.*;
import com.redshift.ShadowDarkCalculator.encounter.CombatSimulator;

import com.redshift.ShadowDarkCalculator.party.TheCrabCrushersBuilder;

import java.util.List;

public class HowDark {

    public static void main(String[] args) {
        int group1Wins = 0;
        int group1WinsWithDeath = 0;

        int group2Wins = 0;
        int group2WinsWithDeath = 0;

        for (int i = 0; i < 1000; i++) {

            System.out.println("[ Fight: " + (i + 1) + " ]");

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

//                    List.of(
//                            new Skeleton("Skeleton 1"),
//                            new Skeleton("Skeleton 2"),
//                            new Skeleton("Skeleton 3"),
//                            new Skeleton("Skeleton 4")
//                    )

                    List.of(new Bugbear("Bugbear 1"), new Bugbear("Bugbear 2"))
			);

            simulator.simulateFight();

            group1Wins = group1Wins + simulator.getGroup1Wins();
            group1WinsWithDeath = group1WinsWithDeath + simulator.getGroup1WinsWithDeath();

            group2Wins = group2Wins + simulator.getGroup2Wins();
            group2WinsWithDeath = group2WinsWithDeath + simulator.getGroup2WinsWithDeath();
        }

        System.out.println("Group 1: wins=" + group1Wins + ", winsWithDeath=" + group1WinsWithDeath);
        System.out.println("Group 2: wins=" + group2Wins + ", winsWithDeath=" + group2WinsWithDeath);
    }

}