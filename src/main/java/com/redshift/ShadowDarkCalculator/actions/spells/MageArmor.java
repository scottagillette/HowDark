package com.redshift.ShadowDarkCalculator.actions.spells;

import com.redshift.ShadowDarkCalculator.conditions.MageArmorCondition;
import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.dice.RollModifier;
import com.redshift.ShadowDarkCalculator.encounter.Encounter;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * Tier 1, wizard
 * Duration: 10 rounds
 * Range: Self
 *
 * An invisible layer of magical force protects your vitals. Your armor class becomes 14
 * (18 on a critical spell casting roll)
 */

@Slf4j
public class MageArmor extends Spell {

    public MageArmor() {
        super("Mage Armor", 11, RollModifier.INTELLIGENCE);
    }

    @Override
    public boolean canPerform(Creature actor, List<Creature> enemies, List<Creature> allies) {
        boolean canPerform = super.canPerform(actor, enemies, allies);
        boolean hasMageArmor = actor.hasCondition(MageArmorCondition.class.getName());
        return canPerform && !hasMageArmor;
    }

    @Override
    public List<Creature> getTargets(Creature actor, List<Creature> enemies, List<Creature> allies) {
        final List<Creature> targets = new ArrayList<>();
        targets.add(actor);
        return targets;
    }

    @Override
    public void performCriticalSpell(Creature actor, List<Creature> targets, Encounter encounter, int spellCheckRoll) {
        actor.addCondition(new MageArmorCondition(10, 18));
        log.info("{} critically casts {} for 18 AC!", actor.getName(), name);
    }

    @Override
    public void performSpell(Creature actor, List<Creature> targets, Encounter encounter, int spellCheckRoll) {
        actor.addCondition(new MageArmorCondition(10, 14));
        log.info("{} casts {} for 14 AC.", actor.getName(), name);
    }

}
