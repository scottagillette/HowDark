package com.redshift.ShadowDarkCalculator.party;

import com.redshift.ShadowDarkCalculator.actions.PerformOneAction;
import com.redshift.ShadowDarkCalculator.actions.spells.*;
import com.redshift.ShadowDarkCalculator.actions.weapons.WeaponBuilder;
import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.creatures.Label;
import com.redshift.ShadowDarkCalculator.creatures.Player;
import com.redshift.ShadowDarkCalculator.creatures.Stats;
import com.redshift.ShadowDarkCalculator.targets.FocusFireTargetSelector;

import java.util.ArrayList;
import java.util.List;

public class TheCrabCrushersBuilder implements PartyBuilder {

    private final List<Creature> creatures = new ArrayList<>();

    public TheCrabCrushersBuilder() {

        final Creature karn = new Player(
                "Karn Crabcrusher",
                2,
                new Stats(15, 14, 14, 14, 11, 14),
                15,
                7,
                WeaponBuilder.BASTARD_SWORD_1H.build().addAttackRollBonus(4).addDamageRollBonus(3),
                new FocusFireTargetSelector()
        );
        karn.getLabels().add(Label.FRONT_LINE);
        creatures.add(karn);

        final Creature kabsal = new Player(
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
                ),
                new FocusFireTargetSelector()
        );
        kabsal.getLabels().add(Label.HEALER);
        creatures.add(kabsal);

        final Creature alderon = new Player(
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
                ),
                new FocusFireTargetSelector()
        );
        alderon.getLabels().add(Label.BACKLINE);
        alderon.getLabels().add(Label.CASTER);
        creatures.add(alderon);

        final Creature fennick = new Player(
                "Fennick Quickfoot",
                2,
                new Stats(11, 18, 13, 11, 10, 7),
                15,
                10,
                WeaponBuilder.CROSSBOW.build(),
                new FocusFireTargetSelector()
        );
        fennick.getLabels().add(Label.BACKLINE);
        creatures.add(fennick);
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
