package com.redshift.ShadowDarkCalculator.actions.spells;

import com.redshift.ShadowDarkCalculator.conditions.DevouredCondition;
import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.creatures.CreatureLabel;
import com.redshift.ShadowDarkCalculator.creatures.monsters.undead.Skeleton;
import com.redshift.ShadowDarkCalculator.dice.RollModifier;
import com.redshift.ShadowDarkCalculator.encounter.Encounter;
import com.redshift.ShadowDarkCalculator.targets.multi.CreatureLabelTargetSelector;
import com.redshift.ShadowDarkCalculator.targets.single.DeadCreatureTargetSelector;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * You touch one humanoid’s remains, and it rises as a zombie or skeleton under your control.
 * The remains must have at least three limbs and its head intact. The creature acts on your turn.
 * You can only create one undead creature with this spell at a time. When the spell ends, the
 * corpse collapses into grave dust.
 *
 * TODO: Skeleton added doesn't go away after 5 rounds. TODO: Randomly generate a Zombie?
 */

@Slf4j
public class Undeath extends Spell {

    public Undeath() {
        super("Undeath", 11, RollModifier.CHARISMA);
    }

    @Override
    public boolean canPerform(Creature actor, List<Creature> enemies, List<Creature> allies) {
        final boolean canPerform = super.canPerform(actor, enemies, allies);
        final boolean hasTarget = !getTargets(actor, enemies, allies).isEmpty();
        return (canPerform && hasTarget);
    }

    @Override
    public List<Creature> getTargets(Creature actor, List<Creature> enemies, List<Creature> allies) {
        final List<Creature> targets = new ArrayList<>();

        final List<Creature> allCreatures = new ArrayList<>();
        allCreatures.addAll(enemies);
        allCreatures.addAll(allies); // Might as well summon a dead ally!

        final List<Creature> humanoidCreatures = new CreatureLabelTargetSelector(CreatureLabel.HUMANOID)
                .getTargets(allCreatures, allCreatures.size());

        targets.add(new DeadCreatureTargetSelector().get(humanoidCreatures));

        return targets;
    }

    @Override
    public void performCriticalSpell(Creature actor, List<Creature> targets, Encounter encounter, int spellCheckRoll) {
        final Creature target = targets.getFirst();
        log.info("{} critically succeeds on raising a skeleton for 10 rounds with {}", actor.getName(), name);
        encounter.addFriendlyCreature(actor, new Skeleton("Undeath Skeleton"));
        lost = true; // Single per combat use.
        target.addCondition(new DevouredCondition()); // Mark corpse as devoured so it can't be used again.
    }

    @Override
    public void performSpell(Creature actor, List<Creature> targets, Encounter encounter, int spellCheckRoll) {
        final Creature target = targets.getFirst();
        log.info("{} succeeds on raising a skeleton for 5 rounds with {}", actor.getName(), name);
        encounter.addFriendlyCreature(actor, new Skeleton("Undeath Skeleton"));
        lost = true; // Single per combat use.
        target.addCondition(new DevouredCondition()); // Mark corpse as devoured so it can't be used again.
    }

}
