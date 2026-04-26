package com.redshift.ShadowDarkCalculator.party;

import com.redshift.ShadowDarkCalculator.actions.PerformOneAction;
import com.redshift.ShadowDarkCalculator.actions.spells.*;
import com.redshift.ShadowDarkCalculator.actions.weapons.WeaponBuilder;
import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.creatures.Stats;
import com.redshift.ShadowDarkCalculator.creatures.players.Fighter;
import com.redshift.ShadowDarkCalculator.creatures.players.Priest;
import com.redshift.ShadowDarkCalculator.creatures.players.Thief;
import com.redshift.ShadowDarkCalculator.creatures.players.Wizard;

import java.util.ArrayList;
import java.util.List;

public class TheWolfPackBuilder implements PartyBuilder {

    @Override
    public List<Creature> build() {

        final List<Creature> creatures = new ArrayList<>();

        final Creature rogar = new Fighter(
                "Rogar Windbane",
                1,
                new Stats(15, 9, 12, 9, 6, 10),
                12,
                9,
                WeaponBuilder.LONGSWORD.build()
        );
        creatures.add(rogar);

        final Creature elyon = new Wizard(
                "Elyon Elothy",
                1,
                new Stats(7, 14, 9, 18, 10, 9),
                12,
                1,
                new PerformOneAction(
                        WeaponBuilder.STAFF.build().setPriority(1),
                        new MagicMissile().setPriority(2),
                        new BurningHands().setPriority(4),
                        new Sleep().setPriority(8)
                )
        );
        creatures.add(elyon);

        final Creature tarin = new Thief(
                "Tarin",
                1,
                new Stats(9, 18, 10, 9, 10, 10),
                15,
                4,
                new PerformOneAction(
                        WeaponBuilder.SHORTBOW.build(),
                        WeaponBuilder.DAGGER_DEX.build()
                )
        );
        creatures.add(tarin);

        final Creature grimm = new Priest(
                "Grimm",
                1,
                new Stats(10, 10, 14, 8, 10, 8),
                12,
                8,
                new PerformOneAction(
                        WeaponBuilder.LONGSWORD.build().setPriority(1),
                        new CureWounds().setPriority(2),
                        new ShieldOfFaith().setPriority(1),
                        new TurnUndead().setPriority(4)
                )
        );
        creatures.add(grimm);

        return creatures;
    }

}
