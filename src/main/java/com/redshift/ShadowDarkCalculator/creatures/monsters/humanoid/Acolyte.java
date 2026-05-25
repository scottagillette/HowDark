package com.redshift.ShadowDarkCalculator.creatures.monsters.humanoid;

import com.redshift.ShadowDarkCalculator.actions.PerformOneAction;
import com.redshift.ShadowDarkCalculator.actions.spells.Spell;
import com.redshift.ShadowDarkCalculator.actions.weapons.WeaponBuilder;
import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.creatures.CreatureLabel;
import com.redshift.ShadowDarkCalculator.creatures.monsters.Monster;
import com.redshift.ShadowDarkCalculator.creatures.Stats;
import com.redshift.ShadowDarkCalculator.dice.RollModifier;
import com.redshift.ShadowDarkCalculator.encounter.Encounter;
import com.redshift.ShadowDarkCalculator.targets.single.HealTargetSelector;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D4;
import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D8;

/**
 * A religious trainee who knows basic rites and rituals.
 * AC 12, HP 4, ATK 1 mace +1 (1d6) or 1 spell +2, MV near
 * S +1, D -1, C +0, I -1, W +2, Ch +0, AL L, LV 1
 * Healing Touch (WIS Spell). DC 11. Heal one creature within close for 1d4 HP.
 */

@Slf4j
public class Acolyte extends Monster {

    public Acolyte(String name) {
        super(
                name,
                1,
                new Stats(12, 8, 10, 8,14, 10),
                12,
                D8.roll(),
                new PerformOneAction(
                        WeaponBuilder.MACE.build().setPriority(1),
                        new HealingTouch().setPriority(4)
                )
        );
        getLabels().add(CreatureLabel.FRONT_LINE);
        getLabels().add(CreatureLabel.HUMANOID);
        getLabels().add(CreatureLabel.LAWFUL);
    }

    /**
     * Healing Touch (WIS Spell). DC 11. Heal one creature within close for 1d4 HP.
     */

    private static class HealingTouch extends Spell {

        public HealingTouch() {
            super("Healing Touch", 11, RollModifier.WISDOM);
        }

        @Override
        public boolean canPerform(Creature actor, List<Creature> enemies, List<Creature> allies) {
            final boolean canPerform = super.canPerform(actor, enemies, allies);
            final boolean hasTarget = new HealTargetSelector().get(allies) != null;
            return canPerform && hasTarget;
        }

        @Override
        public List<Creature> getTargets(Creature actor, List<Creature> enemies, List<Creature> allies) {
            return List.of(new HealTargetSelector().get(allies));
        }

        @Override
        public void performCriticalSpell(Creature actor, List<Creature> targets, Encounter encounter, int spellCheckRoll) {
            final Creature target = targets.getFirst();
            int hitPoints = D4.roll() + D4.roll();
            log.info("{} critically heals on {} for {} with a {}", actor.getName(), target.getName(), hitPoints, name);
            target.healDamage(hitPoints);
        }

        @Override
        public void performSpell(Creature actor, List<Creature> targets, Encounter encounter, int spellCheckRoll) {
            final Creature target = targets.getFirst();
            int hitPoints = D4.roll();
            log.info("{} heals on {} for {} with a {}", actor.getName(), target.getName(), hitPoints, name);
            target.healDamage(hitPoints);
        }

    }

}
