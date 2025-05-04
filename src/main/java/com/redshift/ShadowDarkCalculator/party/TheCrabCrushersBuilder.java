package com.redshift.ShadowDarkCalculator.party;

import com.redshift.ShadowDarkCalculator.actions.PerformOneAction;
import com.redshift.ShadowDarkCalculator.actions.spells.*;
import com.redshift.ShadowDarkCalculator.actions.weapons.SimpleWeapon;
import com.redshift.ShadowDarkCalculator.creatures.Character;
import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.creatures.Stats;
import com.redshift.ShadowDarkCalculator.targets.FocusFireTargetSelector;

import java.util.ArrayList;
import java.util.List;

public class TheCrabCrushersBuilder implements PartyBuilder {

    private final List<Creature> creatures = new ArrayList<>();

    public TheCrabCrushersBuilder() {
        creatures.add(new Character(
                "Karn Crabcrusher",
                1,
                new Stats(15, 14, 14, 14, 11, 14),
                15,
                7,
                SimpleWeapon.BASTARD_SWORD_1H.build(3,2),
                new FocusFireTargetSelector()
        ));

        creatures.add(new Character(
                "Kabsal Argent",
                1,
                new Stats(15, 9, 13, 10, 17, 12),
                12,
                7,
                new PerformOneAction(List.of(
                        SimpleWeapon.LONGSWORD.build(1),
                        new TurnUndead().addBonus(1),
                        new CureWounds().addBonus(1),
                        new ShieldOfFaith().addBonus(1)
                )),
                new FocusFireTargetSelector()
        ));

        creatures.add(new Character(
                "Alderon",
                1,
                new Stats(8, 13, 12, 16, 10, 8),
                11,
                2,
                new PerformOneAction(List.of(
                        SimpleWeapon.STAFF.build(),
                        new Sleep().addBonus(1),
                        new MagicMissile().addBonus(1).addAdvantage(),
                        new BurningHands().addBonus(1).addAdvantage()
                )),
                new FocusFireTargetSelector()
        ));

        creatures.add(new Character(
                "Fennick Quickfoot",
                1,
                new Stats(10, 10, 14, 8, 10, 8),
                14,
                6,
                new PerformOneAction(List.of(SimpleWeapon.CROSSBOW.build())),
                new FocusFireTargetSelector()
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
