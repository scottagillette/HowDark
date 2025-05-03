package com.redshift.ShadowDarkCalculator.creatures;

import com.redshift.ShadowDarkCalculator.actions.Action;
import com.redshift.ShadowDarkCalculator.actions.PerformAllAction;
import com.redshift.ShadowDarkCalculator.actions.PerformOneAction;
import com.redshift.ShadowDarkCalculator.actions.weapons.MonsterWeapons;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D8;

public class HillGiant extends BaseCreature {

    public HillGiant(String name) {
        super(
                name,
                7,
                new Stats(18,10,17,7,7,7),
                11,
                D8.roll() + D8.roll() + D8.roll() + D8.roll() + D8.roll() + D8.roll() + D8.roll() + 3,
                new PerformOneAction(
                        new PerformOneAction(
                                MonsterWeapons.GREAT_CLUB_D8.build(2),
                                MonsterWeapons.GREAT_CLUB_D8.build(2)
                        ),
                        MonsterWeapons.BOULDER.build()
                )
        );
    }

}
