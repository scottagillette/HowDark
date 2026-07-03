package com.redshift.ShadowDarkCalculator.api;

import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.encounter.EncounterSimulator;

import java.util.ArrayList;
import java.util.List;

/**
 * Programmatic entry point for running fights from a FightConfig; 
 *
 * Creatures are stateful, so a fresh party and monster group is built for every simulation.
 */

public class FightSimulator {

    public FightResult run(FightConfig config) {
        config.validate();

        int partyWins = 0;
        int partyWinsWithDeath = 0;
        int monsterWins = 0;
        int monsterWinsWithDeath = 0;

        EncounterSimulator lastSimulation = null;

        for (int i = 0; i < config.getSimulations(); i++) {
            final EncounterSimulator simulator = new EncounterSimulator(
                    buildParty(config),
                    buildMonsters(config)
            );

            simulator.setDelay(0);
            simulator.simulateEncounter();

            partyWins += simulator.getGroup1Wins();
            partyWinsWithDeath += simulator.getGroup1WinsWithDeath();
            monsterWins += simulator.getGroup2Wins();
            monsterWinsWithDeath += simulator.getGroup2WinsWithDeath();

            lastSimulation = simulator;
        }

        return new FightResult(
                config.getSimulations(),
                partyWins,
                partyWinsWithDeath,
                monsterWins,
                monsterWinsWithDeath,
                buildCreatureResults(lastSimulation)
        );
    }

    private List<Creature> buildParty(FightConfig config) {
        if (config.usesPrebuiltParty()) {
            return PartyFactory.create(config.getPartyBuilder());
        }

        final List<Creature> party = new ArrayList<>();
        config.getParty().forEach(member -> party.add(PlayerFactory.create(member)));
        return party;
    }

    private List<Creature> buildMonsters(FightConfig config) {
        final List<Creature> monsters = new ArrayList<>();
        config.getMonsters().forEach(monster -> monsters.addAll(MonsterFactory.create(monster)));
        return monsters;
    }

    private List<CreatureResult> buildCreatureResults(EncounterSimulator simulator) {
        final List<CreatureResult> results = new ArrayList<>();

        // Read groups back from the simulator so creatures added mid-fight are included.
        simulator.getGroup1().forEach(creature -> results.add(toResult(creature, CreatureResult.Group.PARTY)));
        simulator.getGroup2().forEach(creature -> results.add(toResult(creature, CreatureResult.Group.MONSTERS)));

        return results;
    }

    private CreatureResult toResult(Creature creature, CreatureResult.Group group) {
        return new CreatureResult(
                creature.getName(),
                group,
                creature.getStatus(),
                creature.getCurrentHitPoints(),
                creature.getMaxHitPoints()
        );
    }

}
