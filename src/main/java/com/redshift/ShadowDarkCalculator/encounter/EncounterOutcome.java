package com.redshift.ShadowDarkCalculator.encounter;

import com.redshift.ShadowDarkCalculator.creatures.Creature;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * An encounter outcome retuned by an Encounter.
 */

@Getter
@Setter
public class EncounterOutcome {

    private boolean group1Wins;
    private int group1Deaths;

    private boolean group2Wins;
    private int group2Deaths;

    private List<Creature> group1Creatures;
    private List<Creature> group2Creatures;

}
