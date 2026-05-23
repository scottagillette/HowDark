package com.redshift.ShadowDarkCalculator.actions.spells;

import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.dice.Dice;
import com.redshift.ShadowDarkCalculator.dice.RollModifier;
import com.redshift.ShadowDarkCalculator.encounter.Encounter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * A spell that can affect a single targets and does damage to it, in addition
 * to some special effect. Note this uses the actors assigned target selection;
 * not a specialized one.
 */

@Slf4j
public abstract class SingleTargetDamageWithEffectSpell extends SingleTargetDamageSpell {

    public SingleTargetDamageWithEffectSpell(String name, int difficultyClass, RollModifier rollModifier, Dice damageDice, boolean advantage) {
        super(name, difficultyClass, rollModifier, damageDice, advantage);
    }

    @Override
    public void perform(Creature actor, List<Creature> enemies, List<Creature> allies, Encounter encounter) {
        final Creature target = actor.getSingleTargetSelector().get(enemies);

        if (target == null) {
            log.info("{} is skipping their turn... no target!", actor.getName());
        } else {
            final boolean spellWorks = super.performSingleTargetSpellAttack(actor, target, this, difficultyClass, damageDice, rollModifier);

            if (spellWorks) {
                performEffect(actor, target, allies, encounter);
            }
        }
    }

    /**
     * The spell has worked; perform any special effect.
     */

    public abstract void performEffect(Creature actor, Creature target, List<Creature> allies, Encounter encounter);
}
