package com.redshift.ShadowDarkCalculator.actions.spells;

import com.redshift.ShadowDarkCalculator.conditions.FascinatedCondition;
import com.redshift.ShadowDarkCalculator.conditions.ProtectionFromEvilCondition;
import com.redshift.ShadowDarkCalculator.conditions.SpellFocusCondition;
import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.dice.RollModifier;
import com.redshift.ShadowDarkCalculator.encounter.Encounter;
import com.redshift.ShadowDarkCalculator.targets.multi.AliveAwakeTargetSelector;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D10;
import static java.lang.Math.min;

/**
 * Fascinate (Focus). Make a DC 12 CHA check. On a success, you transfix all targets
 * in near whose LV is equal to or less than 1 + half your level (round down). If you
 * fail, excluding focus, you can't use this again until you rest.
 */

@Slf4j
public class Fascinate extends MultipleTargetSpell {

    public Fascinate() {
        // Total targets D4 affected... rules say near size cube from player.
        super("Fascinate", 12, RollModifier.CHARISMA, D10);
    }

    @Override
    public boolean canPerform(Creature actor, List<Creature> enemies, List<Creature> allies) {
        final boolean canPerform = super.canPerform(actor, enemies, allies);
        final boolean hasTarget = !new AliveAwakeTargetSelector().getTargets(enemies, enemies.size()).isEmpty();
        final boolean hasFocus = actor.hasCondition(SpellFocusCondition.class.getName());
        return (canPerform && hasTarget && !hasFocus);
    }

    @Override
    public List<Creature> getTargets(Creature actor, List<Creature> enemies, List<Creature> allies) {
        final int numberOfTargets = min(enemies.size(), totalTargets.roll());
        return new AliveAwakeTargetSelector().getTargets(enemies, numberOfTargets);
    }

    @Override
    public void performCriticalSpell(Creature actor, List<Creature> targets, Encounter encounter, int spellCheckRoll) {
        targets.forEach(target -> {
            if (target.getLevel() <= (1 + (actor.getLevel() / 2))) {
                target.addCondition(new FascinatedCondition());
                log.info("{} critically hits a spell on {} with a {}", actor.getName(), target.getName(), name);
            } else {
                log.info("{} critically hits a spell on {} with a {} but doesn't affect the creature.", actor.getName(), target.getName(), name);
                lost = true; // Doesn't affect at least one creature... stop casting Fascinate!
            }
        });

        actor.addCondition(new SpellFocusCondition(
                12,
                RollModifier.CHARISMA,
                spellCheckAdvantage,
                spellCheckBonus,
                new RemoveFascinatedCondition(targets)
        ));
    }

    @Override
    public void performSpell(Creature actor, List<Creature> targets, Encounter encounter, int spellCheckRoll) {
        targets.forEach(target -> {
            if (target.getLevel() <= (1 + (actor.getLevel() / 2))) {
                target.addCondition(new FascinatedCondition());
                log.info("{} hits a spell on {} with a {}", actor.getName(), target.getName(), name);
            } else {
                log.info("{} hits a spell on {} with a {} but doesn't affect the creature.", actor.getName(), target.getName(), name);
                lost = true; // Doesn't affect at least one creature... stop casting Fascinate!
            }
        });

        actor.addCondition(new SpellFocusCondition(
                12,
                RollModifier.CHARISMA,
                spellCheckAdvantage,
                spellCheckBonus,
                new RemoveFascinatedCondition(targets)
        ));
    }

    /**
     * Runnable to remove the Fascinated Condition on spell focus loss.
     */

    private static class RemoveFascinatedCondition implements Runnable {

        private final List<Creature> creatures;

        private RemoveFascinatedCondition(List<Creature> creatures) {
            this.creatures = creatures;
        }

        @Override
        public void run() {
            for (Creature creature : creatures) {
                if (creature.hasCondition(FascinatedCondition.class.getName())) {
                    log.info("{} is no longer fascinated and transfixed!", creature.getName());
                    creature.removeCondition(FascinatedCondition.class.getName());
                }
            }
        }
    }
}
