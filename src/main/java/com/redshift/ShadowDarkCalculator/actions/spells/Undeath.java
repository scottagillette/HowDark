package com.redshift.ShadowDarkCalculator.actions.spells;

import com.redshift.ShadowDarkCalculator.conditions.DevouredCondition;
import com.redshift.ShadowDarkCalculator.conditions.DisadvantagedCondition;
import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.creatures.CreatureLabel;
import com.redshift.ShadowDarkCalculator.creatures.monsters.undead.Skeleton;
import com.redshift.ShadowDarkCalculator.dice.RollModifier;
import com.redshift.ShadowDarkCalculator.dice.RollOutcome;
import com.redshift.ShadowDarkCalculator.encounter.Encounter;
import com.redshift.ShadowDarkCalculator.targets.CreatureLabelTargetSelector;
import com.redshift.ShadowDarkCalculator.targets.DeadCreatureTargetSelector;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * You touch one humanoid’s remains, and it rises as a zombie or skeleton under your control.
 * The remains must have at least three limbs and its head intact. The creature acts on your turn.
 * You can only create one undead creature with this spell at a time. When the spell ends, the
 * corpse collapses into grave dust.
 *
 * TODO: Skeleton added doesn't go away after 5 rounds. Randomly generate a Zombie?
 */

@Slf4j
public class Undeath extends Spell {

    public Undeath() {
        super("Undeath", 11, RollModifier.CHARISMA);
    }

    @Override
    public boolean canPerform(Creature actor, List<Creature> enemies, List<Creature> allies) {
        final Creature deadCreature = getTarget(enemies, allies);
        return !lost && deadCreature != null;
    }

    private Creature getTarget(List<Creature> enemies, List<Creature> allies) {
        final List<Creature> allCreatures = new ArrayList<>();
        allCreatures.addAll(enemies);
        allCreatures.addAll(allies); // Might as well summon a dead ally!

        final List<Creature> humanoidCreatures = new CreatureLabelTargetSelector(CreatureLabel.HUMANOID)
                .getTargets(allCreatures, allCreatures.size());

        return new DeadCreatureTargetSelector().get(humanoidCreatures);
    }

    @Override
    public void perform(Creature actor, List<Creature> enemies, List<Creature> allies, Encounter encounter) {
        final Creature deadCreature = getTarget(enemies, allies);

        boolean disadvantage = actor.hasCondition(DisadvantagedCondition.class.getName());
        actor.removeCondition(DisadvantagedCondition.class.getName());

        // See if they pass the spell check!
        final int spellCheckRoll = getSpellCheckRoll(disadvantage);

        final boolean criticalSuccess = spellCheckRoll == RollOutcome.CRITICAL_SUCCESS;
        final boolean criticalFailure = spellCheckRoll == RollOutcome.CRITICAL_FAILURE;

        final int spellCheckModifier = actor.getStats().getCharismaModifier(); // Always uses Charisma!

        if (criticalFailure) {
            lost = true; // Failed spell check!
            log.info("{} critically MISSES the spell check on {}", actor.getName(), getName());
        } else if (criticalSuccess) {
            log.info("{} critically succeeds on raising a skeleton for 10 rounds with {}", actor.getName(), getName());
            encounter.addFriendlyCreature(actor, new Skeleton("Undeath Skeleton"));
            lost = true; // Single per combat use.
            deadCreature.addCondition(new DevouredCondition()); // Mark corpse as devoured so it can't be used again.
        } else if (spellCheckRoll + spellCheckModifier + spellCheckBonus >= difficultyClass) {
            log.info("{} succeeds on raising a skeleton for 5 rounds with {}", actor.getName(), getName());
            encounter.addFriendlyCreature(actor, new Skeleton("Undeath Skeleton"));
            lost = true; // Single per combat use.
            deadCreature.addCondition(new DevouredCondition()); // Mark corpse as devoured so it can't be used again.
        } else {
            lost = true; // Failed spell check!
            log.info("{} MISSES the spell check with a {}", actor.getName(), getName());
        }
    }

}
