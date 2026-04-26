package com.redshift.ShadowDarkCalculator.creatures.monsters.humanoid;

import com.redshift.ShadowDarkCalculator.actions.PerformOneAction;
import com.redshift.ShadowDarkCalculator.actions.spells.SingleTargetDamageSpell;
import com.redshift.ShadowDarkCalculator.actions.weapons.WeaponBuilder;
import com.redshift.ShadowDarkCalculator.creatures.CreatureLabel;
import com.redshift.ShadowDarkCalculator.creatures.monsters.Monster;
import com.redshift.ShadowDarkCalculator.creatures.Stats;
import com.redshift.ShadowDarkCalculator.dice.RollModifier;
import lombok.extern.slf4j.Slf4j;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.*;

@Slf4j
public class Cultist extends Monster {

    public Cultist(String name) {
        super(
                name,
                2,
                new Stats(12, 8, 10, 8,14, 10),
                14,
                D8.roll() + D8.roll(),
                new PerformOneAction(
                        WeaponBuilder.LONGSWORD.build().setPriority(1),
                        new DeathTouch().setPriority(2)
                )
        );
        getLabels().add(CreatureLabel.FRONT_LINE);
        getLabels().add(CreatureLabel.HUMANOID);
    }

    private static class DeathTouch extends SingleTargetDamageSpell {

        private DeathTouch() {
            super("Death Touch", 12, RollModifier.WISDOM, D4, false);
        }

    }
}
