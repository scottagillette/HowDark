package com.redshift.ShadowDarkCalculator.encounter;

import java.util.ArrayList;
import java.util.List;

import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.creatures.monsters.config.MonsterFactory;

public class RandomEncounter {
    // Create a random encounter for a party of a given size and level sum
    // difficulty is a number 1-3, easy, medium, hard. 
    public static List<Creature> createRandomEncounter(int partySize, int partyLevelSum, int difficulty) {

        // TODO constrain the monsters to be appropriate for the party level sum and size.
        // TODO be able to select monsters by level from monster factory. 
        
        // select random monsters and then throw them away if they don't fit. 
        final List<Creature> monsters = new ArrayList<>();
        int numEnemies = difficulty; 
        for (int i = 0; i < numEnemies; i++) {
            monsters.add(MonsterFactory.createRandomMonster());
        }

        return monsters;
    }
}
