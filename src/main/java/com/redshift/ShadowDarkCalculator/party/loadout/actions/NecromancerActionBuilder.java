package com.redshift.ShadowDarkCalculator.party.loadout.actions;

import com.redshift.ShadowDarkCalculator.actions.Action;
import com.redshift.ShadowDarkCalculator.actions.PerformOneAction;
import com.redshift.ShadowDarkCalculator.actions.spells.*;
import com.redshift.ShadowDarkCalculator.actions.weapons.WeaponBuilder;
import com.redshift.ShadowDarkCalculator.cards.DeckOfCards;
import com.redshift.ShadowDarkCalculator.creatures.Stats;
import com.redshift.ShadowDarkCalculator.party.loadout.Bonuses;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * Specific action builder for the Necromancer class.
 */

@Slf4j
public class NecromancerActionBuilder implements ActionBuilder {

    @Override
    public Action build(Stats stats, Bonuses bonuses) {
        // Weapons: Crossbow, dagger, longsword, scimitar, staff, stave

        final List<Action> actions = new ArrayList<>();

        if (stats.getStrength() >= stats.getDexterity()) {
            // STR Build
            actions.add(WeaponBuilder.LONGSWORD.build()
                    .addAttackRollBonus(bonuses.getMeleeAttackBonus())
                    .addDamageRollBonus(bonuses.getMeleeDamageBonus())
                    .setPriority(1)
            );
        } else {
            // DEX Build
            actions.add(WeaponBuilder.CROSSBOW.build()
                    .addAttackRollBonus(bonuses.getRangedAttackBonus())
                    .addDamageRollBonus(bonuses.getRangedDamageBonus())
                    .setPriority(1)
            );
        }

        // Select spells

        final List<Action> spellActions = buildSpellList();
        final DeckOfCards deckOfCards = new DeckOfCards(spellActions.size() - 1);

        int drawCount = 2 + bonuses.getExtraSpellChoice();
        int advantageCount = bonuses.getSpellAdvantages();

        for (int i = 1; i <= drawCount; i++) {
            final Spell spell = (Spell)spellActions.get(deckOfCards.draw());
            spell.addSpellCheckBonus(bonuses.getSpellCheckBonus());

            if (advantageCount > 0) {
                spell.addAdvantage();
                advantageCount--;
            }
            actions.add(spell);
        }

        return new PerformOneAction(actions.toArray(new Action[0]));
    }

    private List<Action> buildSpellList() {
        // Level 1Spells: Protection from Evil, Turn Undead, Undeath, Withermark
        // TODO: First Gate, Seal Soul

        final List<Action> spells = new ArrayList<>();

        spells.add(new ProtectionFromEvil().setPriority(2));
        spells.add(new TurnUndead().setPriority(10));
        spells.add(new Undeath().setPriority(10));
        spells.add(new Withermark().setPriority(5));

        return spells;
    }

}
