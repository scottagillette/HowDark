package com.redshift.ShadowDarkCalculator.creatures.players;

import com.redshift.ShadowDarkCalculator.actions.Action;
import com.redshift.ShadowDarkCalculator.conditions.AdvantageCondition;
import com.redshift.ShadowDarkCalculator.conditions.ExtraDamageDiceCondition;
import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.creatures.CreatureLabel;
import com.redshift.ShadowDarkCalculator.creatures.Stats;
import com.redshift.ShadowDarkCalculator.dice.DifficultyClass;
import com.redshift.ShadowDarkCalculator.encounter.Encounter;
import com.redshift.ShadowDarkCalculator.targets.single.FocusFireTargetSelector;
import com.redshift.ShadowDarkCalculator.targets.SingleTargetSelector;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

/**
 * Class specific player; Thief.
 */

@Slf4j
public class Thief extends Player {

    public Thief(
            String name,
            int level,
            Stats stats,
            int armorClass,
            int hitPoints,
            Action action) {

        super(name, level, stats, armorClass, hitPoints, action, new FocusFireTargetSelector());
        addLabels();
    }

    public Thief(
            String name,
            int level,
            Stats stats,
            int armorClass,
            int hitPoints,
            Action action,
            SingleTargetSelector singleTargetSelector) {

        super(name, level, stats, armorClass, hitPoints, action, singleTargetSelector);
        addLabels();
    }

    private void addLabels() {
        getLabels().add(CreatureLabel.FRONT_LINE);
        getLabels().add(CreatureLabel.THIEF);
    }

    @Override
    public String getClassName() {
        return PlayerClass.THIEF.getClassName();
    }

    @Override
    public void takePreCombatTurn(List<Creature> enemies, List<Creature> allies, Encounter encounter) {
        // Thieves can attempt to hide before combat.
        final int dexRoll = Math.max(getStats().dexterityRoll(), getStats().dexterityRoll());

        if (dexRoll >= DifficultyClass.NORMAL.getDc()) {
            log.info("{} has successfully hidden from their enemies in the shadows!", getName());
            addCondition(new AdvantageCondition()); // Advantage when attacking once!
            addCondition(new ExtraDamageDiceCondition(1 + getLevel() / 2));
            takeTurn(enemies, allies, encounter); // Free attack
        } else {
            log.info("{} has failed to hide from their enemies before combat.", getName());
        }

        // Rules:
        // Those with Surprise take a turn before the combats Initiate Roll
        // A creature starting its turn undetected has Advantage on attack rolls

        // Thief:
        // Thievery. ...You are trained in the following tasks and have advantage on
        // any associated checks:
        // • Climbing
        // • Sneaking and hiding
        // ...

        // Thief:
        // Backstab. If you hit a creature who is unaware of your attack,
        // you deal an extra weapon die of damage. Add additional weapon
        // dice of damage equal to half your level (round down).
    }

}
