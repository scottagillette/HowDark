package com.redshift.ShadowDarkCalculator.party;

import com.redshift.ShadowDarkCalculator.actions.PerformOneAction;
import com.redshift.ShadowDarkCalculator.actions.spells.*;
import com.redshift.ShadowDarkCalculator.actions.weapons.SimpleWeapon;
import com.redshift.ShadowDarkCalculator.creatures.Character;
import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.creatures.Stats;

import java.util.ArrayList;
import java.util.List;

public class TheWolfPackBuilder implements PartyBuilder {

    private final List<Creature> creatures = new ArrayList<>();

    public TheWolfPackBuilder() {
        creatures.add(new Character(
                "Rogar Windbane",
                1,
                new Stats(15, 9, 12, 9, 6, 10),
                12,
                9,
                SimpleWeapon.LONGSWORD.build()
        ));

        creatures.add(new Character(
                "Elyon Elothy",
                1,
                new Stats(7, 14, 9, 18, 10, 9),
                12,
                1,
                new PerformOneAction(List.of(SimpleWeapon.STAFF.build(), new MagicMissile(), new BurningHands(), new Sleep()))
        ));

        creatures.add(new Character(
                "Tarin",
                1,
                new Stats(9, 18, 10, 9, 10, 10),
                15,
                4,
                new PerformOneAction(List.of(SimpleWeapon.SHORTBOW.build(), SimpleWeapon.DAGGER_DEX.build()))
        ));

        creatures.add(new Character(
                "Grimm",
                1,
                new Stats(10, 10, 14, 8, 10, 8),
                12,
                8,
                new PerformOneAction(List.of(SimpleWeapon.LONGSWORD.build(), new CureWounds(), new ShieldOfFaith(), new TurnUndead()))
        ));
    }

    @Override
    public PartyBuilder add(Creature creature) {
        creatures.add(creature);
        return this;
    }

    @Override
    public List<Creature> build() {
        return creatures;
    }
}
