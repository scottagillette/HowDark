package com.redshift.ShadowDarkCalculator.creatures.monsters.hag;

import com.redshift.ShadowDarkCalculator.actions.DamageType;
import com.redshift.ShadowDarkCalculator.actions.PerformAllActions;
import com.redshift.ShadowDarkCalculator.actions.PerformOneAction;
import com.redshift.ShadowDarkCalculator.actions.spells.Spell;
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

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D4;
import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D8;

/**
 * Eyes dark as moonless nights, skin made of rotting wood, hair of tangled
 * roots and vines.
 * AC 14, HP 28, ATK 2 claw +4 (1d8) or 1 drink pain, MV near
 * S +3, D +2, C +1, I +1, W +2, Ch +3, AL C, LV 6
 * Drink Pain. Near range. DC 12 CHA to deal 2d4 damage to a creature; regain that many HP.
 * TODO: Shapechange. Instantly change to look like any other humanoid.
 */

@Slf4j
public class WeldHag extends Monster {

    public WeldHag(String name) {
        super(
                name,
                6,
                new Stats(16, 14, 12, 12, 14, 16),
                14,
                D8.roll() + D8.roll() + D8.roll() + D8.roll() + D8.roll() + D8.roll() + 3,
                new PerformOneAction(
                        new PerformAllActions(
                                new Weapon("Claw", D8, RollModifier.STRENGTH).addPiercing().addAttackRollBonus(1),
                                new Weapon("Claw", D8, RollModifier.STRENGTH).addPiercing().addAttackRollBonus(1)
                        ).setPriority(1),
                        new DrinkPain().setPriority(5)
                )
        );
        getLabels().add(CreatureLabel.FRONT_LINE);
        getLabels().add(CreatureLabel.CHAOTIC);
    }

    /**
     * Drink Pain. Near range. DC 12 CHA to deal 2d4 damage to a creature; regain that many HP.
     */

    private static final class DrinkPain extends Spell {

        private DrinkPain() {
            super("Drink Pain", 12, RollModifier.CHARISMA);
        }

        @Override
        public boolean canPerform(Creature actor, List<Creature> enemies, List<Creature> allies) {
            final boolean canPerform = super.canPerform(actor, enemies, allies);
            final boolean hasTarget = actor.getSingleTargetSelector().get(enemies) != null;
            final boolean isWounded = actor.isWounded();
            return canPerform && hasTarget && isWounded;
        }

        @Override
        public List<Creature> getTargets(Creature actor, List<Creature> enemies, List<Creature> allies) {
            return List.of(actor.getSingleTargetSelector().get(enemies));
        }

        @Override
        public void performCriticalSpell(Creature actor, List<Creature> targets, Encounter encounter, int spellCheckRoll) {
            final Creature target = targets.getFirst();

            final int damage = new MultipleDice(D4, D4, D4, D4).roll();
            log.info("{} is critically drained of health that {} is healed from!", target.getName(), actor.getName());
            final DamageType damageType = new DamageType();
            damageType.addMagical();
            target.takeDamage(damage, damageType);
            actor.healDamage(damage);
        }

        @Override
        public void performSpell(Creature actor, List<Creature> targets, Encounter encounter, int spellCheckRoll) {
            final Creature target = targets.getFirst();

            final int damage = new MultipleDice(D4, D4).roll();
            log.info("{} is drained of health that {} is healed from!", target.getName(), actor.getName());
            final DamageType damageType = new DamageType();
            damageType.addMagical();
            target.takeDamage(damage, damageType);
            actor.healDamage(damage);
        }

    }

}
