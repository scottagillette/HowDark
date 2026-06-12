package com.redshift.ShadowDarkCalculator.actions.spells;

import com.redshift.ShadowDarkCalculator.conditions.ProtectionFromEvilCondition;
import com.redshift.ShadowDarkCalculator.conditions.SpellFocusCondition;
import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.dice.RollModifier;
import com.redshift.ShadowDarkCalculator.encounter.Encounter;
import com.redshift.ShadowDarkCalculator.targets.SingleTargetSelector;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Tier 1, priest, wizard
 * Duration: Focus
 * Range: Close
 *
 * For the spell’s duration, chaotic beings have disadvantage on attack rolls and hostile
 * spell casting checks against the target.
 * These beings also can’t possess , compel , or beguile it.
 * TODO: When cast on an already-possessed target, the possessing entity makes a CHA
 * TODO: check vs. the last spell casting check. On a failure, the entity is expelled.
 */

@Slf4j
public class ProtectionFromEvil extends Spell {

    public ProtectionFromEvil() {
        super("Protection from Evil", 11, RollModifier.WISDOM);
    }

    @Override
    public boolean canPerform(Creature actor, List<Creature> enemies, List<Creature> allies) {
        final boolean canPerform = super.canPerform(actor, enemies, allies);
        final boolean hasTarget = new ProtectionFromEvilTargetSelector().get(allies) != null;
        final boolean hasFocus = actor.hasCondition(SpellFocusCondition.class.getName());
        return canPerform && hasTarget && !hasFocus;
    }

    @Override
    public List<Creature> getTargets(Creature actor, List<Creature> enemies, List<Creature> allies) {
        final List<Creature> targets = new ArrayList<>();
        final Creature target = new ProtectionFromEvilTargetSelector().get(allies);
        if (target != null) targets.add(target);
        return targets;
    }

    @Override
    public void performCriticalSpell(Creature actor, List<Creature> targets, Encounter encounter, int spellCheckRoll) {
        final Creature target = targets.getFirst();
        actor.addCondition(new SpellFocusCondition(
                11,
                RollModifier.WISDOM,
                spellCheckAdvantage,
                spellCheckBonus,
                new RemoveProtectionFromEvilCondition(target)
        ));
        target.addCondition(new ProtectionFromEvilCondition());
        log.info("{} critically succeeds casting {} on {}", actor.getName(), name, target.getName());
    }

    @Override
    public void performSpell(Creature actor, List<Creature> targets, Encounter encounter, int spellCheckRoll) {
        final Creature target = targets.getFirst();
        actor.addCondition(new SpellFocusCondition(
                11,
                RollModifier.WISDOM,
                spellCheckAdvantage,
                spellCheckBonus,
                new RemoveProtectionFromEvilCondition(target)
        ));
        target.addCondition(new ProtectionFromEvilCondition());
        log.info("{} casts {} on {}", actor.getName(), name, target.getName());
    }

    private static class ProtectionFromEvilTargetSelector implements SingleTargetSelector {

        @Override
        public Creature get(List<Creature> targetOptions) {
            final List<Creature> creaturesWithoutProtection = new java.util.ArrayList<>(targetOptions
                    .stream()
                    .filter(creature -> !creature.isUnconscious())
                    .filter(creature -> !creature.isDead())
                    .filter(creature -> !creature.hasFled())
                    .filter(creature -> !creature.hasCondition(ProtectionFromEvil.class.getName()))
                    .toList());

            if (creaturesWithoutProtection.isEmpty()) {
                return null;
            } else {
                Collections.shuffle(creaturesWithoutProtection);
                return creaturesWithoutProtection.getFirst();
            }
        }

    }

    /**
     * Runnable to remove the Protection from Evil Condition on spell focus loss.
     */

    private static class RemoveProtectionFromEvilCondition implements Runnable {

        private final Creature creature;

        private RemoveProtectionFromEvilCondition(Creature creature) {
            this.creature = creature;
        }

        @Override
        public void run() {
            if (creature.hasCondition(ProtectionFromEvilCondition.class.getName())) {
                log.info("{} is no longer protected from evil!", creature.getName());
                creature.removeCondition(ProtectionFromEvilCondition.class.getName());
            }
        }
    }

}
