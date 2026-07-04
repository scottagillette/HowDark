package com.redshift.ShadowDarkCalculator.fights;

import lombok.Value;

import java.util.List;

/**
 * Aggregate outcome of one or more simulated fights, plus the detailed final state
 * of every creature from the last fight (including creatures raised mid-combat).
 */

@Value
public class FightResult {

    int simulations;

    int partyWins;

    int partyWinsWithDeath;

    int monsterWins;

    int monsterWinsWithDeath;

    List<CreatureResult> lastFightCreatures;

    public double getPartyWinRate() {
        return simulations == 0 ? 0 : (double) partyWins / simulations;
    }

    public boolean isPartyVictorious() {
        return partyWins > monsterWins;
    }

}
