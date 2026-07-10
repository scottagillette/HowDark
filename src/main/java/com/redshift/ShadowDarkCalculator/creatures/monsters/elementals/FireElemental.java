package com.redshift.ShadowDarkCalculator.creatures.monsters.elementals;

import com.redshift.ShadowDarkCalculator.actions.BaseAction;
import com.redshift.ShadowDarkCalculator.actions.DamageType;
import com.redshift.ShadowDarkCalculator.actions.PerformAllActions;
import com.redshift.ShadowDarkCalculator.actions.PerformOneAction;
import com.redshift.ShadowDarkCalculator.actions.weapons.Weapon;
import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.creatures.CreatureLabel;
import com.redshift.ShadowDarkCalculator.creatures.Stats;
import com.redshift.ShadowDarkCalculator.creatures.monsters.Monster;
import com.redshift.ShadowDarkCalculator.dice.MultipleDice;
import com.redshift.ShadowDarkCalculator.dice.RollModifier;
import com.redshift.ShadowDarkCalculator.encounter.Encounter;
import com.redshift.ShadowDarkCalculator.resistance.FireImmunity;
import com.redshift.ShadowDarkCalculator.resistance.NonMagicalImmunity;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.redshift.ShadowDarkCalculator.dice.DifficultyClass.HARD;
import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D10;
import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D8;

/**
 * A roaring column of flames.
 * AC 15, HP 30/43, ATK 3 slam +6 (2d10/3d10) or 1 inferno, MV near (fly)
 * S +4, D +3, C +3, I -2, W +1, Ch -2, AL N, LV 6/9
 * Impervious. Only damaged by magical sources. Fire immune.
 * Inferno. All within near DC 15 DEX or 3d8 damage.
 */

@Slf4j
public class FireElemental extends Monster {

    public FireElemental(String name) {
        super(
                name,
                9,
                new Stats(18, 16, 16, 6, 12, 6),
                15,
                D8.roll() + D8.roll() + D8.roll() +
                        D8.roll() + D8.roll() + D8.roll() +
                        D8.roll() + D8.roll() + D8.roll() + 3,
                new PerformOneAction(
                        new PerformAllActions(
                                new Weapon("Slam", new MultipleDice(D10, D10, D10), RollModifier.STRENGTH)
                                        .addAttackRollBonus(2)
                                        .addFire()
                                        .addMagical(),
                                new Weapon("Slam", new MultipleDice(D10, D10, D10), RollModifier.STRENGTH)
                                        .addAttackRollBonus(2)
                                        .addFire()
                                        .addMagical(),
                                new Weapon("Slam", new MultipleDice(D10, D10, D10), RollModifier.STRENGTH)
                                        .addAttackRollBonus(2)
                                        .addFire()
                                        .addMagical()
                        ).setPriority(2),
                        new Inferno().setPriority(1)
                )
        );

        getLabels().add(CreatureLabel.FRONT_LINE);
        getLabels().add(CreatureLabel.NEUTRAL);
    }

    @Override
    public void takeDamage(int amount, DamageType damageType) {
        int damage = new FireImmunity().calculateDamage(this, amount, damageType);
        if (damage > 0) {
            damage = new NonMagicalImmunity().calculateDamage(this, damage, damageType);
            if (damage > 0) {
                super.takeDamage(damage, damageType);
            }
        }
    }

    /**
     * Inferno. All within near DC 15 DEX or 3d8 damage.
     */

    private static class Inferno extends BaseAction {

        public Inferno() {
            super("Inferno");
        }

        @Override
        public boolean canPerform(Creature actor, List<Creature> enemies, List<Creature> allies) {
            return true; // Can always be performed... yikes good luck players!
        }

        @Override
        public boolean isMagicalWeapon() {
            return false;
        }

        @Override
        public void perform(Creature actor, List<Creature> enemies, List<Creature> allies, Encounter encounter) {
            enemies.forEach(creature -> {
                if (creature.isUnconscious()) {
                    final int damage = new MultipleDice(D8, D8, D8).roll();
                    log.info("{} is unconscious and is burned by {} for {} damage!", creature.getName(), name, damage);
                    creature.takeDamage(damage, new DamageType().addFire().addMagical());
                } else if (creature.isDead()) {
                    // Ignore damage for dead creatures
                } else {
                    if (creature.getStats().dexteritySave(HARD.getDc())) {
                        log.info("{} makes a DEX save and takes no damage from Fire Breath!", creature.getName());
                    } else {
                        final int damage = new MultipleDice(D8, D8, D8).roll();
                        log.info("{} is burned by {} for {} damage!", creature.getName(), name, damage);
                        creature.takeDamage(damage, new DamageType().addFire().addMagical());
                    }
                }
            });
        }
    }

}
