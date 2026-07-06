package com.redshift.ShadowDarkCalculator.creatures.players;

import com.redshift.ShadowDarkCalculator.dice.Dice;
import lombok.Getter;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.*;

/**
 * An enumeration of player classes.
 */

@Getter
public enum PlayerClass {
    BARD(D6, false),
    FIGHTER(D8, false),
    //KNIGHT_OF_ST_YDRIS(D6),
    //NECROMANCER(D6),
    //PALADIN(D8),
    PRIEST(D6, true),
    //RANGER(D8),
    THIEF(D4, false),
    //WARLOCK(D6),
    //WITCH(D4),
    WIZARD(D4, true);

    private Dice hitDice;
    private boolean caster;

    private PlayerClass(Dice hitDice, boolean caster) {
        this.hitDice = hitDice;
        this.caster = caster;
    }
}
