package com.redshift.ShadowDarkCalculator.creatures.monsters.oozes;

import com.redshift.ShadowDarkCalculator.actions.DamageType;
import com.redshift.ShadowDarkCalculator.actions.PerformAllActions;
import com.redshift.ShadowDarkCalculator.actions.weapons.Weapon;
import com.redshift.ShadowDarkCalculator.conditions.CosticToWeaponsCondition;
import com.redshift.ShadowDarkCalculator.creatures.CreatureLabel;
import com.redshift.ShadowDarkCalculator.creatures.Stats;
import com.redshift.ShadowDarkCalculator.creatures.monsters.Monster;
import com.redshift.ShadowDarkCalculator.dice.RollModifier;
import com.redshift.ShadowDarkCalculator.resistance.PiercingResistance;
import lombok.extern.slf4j.Slf4j;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D6;
import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D8;

/**
 * A trembling mass of filmy bile that slurps toward warmth.
 * AC 12, HP 15, ATK 2 tendril +3 (1d6), MV near (climb)
 * S +3, D +2, C +2, I -3, W +1, Ch -3, AL N, LV 3
 * Rubbery. Half damage from stabbing weapons.
 * Corrosive. Metal or wood that touches the ooze is destroyed on a d6 roll of 1.
 */

@Slf4j
public class IchorOoze extends Monster {

    private final PiercingResistance piercingResistance = new PiercingResistance();

    public IchorOoze(String name) {
        super(
                name,
                3,
                new Stats(16, 14, 14, 4, 12, 4),
                12,
                D8.roll() + D8.roll() + D8.roll() + 2,
                new PerformAllActions(
                        new Weapon("Tendril", D6, RollModifier.STRENGTH),
                        new Weapon("Tendril", D6, RollModifier.STRENGTH)
                )
        );
        getLabels().add(CreatureLabel.FRONT_LINE);
        getLabels().add(CreatureLabel.NEUTRAL);

        addCondition(new CosticToWeaponsCondition()); // Damages weapons!
    }

    @Override
    public void takeDamage(int amount, DamageType damageType) {
        super.takeDamage(
                piercingResistance.calculateDamage(this, amount, damageType),
                damageType
        );
    }

}