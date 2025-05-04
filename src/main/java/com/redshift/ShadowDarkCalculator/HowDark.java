package com.redshift.ShadowDarkCalculator;

import com.redshift.ShadowDarkCalculator.creatures.*;
import com.redshift.ShadowDarkCalculator.encounter.CombatSimulator;

import com.redshift.ShadowDarkCalculator.party.MonsterPartyBuilder;
import com.redshift.ShadowDarkCalculator.party.TheCrabCrushersBuilder;

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

//					new MonsterPartyBuilder()
//							.add(new Ogre("'Ulak The Crusher' Ogre")).build()

//					new MonsterPartyBuilder()
//							.add(new CaveCreeper("Cave Creeper")).build()

//					new MonsterPartyBuilder()
//							.add(new CaveCreeper("Cave Creeper 1"))
//							.add(new CaveCreeper("Cave Creeper 2")).build()

//					new MonsterPartyBuilder()
//							.add(new GelatinousCube("'Gelatinous Cube'")).build()

					new MonsterPartyBuilder()
							.add(new HillGiant("Brundo the Crusher"))
							.add(new HillGiant("Gunda the Smasher")).build()

//					new MonsterPartyBuilder()
//							.add(new HillGiant("Brundo the Crusher")).build()

//					new MonsterPartyBuilder()
//							.add(new Goblin("Grek"))
//							.add(new Goblin("Pek"))
//							.add(new Goblin("Mek"))
//							.add(new Goblin("Rek")).build()

//					new MonsterPartyBuilder()
//							.add(new Skeleton("Skeleton 1"))
//							.add(new Skeleton("Skeleton 2"))
//							.add(new Skeleton("Skeleton 3"))
//							.add(new Skeleton("Skeleton 4")).build()

//					new MonsterPartyBuilder()
//							.add(new Bugbear("Bugbear 1"))
//							.add(new Bugbear("Bugbear 2")).build()
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