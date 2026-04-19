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

public class DiableriePartyBuilder implements PartyBuilder {

    private final List<Creature> creatures = new ArrayList<>();

    @Override
    public PartyBuilder add(Creature creature) {
        creatures.add(creature);
        return this;
    }

    @Override
    public List<Creature> build() {
        final Creature borlin = new Player(
                "Borlin Little Digger",
                1,
                new Stats(18, 13, 13, 8, 7, 8),
                14,
                6,
                WeaponBuilder.BASTARD_SWORD_1H.build(),
                new FocusFireTargetSelector()
        );
        borlin.getLabels().add(Label.FRONT_LINE);
        creatures.add(borlin);


        final Creature mal = new Player(
                "Malady Blackhand",
                1,
                new Stats(13, 10, 10, 13, 5, 15),
                11,
                4,
                new PerformOneAction(List.of(
                        WeaponBuilder.LONGSWORD.build().addAttackRollBonus(1).setPriority(2),
                        new Withermark().addSpellCheckBonus(1).setPriority(1),
                        new Undeath().addSpellCheckBonus(1).setPriority(10)
                )),
                new FocusFireTargetSelector()
        );
        mal.getLabels().add(Label.BACKLINE);
        mal.getLabels().add(Label.CASTER);
        creatures.add(mal);


        final Creature alaric = new Player(
                "Alaric",
                1,
                new Stats(13, 13, 13, 16, 10, 11),
                11,
                5,
                new PerformOneAction(List.of(
                        WeaponBuilder.STAFF.build().setPriority(1),
                        new MagicMissile().setPriority(2),
                        new Sleep().setPriority(10),
                        new BurningHands().addAdvantage().setPriority(5)
                )),
                new FocusFireTargetSelector()
        );
        alaric.getLabels().add(Label.BACKLINE);
        alaric.getLabels().add(Label.CASTER);
        creatures.add(alaric);

        return creatures;
    }
}
