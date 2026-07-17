package com.redshift.ShadowDarkCalculator.actions.items;

import com.redshift.ShadowDarkCalculator.actions.Action;
import com.redshift.ShadowDarkCalculator.actions.BaseAction;
import com.redshift.ShadowDarkCalculator.actions.spells.Spell;
import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.encounter.Encounter;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * A magic wand with an attached spell.
 */

@Slf4j
public class MagicWand extends BaseAction implements Action {

    private Spell spell;

    public MagicWand(Spell spell) {
        super("Wand of " + spell.getName());
    }

    @Override
    public boolean canPerform(Creature actor, List<Creature> enemies, List<Creature> allies) {
        return spell.canPerform(actor, enemies, allies);
    }

    @Override
    public boolean isMagicalWeapon() {
        return false;
    }

    @Override
    public void perform(Creature actor, List<Creature> enemies, List<Creature> allies, Encounter encounter) {
        spell.perform(actor, enemies, allies, encounter);
    }

}
