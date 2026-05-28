package com.redshift.ShadowDarkCalculator.actions.spells;

import com.redshift.ShadowDarkCalculator.conditions.HolyWeaponCondition;
import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.creatures.CreatureLabel;
import com.redshift.ShadowDarkCalculator.dice.RollModifier;
import com.redshift.ShadowDarkCalculator.encounter.Encounter;
import com.redshift.ShadowDarkCalculator.targets.SingleTargetSelector;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Tier 1, priest
 * Duration: 5 rounds
 * Range: Close
 *
 * One weapon you touch is imbued with a sacred blessing. The weapon becomes magical
 * and has +1 to attack and damage rolls for the duration.
 */

@Slf4j
public class HolyWeapon extends Spell {

    public HolyWeapon() {
        super("Holy Weapon", 11, RollModifier.WISDOM);
    }

    @Override
    public boolean canPerform(Creature actor, List<Creature> enemies, List<Creature> allies) {
        final boolean canPerform = super.canPerform(actor, enemies, allies);
        final boolean hasTarget = new HolyWeaponTargetSelector().get(allies) != null;
        return (canPerform && hasTarget);
    }

    @Override
    public List<Creature> getTargets(Creature actor, List<Creature> enemies, List<Creature> allies) {
        final List<Creature> targets = new ArrayList<>();
        final Creature target = new HolyWeaponTargetSelector().get(allies);
        if (target != null) targets.add(target);
        return targets;
    }

    @Override
    public void performCriticalSpell(Creature actor, List<Creature> targets, Encounter encounter, int spellCheckRoll) {
        final Creature target = targets.getFirst(); // Always uses single target.
        target.addCondition(new HolyWeaponCondition(10));
        log.info("{} critically succeeds casting {} on {}", actor.getName(), name, target.getName());
    }

    @Override
    public void performSpell(Creature actor, List<Creature> targets, Encounter encounter, int spellCheckRoll) {
        final Creature target = targets.getFirst(); // Always uses single target.
        target.addCondition(new HolyWeaponCondition());
        log.info("{} casts {} on {}", actor.getName(), name, target.getName());
    }

    /**
     * A target selector for the Holy Weapon spell.
     */

    private static class HolyWeaponTargetSelector implements SingleTargetSelector {

        @Override
        public Creature get(List<Creature> targetOptions) {
            final List<Creature> creaturesWithoutHolyWeapon = new java.util.ArrayList<>(targetOptions
                    .stream()
                    .filter(creature -> !creature.isUnconscious())
                    .filter(creature -> !creature.isDead())
                    .filter(creature -> !creature.getAction().isMagicalWeapon())
                    .filter(creature -> creature.getLabels().contains(CreatureLabel.FRONT_LINE))
                    .filter(creature -> !creature.hasCondition(HolyWeaponCondition.class.getName()))
                    .toList());

            if (creaturesWithoutHolyWeapon.isEmpty()) {
                return null;
            } else {
                Collections.shuffle(creaturesWithoutHolyWeapon);
                return creaturesWithoutHolyWeapon.getFirst();
            }
        }

    }

}
