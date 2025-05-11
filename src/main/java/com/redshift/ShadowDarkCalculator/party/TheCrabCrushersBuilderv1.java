package com.redshift.ShadowDarkCalculator.party;

import com.redshift.ShadowDarkCalculator.actions.PerformOneAction;
import com.redshift.ShadowDarkCalculator.actions.spells.*;
import com.redshift.ShadowDarkCalculator.actions.weapons.WeaponBuilder;
import com.redshift.ShadowDarkCalculator.creatures.Label;
import com.redshift.ShadowDarkCalculator.creatures.Player;
import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.creatures.Stats;
import com.redshift.ShadowDarkCalculator.targets.FocusFireTargetSelector;

import java.util.ArrayList;
import java.util.List;

public class TheCrabCrushersBuilderv1 implements PartyBuilder {

    private final List<Creature> creatures = new ArrayList<>();

    public TheCrabCrushersBuilderv1() {

        final Creature karn = new Player(
                "Karn Crabcrusher",
                1,
                new Stats(15, 14, 14, 14, 11, 14),
                15,
                7,
                WeaponBuilder.BASTARD_SWORD_1H.build(3,2),
                new FocusFireTargetSelector()
        );
        karn.getLabels().add(Label.BRUTE);
        creatures.add(karn);

        final Creature kabsal = new Player(
                "Kabsal Argent",
                1,
                new Stats(15, 9, 13, 10, 17, 12),
                12,
                7,
                new PerformOneAction(List.of(
                        WeaponBuilder.LONGSWORD.build(1),
                        new TurnUndead().addSpellCheckBonus(1),
                        new CureWounds().addSpellCheckBonus(1),
                        new ShieldOfFaith().addSpellCheckBonus(1)
                )),
                new FocusFireTargetSelector()
        );
        kabsal.getLabels().add(Label.HEALER);
        creatures.add(kabsal);

        final Creature alderon = new Player(
                "Alderon",
                1,
                new Stats(8, 13, 12, 16, 10, 8),
                11,
                2,
                new PerformOneAction(List.of(
                        WeaponBuilder.STAFF.build(),
                        new Sleep().addSpellCheckBonus(1),
                        new MagicMissile().addSpellCheckBonus(1).addAdvantage(),
                        new BurningHands().addSpellCheckBonus(1).addAdvantage()
                )),
                new FocusFireTargetSelector()
        );
        alderon.getLabels().add(Label.BACKLINE);
        alderon.getLabels().add(Label.CASTER);
        creatures.add(alderon);

        final Creature fennick = new Player(
                "Fennick Quickfoot",
                1,
                new Stats(10, 10, 14, 8, 10, 8),
                14,
                6,
                new PerformOneAction(List.of(WeaponBuilder.CROSSBOW.build())),
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
