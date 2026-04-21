package com.redshift.ShadowDarkCalculator.party;

import com.redshift.ShadowDarkCalculator.actions.weapons.WeaponBuilder;
import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.creatures.Label;
import com.redshift.ShadowDarkCalculator.creatures.Player;
import com.redshift.ShadowDarkCalculator.creatures.Stats;
import com.redshift.ShadowDarkCalculator.targets.FocusFireTargetSelector;

import java.util.ArrayList;
import java.util.List;

/**
 * Test a party of 12 level 0 characters.
 */

public class NewbiesPartyBuilder implements PartyBuilder {

    private final List<Creature> creatures = new ArrayList<>();

    public NewbiesPartyBuilder() {
        final Creature glogor = new Player(
                "Glogor the Half Orc",
                0,
                new Stats(15, 14, 8, 12, 10, 10),
                12,
                1,
                WeaponBuilder.FIST.build(),
                new FocusFireTargetSelector()
        );
        glogor.getLabels().add(Label.FRONT_LINE);
        creatures.add(glogor);

        final Creature muddle = new Player(
                "Muddle the Kobold",
                0,
                new Stats(16, 10, 10, 12, 14, 12),
                10,
                1,
                WeaponBuilder.FIST.build(),
                new FocusFireTargetSelector()
        );
        muddle.getLabels().add(Label.FRONT_LINE);
        creatures.add(muddle);

        final Creature imol = new Player(
                "Imol the Elf",
                0,
                new Stats(10, 10, 12, 12, 14, 12),
                10,
                1,
                WeaponBuilder.FIST.build(),
                new FocusFireTargetSelector()
        );
        imol.getLabels().add(Label.FRONT_LINE);
        creatures.add(imol);

        final Creature da = new Player(
                "Da the Dwarf",
                0,
                new Stats(12, 10, 10, 14, 14, 6),
                10,
                1,
                WeaponBuilder.DAGGER_STR.build(),
                new FocusFireTargetSelector()
        );
        da.getLabels().add(Label.FRONT_LINE);
        creatures.add(da);

        final Creature hod = new Player(
                "Hod the Halfling",
                0,
                new Stats(14, 14, 10, 10, 10, 16),
                12,
                1,
                WeaponBuilder.FIST.build(),
                new FocusFireTargetSelector()
        );
        hod.getLabels().add(Label.FRONT_LINE);
        creatures.add(hod);

        final Creature bakhrud = new Player(
                "Bakhrud the Half-Orc",
                0,
                new Stats(10, 12, 12, 16, 8, 12),
                11,
                1,
                WeaponBuilder.FIST.build(),
                new FocusFireTargetSelector()
        );
        bakhrud.getLabels().add(Label.FRONT_LINE);
        creatures.add(bakhrud);

        final Creature ghazshad = new Player(
                "Ghazshad the Human",
                0,
                new Stats(6, 4, 8, 10, 14, 14),
                10,
                1,
                WeaponBuilder.DAGGER_STR.build(),
                new FocusFireTargetSelector()
        );
        ghazshad.getLabels().add(Label.FRONT_LINE);
        creatures.add(ghazshad);

        final Creature slogyark = new Player(
                "Slogyark the Goblin",
                0,
                new Stats(10, 12, 10, 10, 10, 17),
                10,
                1,
                WeaponBuilder.SHORTBOW.build(),
                new FocusFireTargetSelector()
        );
        slogyark.getLabels().add(Label.FRONT_LINE);
        creatures.add(slogyark);

        final Creature mufas = new Player(
                "Mufas the Human",
                0,
                new Stats(15, 14, 8, 12, 10, 10),
                12,
                1,
                WeaponBuilder.FIST.build(),
                new FocusFireTargetSelector()
        );
        mufas.getLabels().add(Label.FRONT_LINE);
        creatures.add(mufas);

        final Creature krukar = new Player(
                "Krugar the Human",
                0,
                new Stats(16, 10, 10, 12, 14, 12),
                10,
                1,
                WeaponBuilder.FIST.build(),
                new FocusFireTargetSelector()
        );
        krukar.getLabels().add(Label.FRONT_LINE);
        creatures.add(krukar);

        final Creature ufas = new Player(
                "Ufas the Human",
                0,
                new Stats(10, 10, 12, 12, 14, 12),
                10,
                1,
                WeaponBuilder.FIST.build(),
                new FocusFireTargetSelector()
        );
        ufas.getLabels().add(Label.FRONT_LINE);
        creatures.add(ufas);

        final Creature gregor = new Player(
                "Gregor the Dwarf",
                0,
                new Stats(12, 10, 10, 14, 14, 6),
                10,
                1,
                WeaponBuilder.DAGGER_STR.build(),
                new FocusFireTargetSelector()
        );
        gregor.getLabels().add(Label.FRONT_LINE);
        creatures.add(gregor);
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
