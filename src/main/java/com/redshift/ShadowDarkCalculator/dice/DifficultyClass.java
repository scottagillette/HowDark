package com.redshift.ShadowDarkCalculator.dice;

import lombok.Getter;

/**
 * An enumeration of standard difficulty classes.
 */

@Getter
public enum DifficultyClass {

    EASY(9),
    NORMAL(12),
    HARD(15),
    EXTREME(18);

    private final int dc;

    private DifficultyClass(int dc) {
        this.dc = dc;
    }

}
