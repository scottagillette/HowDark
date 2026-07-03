package com.redshift.ShadowDarkCalculator.api;

import lombok.Value;

/**
 * Final state of one creature after a fight.
 */

@Value
public class CreatureResult {

    String name;

    Group group;

    String status;

    int currentHitPoints;

    int maxHitPoints;

    public enum Group {
        PARTY,
        MONSTERS
    }

}
