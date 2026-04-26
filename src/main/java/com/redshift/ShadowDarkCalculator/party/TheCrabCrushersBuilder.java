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

public class TheCrabCrushersBuilder implements PartyBuilder {

    @Override
    public List<Creature> build() {
        final List<Creature> creatures = new ArrayList<>();

        final Creature karn = new Fighter(
                "Karn Crabcrusher",
                2,
                new Stats(15, 14, 14, 14, 11, 14),
                15,
                7,
                WeaponBuilder.BASTARD_SWORD_1H.build().addAttackRollBonus(4).addDamageRollBonus(3)
        );
        creatures.add(karn);

        final Creature kabsal = new Priest(
                "Kabsal Argent",
                2,
                new Stats(15, 9, 13, 10, 17, 12),
                12,
                9,
                new PerformOneAction(
                        WeaponBuilder.LONGSWORD.build().addAttackRollBonus(1).setPriority(1),
                        new TurnUndead().addSpellCheckBonus(1).setPriority(4),
                        new CureWounds().addSpellCheckBonus(1).setPriority(2),
                        new ShieldOfFaith().addSpellCheckBonus(1)
                )
        );
        creatures.add(kabsal);

        final Creature alderon = new Wizard(
                "Alderon",
                2,
                new Stats(8, 13, 12, 16, 10, 8),
                11,
                5,
                new PerformOneAction(
                        WeaponBuilder.STAFF.build().setPriority(1),
                        new Sleep().addSpellCheckBonus(1).setPriority(3),
                        new MagicMissile().addSpellCheckBonus(1).addAdvantage().setPriority(2),
                        new BurningHands().addSpellCheckBonus(1).addAdvantage().setPriority(4)
                )
        );
        creatures.add(alderon);

        final Creature fennick = new Thief(
                "Fennick Quickfoot",
                2,
                new Stats(11, 18, 13, 11, 10, 7),
                15,
                10,
                WeaponBuilder.CROSSBOW.build()
        );
        creatures.add(fennick);

        return creatures;
    }

}
