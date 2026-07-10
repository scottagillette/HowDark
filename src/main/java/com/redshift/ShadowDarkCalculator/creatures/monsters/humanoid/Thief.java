package com.redshift.ShadowDarkCalculator.creatures.monsters.humanoid;

import com.redshift.ShadowDarkCalculator.actions.PerformOneAction;
import com.redshift.ShadowDarkCalculator.actions.weapons.WeaponBuilder;
import com.redshift.ShadowDarkCalculator.conditions.AdvantageCondition;
import com.redshift.ShadowDarkCalculator.conditions.ExtraDamageDiceCondition;
import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.creatures.CreatureLabel;
import com.redshift.ShadowDarkCalculator.creatures.Stats;
import com.redshift.ShadowDarkCalculator.creatures.monsters.Monster;
import com.redshift.ShadowDarkCalculator.dice.DifficultyClass;
import com.redshift.ShadowDarkCalculator.encounter.Encounter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D8;

/**
 * A cat burglar in a black cloak.
 * AC 13 (leather), HP 13, ATK 1 dagger (close/near) +2 (1d4) or 1 shortsword +0 (1d6), MV near
 * S +0, D +2, C +0, I +0, W +0, Ch +1, AL N, LV 3
 * Stealthy. ADV on DEX checks to sneak and hide.
 * Backstab. Deal x2 damage against surprised creatures.
 */

@Slf4j
public class Thief extends Monster {

    public Thief(String name) {
        super(
                name,
                3,
                new Stats(10, 14, 10, 10,10, 12),
                13,
                D8.roll() + D8.roll() + D8.roll(),
                new PerformOneAction(
                        WeaponBuilder.DAGGER_DEX.build(),
                        WeaponBuilder.SHORT_SWORD.build()
                )
        );
        getLabels().add(CreatureLabel.FRONT_LINE);
        getLabels().add(CreatureLabel.HUMANOID);
        getLabels().add(CreatureLabel.NEUTRAL);
    }

    @Override
    public void takePreCombatTurn(List<Creature> enemies, List<Creature> allies, Encounter encounter) {
        // Thieves can attempt to hide before combat.
        final int dexRoll = Math.max(getStats().dexterityRoll(), getStats().dexterityRoll());

        if (dexRoll >= DifficultyClass.NORMAL.getDc()) {
            log.info("{} has successfully hidden from their enemies in the shadows!", getName());
            addCondition(new AdvantageCondition()); // Advantage when attacking once!
            addCondition(new ExtraDamageDiceCondition(1 + getLevel() / 2));
            takeTurn(enemies, allies, encounter); // Free attack
        } else {
            log.info("{} has failed to hide from their enemies before combat.", getName());
        }

        // Rules:
        // Those with Surprise take a turn before the combats Initiate Roll
        // A creature starting its turn undetected has Advantage on attack rolls

        // Thief:
        // Thievery. ...You are trained in the following tasks and have advantage on
        // any associated checks:
        // • Climbing
        // • Sneaking and hiding
        // ...

        // Thief:
        // Backstab. If you hit a creature who is unaware of your attack,
        // you deal an extra weapon die of damage. Add additional weapon
        // dice of damage equal to half your level (round down).
    }
}
