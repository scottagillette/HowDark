package com.redshift.ShadowDarkCalculator.actions.weapons;


import com.redshift.ShadowDarkCalculator.dice.Dice;
import com.redshift.ShadowDarkCalculator.dice.MultipleDice;
import com.redshift.ShadowDarkCalculator.dice.RollModifier;

import java.util.List;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.*;

/**
 * Shared weapons across more than one monster.
 */

public enum MonsterWeapons {

    BOULDER("Boulder", new MultipleDice(List.of(D10, D10)), RollModifier.STRENGTH),
    GREAT_CLUB_D6("Great Club", new MultipleDice(List.of(D6, D6)), RollModifier.STRENGTH),
    GREAT_CLUB_D8("Great Club", new MultipleDice(List.of(D8, D8)), RollModifier.STRENGTH);

    private final String name;
    private final Dice dice;
    private final RollModifier rollModifier;

    private MonsterWeapons(String name, Dice damageDice, RollModifier rollModifier) {
        this.name = name;
        this.dice = damageDice;
        this.rollModifier = rollModifier;
    }

    public CustomWeapon build() {
        return new CustomWeapon(name, dice, rollModifier, 0, 0);
    }

    public CustomWeapon build(int attackModifier) {
        return new CustomWeapon(name, dice, rollModifier, attackModifier);
    }

    public CustomWeapon build(int attackModifier, int damageModifier) {
        return new CustomWeapon(name, dice, rollModifier, attackModifier, damageModifier);
    }
}
