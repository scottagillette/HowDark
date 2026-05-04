package com.redshift.ShadowDarkCalculator.creatures.monsters.giants;

import com.redshift.ShadowDarkCalculator.actions.DamageType;
import com.redshift.ShadowDarkCalculator.actions.PerformAllActions;
import com.redshift.ShadowDarkCalculator.actions.weapons.Weapon;
import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.creatures.CreatureLabel;
import com.redshift.ShadowDarkCalculator.creatures.Stats;
import com.redshift.ShadowDarkCalculator.creatures.monsters.Monster;
import com.redshift.ShadowDarkCalculator.dice.MultipleDice;
import com.redshift.ShadowDarkCalculator.dice.RollModifier;
import com.redshift.ShadowDarkCalculator.encounter.Encounter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D6;
import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D8;

/**
 * Green, lanky giants with warty skin and territorial rage. Lair in deep forests and swamps.
 * AC 12, HP 24, ATK 2 claw +4 (1d6) and 1 bite +4 (1d10), MV near
 * S +3, D +2, C +2, I -1, W +0, Ch -1, AL C, LV 5
 * Regenerate. Regains 2d6 HP on its turn unless its wounds are cauterized with fire or acid.
 */

@Slf4j
public class Troll extends Monster {

    private boolean burnedSinceLastTurn;

    public Troll(String name) {
        super(
                name,
                5,
                new Stats(16,14,14,8,10,8),
                12,
                D8.roll() + D8.roll() + D8.roll() + D8.roll() + D8.roll() + 2,
                new PerformAllActions(
                        new Weapon("Claw", D6, RollModifier.STRENGTH).addSlashing().addAttackRollBonus(1),
                        new Weapon("Claw", D6, RollModifier.STRENGTH).addSlashing().addAttackRollBonus(1)
                )
        );
        getLabels().add(CreatureLabel.FRONT_LINE);
    }

    @Override
    public void takeDamage(int amount, DamageType damageType) {
        super.takeDamage(amount, damageType);

        if (damageType.isFire() || damageType.isAcid()) {
            burnedSinceLastTurn = true;
        }
    }

    @Override
    public void takeTurn(List<Creature> enemies, List<Creature> allies, Encounter encounter) {
        super.takeTurn(enemies, allies, encounter);

        if (burnedSinceLastTurn) {
            burnedSinceLastTurn = false;
            log.info("{} has taken fire or acid damage and does not regenerate hit points!", this.getName());
        } else {
            if (this.isWounded()) {
                final int hitPointsRegenerated = new MultipleDice(D6, D6).roll();
                log.info("{} regenerates {} hit points!", this.getName(), hitPointsRegenerated);
                this.healDamage(hitPointsRegenerated);
            }
        }
    }
}