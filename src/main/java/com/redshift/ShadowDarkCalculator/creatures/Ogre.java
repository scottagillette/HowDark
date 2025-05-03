package com.redshift.ShadowDarkCalculator.creatures;

import com.redshift.ShadowDarkCalculator.actions.PerformAllAction;
import com.redshift.ShadowDarkCalculator.actions.weapons.MonsterWeapons;
import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D8;

public class Ogre extends BaseCreature {

    public Ogre(String name) {
        super(
                name,
                6,
                new Stats(18,9,17,7,7,7),
                9,
                D8.roll() + D8.roll() + D8.roll() + D8.roll() + D8.roll() + D8.roll() + 3,
                new PerformAllAction(
                        MonsterWeapons.GREAT_CLUB_D6.build(2),
                        MonsterWeapons.GREAT_CLUB_D6.build(2)
                )
        );
    }

}
