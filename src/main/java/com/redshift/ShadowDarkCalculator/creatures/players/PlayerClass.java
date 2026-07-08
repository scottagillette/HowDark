package com.redshift.ShadowDarkCalculator.creatures.players;

import com.redshift.ShadowDarkCalculator.dice.Dice;
import lombok.Getter;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.*;

/**
 * An enumeration of player classes.
 */

@Getter
public enum PlayerClass {
    BARD(D6, false, "Bard"),
    FIGHTER(D8, false, "Fighter"),
    KNIGHT_OF_ST_YDRIS(D6, false, "Knight of St. Ydris"),
    NECROMANCER(D6, true, "Necromancer"),
    PALADIN(D8, false, "Paladin"),
    PRIEST(D6, true, "Priest"),
    RANGER(D8, false, "Ranger"),
    THIEF(D4, false, "Thief"),
    WARLOCK(D6, false, "Warlock"),
    WITCH(D4, true, "Witch"),
    WIZARD(D4, true, "Wizard");

    private final Dice hitDice;
    private final boolean caster;
    private final String className;

    private PlayerClass(Dice hitDice, boolean caster, String className) {
        this.hitDice = hitDice;
        this.caster = caster;
        this.className = className;
    }
}
