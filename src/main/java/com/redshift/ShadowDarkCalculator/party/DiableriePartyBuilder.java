package com.redshift.ShadowDarkCalculator.party;

import com.redshift.ShadowDarkCalculator.actions.PerformOneAction;
import com.redshift.ShadowDarkCalculator.actions.spells.*;
import com.redshift.ShadowDarkCalculator.actions.weapons.WeaponBuilder;
import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.creatures.CreatureLabel;
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

        final Creature clank = new Player(
                "Clank Smashfist",
                1,
                new Stats(16, 14, 12, 8, 11, 3),
                15,
                8,
                WeaponBuilder.GREATAXE_1H.build().addAttackRollBonus(2).addDamageRollBonus(2),
                new FocusFireTargetSelector()
        );
        clank.getLabels().add(CreatureLabel.FRONT_LINE);
        creatures.add(clank);


        final Creature borlin = new Player(
                "Borlin Little Digger",
                1,
                new Stats(18, 13, 13, 8, 7, 8),
                14,
                6,
                WeaponBuilder.BASTARD_SWORD_1H.build().addMagical(),
                new FocusFireTargetSelector()
        );
        borlin.getLabels().add(CreatureLabel.FRONT_LINE);
        borlin.getLabels().add(CreatureLabel.HAS_MAGIC_WEAPON);
        creatures.add(borlin);


        final Creature mal = new Player(
                "Malady Blackhand",
                1,
                new Stats(13, 10, 10, 13, 5, 15),
                11,
                4,
                new PerformOneAction(
                        WeaponBuilder.LONGSWORD.build().addAttackRollBonus(1).setPriority(2),
                        new Withermark().addSpellCheckBonus(1).setPriority(1),
                        new Undeath().addSpellCheckBonus(1).setPriority(10)
                ),
                new FocusFireTargetSelector()
        );
        mal.getLabels().add(CreatureLabel.BACKLINE);
        mal.getLabels().add(CreatureLabel.CASTER);
        creatures.add(mal);


        final Creature alaric = new Player(
                "Alaric",
                1,
                new Stats(13, 13, 13, 16, 10, 11),
                11,
                5,
                new PerformOneAction(
                        WeaponBuilder.STAFF.build().setPriority(1),
                        new MagicMissile().setPriority(2),
                        new Sleep().setPriority(10),
                        new BurningHands().addAdvantage().setPriority(5)
                ),
                new FocusFireTargetSelector()
        );
        alaric.getLabels().add(CreatureLabel.BACKLINE);
        alaric.getLabels().add(CreatureLabel.CASTER);
        creatures.add(alaric);


        final Creature torvin = new Player(
                "Brother Torvin",
                1,
                new Stats(18, 10, 10, 7, 18, 10),
                13,
                8,
                new PerformOneAction(
                        WeaponBuilder.LONGSWORD.build().setPriority(1),
                        new HolyWeapon().setPriority(3),
                        new CureWounds().setPriority(10),
                        new TurnUndead().setPriority(10)
                ),
                new FocusFireTargetSelector()
        );
        torvin.getLabels().add(CreatureLabel.BACKLINE);
        torvin.getLabels().add(CreatureLabel.CASTER);
        creatures.add(torvin);


        return creatures;
    }

}
