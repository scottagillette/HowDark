package com.redshift.ShadowDarkCalculator.encounter;

import java.util.ArrayList;
import java.util.List;

import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.creatures.monsters.config.MonsterFactory;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RandomEncounter {
    // Create a random encounter for a party of a given size and level sum
    // difficulty is a number 1-3, easy 75%, medium 50%, hard 25%. 
    public static List<Creature> createRandomEncounter(int partySize, int partyLevelSum, int difficulty) {

        int fightValue = 0;
        int partyValue = (partyLevelSum/partySize) * partySize;
        float difficultyMultiplier = 1;
        switch (difficulty) {
            // 4 level 1s, get 7, 10, 12 for sum of level. 
            // gives 75%, 50% and 25% chance of winning.
            case 1:
                difficultyMultiplier = 1.8f;  //7
                break;
            case 2:
                difficultyMultiplier = 2.5f; //10
                break;
            case 3:
                difficultyMultiplier = 3.0f; //12
                break;
            default:
                throw new IllegalArgumentException("Difficulty must be between 1 and 3");
        }
        int targetValue = (int)(difficultyMultiplier * partyValue);

        log.info("Creating random encounter for party size {} and level sum {} with difficulty {} (target value: {})", partySize, partyLevelSum, difficulty, targetValue);
        // select random monsters and then throw them away if they don't fit. 
        final List<Creature> monsters = new ArrayList<>();
        do {
            // this burns CPU, but it's simple. 
            Creature creature = MonsterFactory.createRandomMonster();  
            if (fightValue + creature.getLevel() > targetValue) {
                continue; // skip this monster if it would exceed the target value
            } else {
                log.info("Selected monster: {}", creature.getName());
                fightValue += creature.getLevel();
                monsters.add(creature);
            }
        } while (fightValue < targetValue);

        return monsters;
    }
}
