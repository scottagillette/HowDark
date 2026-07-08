package com.redshift.ShadowDarkCalculator.party;

import com.redshift.ShadowDarkCalculator.actions.PerformOneAction;
import com.redshift.ShadowDarkCalculator.actions.spells.*;
import com.redshift.ShadowDarkCalculator.actions.weapons.WeaponBuilder;
import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.creatures.Stats;
import com.redshift.ShadowDarkCalculator.creatures.players.*;

import java.util.ArrayList;
import java.util.List;

public class LostCitadelPartyBuilder implements PartyBuilder {

    @Override
    public List<Creature> build() {

        final List<Creature> creatures = new ArrayList<>();

        final Creature borlin = new Paladin(
                "Borlin Little Digger",
                1,
                new Stats(18, 13, 13, 8, 7, 9),
                14,
                6,
                WeaponBuilder.BASTARD_SWORD_1H.build().addMagical()
        );
        creatures.add(borlin);

        final Creature pebble = new Witch(
                "Pebble Weatherton",
                1,
                new Stats(13, 10, 10, 13, 5, 15),
                10,
                1,
                new PerformOneAction(
                        WeaponBuilder.STAFF.build().setPriority(1),
                        new Eyebite().setPriority(3),
                        new Hypnotize().setPriority(6)
                )
        );
        // TODO: Not in party ATM
        //creatures.add(pebble);

        final Creature mal = new Necromancer(
                "Malady Blackhand",
                1,
                new Stats(13, 10, 10, 13, 5, 15),
                12,
                4,
                new PerformOneAction(
                        WeaponBuilder.LONGSWORD.build().addAttackRollBonus(1).setPriority(2),
                        new Withermark().addSpellCheckBonus(1).setPriority(1),
                        new Undeath().addSpellCheckBonus(1).setPriority(10)
                )
        );
        creatures.add(mal);

        final Creature alaric = new Wizard(
                "Alaric",
                1,
                new Stats(13, 13, 13, 16, 10, 11),
                11,
                5,
                new PerformOneAction(
                        WeaponBuilder.STAFF.build().setPriority(1),
                        new MagicMissile().setPriority(2),
                        new Sleep().setPriority(10),
                        new BurningHands().addSpellCheckWithAdvantage().setPriority(5)
                )
        );
        creatures.add(alaric);

        final Creature torvin = new Priest(
                "Brother Torvin",
                1,
                new Stats(18, 10, 10, 7, 18, 10),
                13,
                8,
                new PerformOneAction(
                        WeaponBuilder.LONGSWORD.build().setPriority(1),
                        new HolyWeapon().setPriority(5),
                        //new ProtectionFromEvil().setPriority(10),
                        new CureWounds().setPriority(10),
                        new TurnUndead().setPriority(10)
                )
        );
        creatures.add(torvin);

        // Luck tokens!
//        borlin.giveLuckToken();
//        mal.giveLuckToken();
//        alaric.giveLuckToken();
//        torvin.giveLuckToken();

        return creatures;
    }

}
