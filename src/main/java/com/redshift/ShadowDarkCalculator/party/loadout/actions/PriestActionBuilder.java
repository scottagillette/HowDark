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
 * Specific action builder for the Priest class.
 */

@Slf4j
public class PriestActionBuilder implements ActionBuilder {

    @Override
    public Action build(Stats stats, Bonuses bonuses) {
        // Priest look at two hands as well but also DEX vs STR.

        final List<Action> actions = new ArrayList<>();

        // TODO: Randomize from all the weapons they can use?
        // Club, crossbow, dagger, mace, longsword, staff, warhammer

        if (stats.getStrength() >= stats.getDexterity()) {
            // STR Build
            if (bonuses.isTwoHandsFree()) {
                actions.add(WeaponBuilder.WAR_HAMMER.build()
                        .addCrushing()
                        .addAttackRollBonus(bonuses.getMeleeAttackBonus())
                        .addDamageRollBonus(bonuses.getMeleeDamageBonus())
                        .setPriority(1)
                );
            } else {
                actions.add(WeaponBuilder.LONGSWORD.build()
                        .addSlashing()
                        .addAttackRollBonus(bonuses.getMeleeAttackBonus())
                        .addDamageRollBonus(bonuses.getMeleeDamageBonus())
                        .setPriority(1)
                );
            }
        } else {
            // DEX Build
            if (bonuses.isTwoHandsFree()) {
                actions.add(WeaponBuilder.CROSSBOW.build()
                        .addPiercing()
                        .addAttackRollBonus(bonuses.getRangedAttackBonus())
                        .addDamageRollBonus(bonuses.getRangedDamageBonus())
                        .setPriority(1)
                );
            } else {
                // No good melee DEX options for Priests
                actions.add(WeaponBuilder.LONGSWORD.build()
                        .addSlashing()
                        .addAttackRollBonus(bonuses.getMeleeAttackBonus())
                        .addDamageRollBonus(bonuses.getMeleeDamageBonus())
                        .setPriority(1)
                );
            }
        }

        actions.add(new TurnUndead()
                .addSpellCheckBonus(bonuses.getSpellCheckBonus())
                .setPriority(10)
        );

        // Select spells

        final List<Action> spellActions = buildSpellList();
        final DeckOfCards deckOfCards = new DeckOfCards(spellActions.size());

        int drawCount = 2 + bonuses.getExtraSpellChoice();
        int advantageCount = bonuses.getSpellAdvantages();

        for (int i = 1; i <= drawCount; i++) {
            final Spell spell = (Spell)spellActions.get(deckOfCards.draw() - 1);
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
        // Cure Wounds, Holy Weapon, Light, Protection from Evil, Shield of faith

        final List<Action> spells = new ArrayList<>();

        spells.add(new CureWounds().setPriority(10));
        spells.add(new HolyWeapon().setPriority(5));
        //spells.add(new Light().setPriority(1)); // TODO: Who selects light!
        spells.add(new ProtectionFromEvil().setPriority(5));
        spells.add(new ShieldOfFaith().setPriority(5));

        return spells;
    }
}
