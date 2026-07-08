package com.redshift.ShadowDarkCalculator.party.generator.actions;

import com.redshift.ShadowDarkCalculator.actions.Action;
import com.redshift.ShadowDarkCalculator.actions.PerformOneAction;
import com.redshift.ShadowDarkCalculator.actions.spells.*;
import com.redshift.ShadowDarkCalculator.actions.weapons.WeaponBuilder;
import com.redshift.ShadowDarkCalculator.cards.DeckOfCards;
import com.redshift.ShadowDarkCalculator.creatures.Stats;
import com.redshift.ShadowDarkCalculator.party.generator.Bonuses;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * Specific action builder for the Wizard class.
 */

@Slf4j
public class WizardActionBuilder implements ActionBuilder {

    @Override
    public Action build(Stats stats, Bonuses bonuses) {

        final List<Action> actions = new ArrayList<>();

        if (stats.getStrength() >= stats.getDexterity()) {
            // STR Build
            actions.add(WeaponBuilder.STAFF.build()
                    .addAttackRollBonus(bonuses.getMeleeAttackBonus())
                    .addDamageRollBonus(bonuses.getMeleeDamageBonus())
                    .setPriority(1)
            );
        } else {
            // DEX Build
            actions.add(WeaponBuilder.DAGGER_DEX.build()
                    .addAttackRollBonus(bonuses.getRangedAttackBonus())
                    .addDamageRollBonus(bonuses.getRangedDamageBonus())
                    .setPriority(1)
            );
        }

        // Select spells

        final List<Action> spellActions = buildSpellList();
        final DeckOfCards deckOfCards = new DeckOfCards(spellActions.size());

        int drawCount = 3 + bonuses.getExtraSpellChoice();
        int advantageCount = bonuses.getSpellAdvantages();

        for (int i = 1; i <= drawCount; i++) {
            final Spell spell = (Spell)spellActions.get(deckOfCards.draw() - 1);
            spell.addSpellCheckBonus(bonuses.getSpellCheckBonus());

            if (advantageCount > 0 && !spell.isSpellCheckWithAdvantage()) {
                spell.addSpellCheckWithAdvantage();
                advantageCount--;
            }
            actions.add(spell);
        }

        return new PerformOneAction(actions.toArray(new Action[0]));
    }

    private List<Action> buildSpellList() {
        // TODO: Alarm, Hold Portal, Light, Charm Person, Detect Magic, Feather Fall, Floating Disk
        // Burning Hands, Mage Armor, Magic Missile, Protection from Evil, Sleep

        final List<Action> spells = new ArrayList<>();

        spells.add(new BurningHands().setPriority(10));
        spells.add(new Light().setPriority(1)); // Always 'lost' so it just takes up space.
        spells.add(new MageArmor().setPriority(5));
        spells.add(new MagicMissile().setPriority(3));
        spells.add(new ProtectionFromEvil().setPriority(5));
        spells.add(new Sleep().setPriority(10));

        return spells;
    }

}
