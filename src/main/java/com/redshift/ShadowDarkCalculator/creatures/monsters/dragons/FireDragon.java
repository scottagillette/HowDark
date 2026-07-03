package com.redshift.ShadowDarkCalculator.creatures.monsters.dragons;

import com.redshift.ShadowDarkCalculator.actions.BaseAction;
import com.redshift.ShadowDarkCalculator.actions.DamageType;
import com.redshift.ShadowDarkCalculator.actions.PerformAllActions;
import com.redshift.ShadowDarkCalculator.actions.PerformOneAction;
import com.redshift.ShadowDarkCalculator.actions.weapons.Weapon;
import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.creatures.CreatureLabel;
import com.redshift.ShadowDarkCalculator.creatures.monsters.Monster;
import com.redshift.ShadowDarkCalculator.creatures.Stats;
import com.redshift.ShadowDarkCalculator.dice.MultipleDice;
import com.redshift.ShadowDarkCalculator.dice.RollModifier;
import com.redshift.ShadowDarkCalculator.encounter.Encounter;
import com.redshift.ShadowDarkCalculator.resistance.FireImmunity;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.*;

/**
 * Blood-red scales cover the hide of this mighty, volcanic wyrm. Leaping flames glow at the back of its throat.
 * AC 18, HP 80, ATK 4 rend +11 (2d12) or 1 fire breath MV double near (fly)
 * S +6, D +5, C +4, I +4, W +4, Ch +5, AL C, LV 17
 * Fireblood. Fire immune.
 * Fire Breath. Fills a double near-sized cube extending from dragon. DC 15 DEX or 6d10 damage.
 */

@Slf4j
public class FireDragon extends Monster {

    private final FireImmunity fireImmunity = new FireImmunity();

    public FireDragon(String name) {
        super(
                name,
                17,
                new Stats(22,20,18,18,18,20),
                18,
                new MultipleDice(D8, D8, D8, D8, D8, D8, D8, D8, D8, D8, D8, D8, D8, D8, D8, D8, D8).roll() + 4,
                new PerformOneAction(
                        new PerformAllActions(
                                new Weapon("Rending Claws", new MultipleDice(D12, D12), RollModifier.STRENGTH).addMagical().addSlashing().addAttackRollBonus(5),
                                new Weapon("Rending Claws", new MultipleDice(D12, D12), RollModifier.STRENGTH).addMagical().addSlashing().addAttackRollBonus(5),
                                new Weapon("Rending Claws", new MultipleDice(D12, D12), RollModifier.STRENGTH).addMagical().addSlashing().addAttackRollBonus(5),
                                new Weapon("Rending Claws", new MultipleDice(D12, D12), RollModifier.STRENGTH).addMagical().addSlashing().addAttackRollBonus(5)
                        ).setPriority(2),
                        new FireBreath().setPriority(2) // Every third attack is fire breath!
                )
        );
        getLabels().add(CreatureLabel.CHAOTIC);
    }

    @Override
    public void takeDamage(int amount, DamageType damageType) {
        final int damage = fireImmunity.calculateDamage(this, amount, damageType);
        if (damage != 0) {
            super.takeDamage(damage, damageType);
        }
    }

    /**
     * Fire Breath. Fills a double near-sized cube extending from dragon. DC 15 DEX or 6d10 damage.
     */

    private static class FireBreath extends BaseAction {

        private FireBreath() {
            super("Fire Breath");
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
            log.info("{} breaths out fire!", actor.getName());

            enemies.forEach(creature -> {
                if (creature.isUnconscious()) {
                    final int damage = new MultipleDice(D10, D10, D10, D10, D10, D10).roll();
                    log.info("{} is unconscious and is burned by {} for {} damage!", creature.getName(), name, damage);
                    creature.takeDamage(damage, new DamageType().addFire().addMagical());
                } else if (creature.isDead()) {
                    // Ignore damage for dead cretures
                } else {
                    if (creature.getStats().dexteritySave(15)) {
                        log.info("{} makes a DEX save and takes no damage from {}!", creature.getName(), name);
                    } else {
                        final int damage = new MultipleDice(D10, D10, D10, D10, D10, D10).roll();
                        log.info("{} is burned by {} for {} damage!", creature.getName(), name, damage);
                        creature.takeDamage(damage, new DamageType().addFire().addMagical());
                    }
                }
            });
        }
    }
}
