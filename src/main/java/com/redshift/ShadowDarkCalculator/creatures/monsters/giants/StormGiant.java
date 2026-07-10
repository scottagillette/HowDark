package com.redshift.ShadowDarkCalculator.creatures.monsters.giants;

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
import com.redshift.ShadowDarkCalculator.resistance.LightningImmunity;
import com.redshift.ShadowDarkCalculator.targets.multi.AliveTargetSelector;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.redshift.ShadowDarkCalculator.dice.DifficultyClass.HARD;
import static com.redshift.ShadowDarkCalculator.dice.SingleDie.*;
import static java.lang.Math.min;

/**
 * Regal titans with sea-green skin, flowing white hair, and thundering voices. They breathe
 * water as easily as air.
 * AC 15 (mithral chainmail), HP 58, ATK 3 greatsword +10 (2d12) or 1 lightning bolt, MV double near (swim),
 * S +6, D +2, C +4, I +3, W +4, Ch +4, AL L, LV 12
 * Stormblood. Electricity immune.
 * Lightning Bolt. 3/day, 5' wide line extending far from giant. All creatures in line DC 15
 * DEX or 5d10 damage. TODO: DISADV on check if in water.
 */

@Slf4j
public class StormGiant extends Monster {

    public StormGiant(String name) {
        super(
                name,
                12,
                new Stats(22, 14, 18, 16, 18, 18),
                15,
                D8.roll() + D8.roll() + D8.roll() + D8.roll() + D8.roll() +
                        D8.roll() + D8.roll() + D8.roll() + D8.roll() + D8.roll() +
                        D8.roll() + D8.roll() + 3,
                new PerformOneAction(
                        new PerformAllActions(
                                new Weapon("Greatsword", new MultipleDice(D12, D12), RollModifier.STRENGTH).addSlashing().addAttackRollBonus(4),
                                new Weapon("Greatsword", new MultipleDice(D12, D12), RollModifier.STRENGTH).addSlashing().addAttackRollBonus(4),
                                new Weapon("Greatsword", new MultipleDice(D12, D12), RollModifier.STRENGTH).addSlashing().addAttackRollBonus(4)
                        ).setPriority(2),
                        new LightningBolt().setPriority(1)
                )
        );
        getLabels().add(CreatureLabel.FRONT_LINE);
        getLabels().add(CreatureLabel.LAWFUL);
    }

    @Override
    public void takeDamage(int amount, DamageType damageType) {
        final int damage = new LightningImmunity().calculateDamage(this, amount, damageType);
        if (damage != 0) {
            super.takeDamage(damage, damageType);
        }
    }

    /**
     *  * Lightning Bolt. 3/day, 5' wide line extending far from giant. All creatures in line DC 15
     *  * DEX or 5d10 damage. TODO: DISADV on check if in water.
     */

    private static class LightningBolt extends BaseAction {

        private int uses = 3;

        private LightningBolt() {
            super("Lightning Bolt");
        }

        @Override
        public boolean canPerform(Creature actor, List<Creature> enemies, List<Creature> allies) {
            return uses > 0;
        }

        private List<Creature> getTargets(List<Creature> enemies) {
            final int numberOfTargets = min(enemies.size(), D3.roll());
            return new AliveTargetSelector().getTargets(enemies, numberOfTargets);
        }

        @Override
        public boolean isMagicalWeapon() {
            return false;
        }

        @Override
        public void perform(Creature actor, List<Creature> enemies, List<Creature> allies, Encounter encounter) {
            uses--; // Decrement one use of lighting... only 3 total.

            final List<Creature> targets = getTargets(enemies);

            log.info("{} channels {}}", actor.getName(), name);

            targets.forEach(creature -> {
                if (creature.isUnconscious()) {
                    final int damage = new MultipleDice(D10, D10, D10, D10, D10).roll();
                    log.info("{} is unconscious and is electrocuted by {} for {} damage!", creature.getName(), name, damage);
                    creature.takeDamage(damage, new DamageType().addLightning().addMagical());
                } else if (creature.isDead()) {
                    // Ignore damage for dead creatures
                } else {
                    if (creature.getStats().dexteritySave(HARD.getDc())) {
                        log.info("{} makes a DEX save and takes no damage from {}}!", creature.getName(), name);
                    } else {
                        final int damage = new MultipleDice(D10, D10, D10, D10, D10).roll();
                        log.info("{} is electrocuted by {} for {} damage!", creature.getName(), name, damage);
                        creature.takeDamage(damage, new DamageType().addLightning().addMagical());
                    }
                }
            });
        }
    }
}
