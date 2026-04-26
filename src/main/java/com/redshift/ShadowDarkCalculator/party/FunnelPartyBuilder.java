package com.redshift.ShadowDarkCalculator.party;

import com.redshift.ShadowDarkCalculator.actions.weapons.WeaponBuilder;
import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.creatures.CreatureLabel;
import com.redshift.ShadowDarkCalculator.creatures.players.Player;
import com.redshift.ShadowDarkCalculator.creatures.Stats;

import java.util.ArrayList;
import java.util.List;

/**
 * Test a party of 12 level 0 characters.
 */

public class FunnelPartyBuilder implements PartyBuilder {

    @Override
    public List<Creature> build() {

        final List<Creature> creatures = new ArrayList<>();

        final Creature glogor = new Player(
                "Glogor the Half Orc",
                0,
                new Stats(15, 14, 8, 12, 10, 10),
                12,
                1,
                WeaponBuilder.FIST.build()
        );
        glogor.getLabels().add(CreatureLabel.FRONT_LINE);
        creatures.add(glogor);

        final Creature muddle = new Player(
                "Muddle the Kobold",
                0,
                new Stats(16, 10, 10, 12, 14, 12),
                10,
                1,
                WeaponBuilder.FIST.build()
        );
        muddle.getLabels().add(CreatureLabel.FRONT_LINE);
        creatures.add(muddle);

        final Creature imol = new Player(
                "Imol the Elf",
                0,
                new Stats(10, 10, 12, 12, 14, 12),
                10,
                1,
                WeaponBuilder.FIST.build()
        );
        imol.getLabels().add(CreatureLabel.FRONT_LINE);
        creatures.add(imol);

        final Creature da = new Player(
                "Da the Dwarf",
                0,
                new Stats(12, 10, 10, 14, 14, 6),
                10,
                1,
                WeaponBuilder.DAGGER_STR.build()
        );
        da.getLabels().add(CreatureLabel.FRONT_LINE);
        creatures.add(da);

        final Creature hod = new Player(
                "Hod the Halfling",
                0,
                new Stats(14, 14, 10, 10, 10, 16),
                12,
                1,
                WeaponBuilder.FIST.build()
        );
        hod.getLabels().add(CreatureLabel.FRONT_LINE);
        creatures.add(hod);

        final Creature bakhrud = new Player(
                "Bakhrud the Half-Orc",
                0,
                new Stats(10, 12, 12, 16, 8, 12),
                11,
                1,
                WeaponBuilder.FIST.build()
        );
        bakhrud.getLabels().add(CreatureLabel.FRONT_LINE);
        creatures.add(bakhrud);

        final Creature ghazshad = new Player(
                "Ghazshad the Human",
                0,
                new Stats(6, 4, 8, 10, 14, 14),
                10,
                1,
                WeaponBuilder.DAGGER_STR.build()
        );
        ghazshad.getLabels().add(CreatureLabel.FRONT_LINE);
        creatures.add(ghazshad);

        final Creature slogyark = new Player(
                "Slogyark the Goblin",
                0,
                new Stats(10, 12, 10, 10, 10, 17),
                10,
                1,
                WeaponBuilder.SHORTBOW.build()
        );
        slogyark.getLabels().add(CreatureLabel.FRONT_LINE);
        creatures.add(slogyark);

        final Creature mufas = new Player(
                "Mufas the Human",
                0,
                new Stats(15, 14, 8, 12, 10, 10),
                12,
                1,
                WeaponBuilder.FIST.build()
        );
        mufas.getLabels().add(CreatureLabel.FRONT_LINE);
        creatures.add(mufas);

        final Creature krukar = new Player(
                "Krugar the Human",
                0,
                new Stats(16, 10, 10, 12, 14, 12),
                10,
                1,
                WeaponBuilder.FIST.build()
        );
        krukar.getLabels().add(CreatureLabel.FRONT_LINE);
        creatures.add(krukar);

        final Creature ufas = new Player(
                "Ufas the Human",
                0,
                new Stats(10, 10, 12, 12, 14, 12),
                10,
                1,
                WeaponBuilder.FIST.build()
        );
        ufas.getLabels().add(CreatureLabel.FRONT_LINE);
        creatures.add(ufas);

        final Creature gregor = new Player(
                "Gregor the Dwarf",
                0,
                new Stats(12, 10, 10, 14, 14, 6),
                10,
                1,
                WeaponBuilder.DAGGER_STR.build()
        );
        gregor.getLabels().add(CreatureLabel.FRONT_LINE);
        creatures.add(gregor);

        return creatures;
    }

}
