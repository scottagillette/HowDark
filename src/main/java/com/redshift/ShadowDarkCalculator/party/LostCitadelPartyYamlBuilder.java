package com.redshift.ShadowDarkCalculator.party;

import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.creatures.players.PlayerConfig;
import com.redshift.ShadowDarkCalculator.creatures.players.PlayerFactory;

import java.util.ArrayList;
import java.util.List;

public class LostCitadelPartyYamlBuilder implements PartyBuilder {

    @Override
    public List<Creature> build() {
        final String partyYaml = """
                party:
                  - name: Borlin Little Digger
                    class: Paladin
                    level: 1
                    stats: { str: 18, dex: 13, con: 13, int: 8, wis: 7, cha: 9 }
                    hp: 6
                    ac: 14
                    weapons:
                      - type: bastard-sword-1h
                        magical: true
                        priority: 1
                  - name: Malady Blackhand
                    class: Necromancer
                    level: 1
                    stats: { str: 13, dex: 10, con: 10, int: 13, wis: 5, cha: 15 }
                    hp: 4
                    ac: 12
                    items:
                      - type: Healing-Potion
                        priority: 5
                    weapons:
                      - type: Longsword
                        priority: 1
                    spells:
                      - type: Withermark
                        priority: 2
                      - type: Undeath
                        priority: 10
                  - name: Alaric
                    class: Wizard
                    level: 1
                    stats: { str: 13, dex: 13, con: 13, int: 16, wis: 10, cha: 11 }
                    hp: 5
                    ac: 11
                    weapons:
                      - type: staff
                        priority: 1
                    spells:
                      - type: Magic-Missile
                        priority: 2
                      - type: Sleep
                        priority: 10
                      - type: Burning-Hands
                        advantage: true
                        priority: 5
                  - name: Brother Torvin
                    class: Priest
                    level: 1
                    stats: { str: 18, dex: 10, con: 10, int: 7, wis: 18, cha: 10 }
                    hp: 8
                    ac: 13
                    weapons:
                      - type: Longsword
                        priority: 1
                    spells:
                      - type: holy-weapon
                        priority: 5
                      - type: cure-wounds
                        priority: 10
                      - type: turn-undead
                        priority: 10
                """;

        final List<Creature> players = new ArrayList<>();

        for (PlayerConfig config : PartyConfigLoader.parse(partyYaml).getParty()) {
            players.add(PlayerFactory.create(config));
        }

        return players;
    }
}
