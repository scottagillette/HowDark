package com.redshift.ShadowDarkCalculator.party;

import com.redshift.ShadowDarkCalculator.actions.PerformOneAction;
import com.redshift.ShadowDarkCalculator.actions.spells.*;
import com.redshift.ShadowDarkCalculator.actions.weapons.WeaponBuilder;
import com.redshift.ShadowDarkCalculator.creatures.CreatureLabel;
import com.redshift.ShadowDarkCalculator.creatures.Player;
import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.creatures.Stats;

import java.util.ArrayList;
import java.util.List;

public class TheWolfPackBuilder implements PartyBuilder {

    private final List<Creature> creatures = new ArrayList<>();

    public TheWolfPackBuilder() {
        final Creature rogar = new Player(
                "Rogar Windbane",
                1,
                new Stats(15, 9, 12, 9, 6, 10),
                12,
                9,
                WeaponBuilder.LONGSWORD.build()
        );
        rogar.getLabels().add(CreatureLabel.FRONT_LINE);
        creatures.add(rogar);

        final Creature elyon = new Player(
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
        elyon.getLabels().add(CreatureLabel.BACKLINE);
        elyon.getLabels().add(CreatureLabel.CASTER);
        creatures.add(elyon);

        final Creature tarin = new Player(
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
        tarin.getLabels().add(CreatureLabel.BACKLINE);
        creatures.add(tarin);

        final Creature grimm = new Player(
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
        grimm.getLabels().add(CreatureLabel.HEALER);
        creatures.add(grimm);
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
