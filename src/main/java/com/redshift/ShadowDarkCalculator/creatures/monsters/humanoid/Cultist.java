package com.redshift.ShadowDarkCalculator.creatures.monsters.humanoid;

import com.redshift.ShadowDarkCalculator.actions.PerformOneAction;
import com.redshift.ShadowDarkCalculator.actions.spells.SingleTargetDamageSpell;
import com.redshift.ShadowDarkCalculator.actions.weapons.WeaponBuilder;
import com.redshift.ShadowDarkCalculator.creatures.CreatureLabel;
import com.redshift.ShadowDarkCalculator.creatures.monsters.Monster;
import com.redshift.ShadowDarkCalculator.creatures.Stats;
import com.redshift.ShadowDarkCalculator.dice.MultipleDice;
import com.redshift.ShadowDarkCalculator.dice.RollModifier;
import lombok.extern.slf4j.Slf4j;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.*;

/**
 * A cloaked, wild-eyed zealot chanting the guttural prayers of a dark god.
 * AC 14 (chainmail + shield), HP 9
 * ATK 1 longsword +1 (1d8) or 1 spell +2, MV near
 * S +1, D -1, C +0, I -1, W +2, Ch +0, AL C, LV 2
 * Fearless. Immune to morale checks.
 * Death Touch (WIS Spell). DC 12. 2d4 damage to one creature within close.
 */

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
            super("Death Touch", 12, RollModifier.WISDOM, new MultipleDice(D4, D4), false);
        }

    }
}
