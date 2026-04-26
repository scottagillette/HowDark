package com.redshift.ShadowDarkCalculator.creatures.monsters.dragons;

import com.redshift.ShadowDarkCalculator.actions.BaseAction;
import com.redshift.ShadowDarkCalculator.actions.PerformAllActions;
import com.redshift.ShadowDarkCalculator.actions.PerformOneAction;
import com.redshift.ShadowDarkCalculator.actions.weapons.Weapon;
import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.creatures.Monster;
import com.redshift.ShadowDarkCalculator.creatures.Stats;
import com.redshift.ShadowDarkCalculator.dice.MultipleDice;
import com.redshift.ShadowDarkCalculator.dice.RollModifier;
import com.redshift.ShadowDarkCalculator.encounter.Encounter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.*;

/**
 * Blood-red scales cover the hide of this mighty, volcanic wyrm. Leaping flames glow at the back of its throat.
 * AC 18, HP 80, ATK 4 rend +11 (2d12) or 1 fire breath
 * MV double near (fly)
 * S +6, D +5, C +4, I +4, W +4, Ch +5, AL C, LV 17
 * Fireblood. Fire immune.
 * Fire Breath. Fills a double near-sized cube extending from dragon. DC 15 DEX or 6d10 damage.
 */

@Slf4j
public class FireDragon extends Monster {

    public FireDragon(String name) {
        super(
                name,
                17,
                new Stats(22,20,18,18,18,20),
                18,
                new MultipleDice(D8, D8, D8, D8, D8, D8, D8, D8, D8, D8, D8, D8, D8, D8, D8, D8, D8).roll() + 4,
                new PerformOneAction(
                        new PerformAllActions(
                                new Weapon("Rend", new MultipleDice(D12, D12), RollModifier.STRENGTH).addAttackRollBonus(5),
                                new Weapon("Rend", new MultipleDice(D12, D12), RollModifier.STRENGTH).addAttackRollBonus(5),
                                new Weapon("Rend", new MultipleDice(D12, D12), RollModifier.STRENGTH).addAttackRollBonus(5),
                                new Weapon("Rend", new MultipleDice(D12, D12), RollModifier.STRENGTH).addAttackRollBonus(5)
                        ).setPriority(2),
                        new FireBreath().setPriority(1) // Every third attack is fire breath!
                )
        );
    }

    @Override
    public void takeDamage(int amount, boolean silvered, boolean magical, boolean fire, boolean cold) {
        // Fireblood. Fire immune.

        if (fire) {
            log.info("{} resists all fire damage!", this.getName());
        } else {
            super.takeDamage(amount, silvered, magical, fire, cold);
        }
    }


    /**
     * Fire Breath. Fills a double near-sized cube extending from dragon. DC 15 DEX or 6d10 damage.
     */

    public static class FireBreath extends BaseAction {

        public FireBreath() {
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
            enemies.forEach(creature -> {
                if (creature.isUnconscious() || creature.isDead()) {
                    // No damage
                } else {
                    if (creature.getStats().dexteritySave(15)) {
                        log.info("{} makes a DEX save and takes no damage from Fire Breath!", creature.getName());
                    } else {
                        final int damage = new MultipleDice(D6, D6, D6, D6, D6, D6, D6, D6, D6, D6).roll();
                        log.info("{} is burned by Fire Breath for {} damage!", creature.getName(), damage);
                        creature.takeDamage(damage, false, false, true, false);
                    }
                }
            });
        }
    }
}
