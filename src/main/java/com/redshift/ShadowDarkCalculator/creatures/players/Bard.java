package com.redshift.ShadowDarkCalculator.creatures.players;

import com.redshift.ShadowDarkCalculator.actions.Action;
import com.redshift.ShadowDarkCalculator.cards.DeckOfCards;
import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.creatures.CreatureLabel;
import com.redshift.ShadowDarkCalculator.creatures.Stats;
import com.redshift.ShadowDarkCalculator.encounter.Encounter;
import com.redshift.ShadowDarkCalculator.targets.single.FocusFireTargetSelector;
import com.redshift.ShadowDarkCalculator.targets.SingleTargetSelector;

import java.util.List;

/**
 * Class specific player; Bard.
 */

public class Bard extends Player {

    public Bard(
            String name,
            int level,
            Stats stats,
            int armorClass,
            int hitPoints,
            Action action) {

        super(name, level, stats, armorClass, hitPoints, action, new FocusFireTargetSelector());
        getLabels().add(CreatureLabel.BARD);
    }

    public Bard(
            String name,
            int level,
            Stats stats,
            int armorClass,
            int hitPoints,
            Action action,
            SingleTargetSelector singleTargetSelector) {

        super(name, level, stats, armorClass, hitPoints, action, singleTargetSelector);
        getLabels().add(CreatureLabel.BARD);
    }

    @Override
    public String getClassName() {
        return PlayerClass.BARD.getClassName();
    }

    @Override
    public void takePreCombatTurn(List<Creature> enemies, List<Creature> allies, Encounter encounter) {
        // Inspire. Each day, you can grant a number of luck tokens equal to
        // your Charisma modifier (min. 1).

        int luckTokens = Math.max(1, getStats().getCharismaModifier());

        final DeckOfCards deck = new DeckOfCards(allies.size());

        for (int i = 0; i < luckTokens; i++) {
            allies.get(deck.draw() - 1).giveLuckToken();
        }
    }

}
