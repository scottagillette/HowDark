package com.redshift.ShadowDarkCalculator.dice;

import lombok.Getter;

@Getter
public enum DifficultyLevel {

    EASY(9),
    NORMAL(12),
    HARD(15),
    EXTREME(18);

    private final int difficultyClass;

    private DifficultyLevel(int difficultyClass) {
        this.difficultyClass = difficultyClass;
    }

}
