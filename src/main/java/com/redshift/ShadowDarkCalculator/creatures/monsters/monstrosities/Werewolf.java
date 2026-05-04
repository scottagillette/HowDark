package com.redshift.ShadowDarkCalculator.creatures.monsters.monstrosities;

import com.redshift.ShadowDarkCalculator.actions.DamageType;
import com.redshift.ShadowDarkCalculator.actions.PerformAllActions;
import com.redshift.ShadowDarkCalculator.actions.weapons.Weapon;
import com.redshift.ShadowDarkCalculator.creatures.CreatureLabel;
import com.redshift.ShadowDarkCalculator.creatures.Stats;
import com.redshift.ShadowDarkCalculator.creatures.monsters.Monster;
import com.redshift.ShadowDarkCalculator.dice.RollModifier;
import lombok.extern.slf4j.Slf4j;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D6;
import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D8;

/**
 * A bipedal, wolf-faced humanoid covered in brown fur.
 * AC 12, HP 20, ATK 2 rend +3 (1d6), MV double near
 * S +3, D +2, C +2, I +0, W +1, Ch +0, AL C, LV 4
 * Impervious. Only damaged by silver or magic sources.
 * Lycanthropy. If 12 or more damage from the same werewolf, contract lycanthropy. // TODO: Not implemented
 */

@Slf4j
public class Werewolf extends Monster {

    public Werewolf(String name) {
        super(
                name,
                4,
                new Stats(16,14,14,10,12,10),
                12,
                D8.roll() + D8.roll() + D8.roll() + D8.roll() + 3,
                new PerformAllActions(
                        new Weapon("Rend", D6, RollModifier.STRENGTH).addSlashing(),
                        new Weapon("Rend", D6, RollModifier.STRENGTH).addSlashing()
                )
        );
        getLabels().add(CreatureLabel.FRONT_LINE);
        getLabels().add(CreatureLabel.HUMANOID);
    }

    @Override
    public void takeDamage(int amount, DamageType damageType) {
        // Take only silvered or magical damage!
        final boolean takeDamage = damageType.isSilvered() || damageType.isMagical();

        if (takeDamage) {
            super.takeDamage(amount, damageType);
        } else {
            log.info("{} takes no damage from non-silvered, non-magical damage!", getName());
        }
    }
}
