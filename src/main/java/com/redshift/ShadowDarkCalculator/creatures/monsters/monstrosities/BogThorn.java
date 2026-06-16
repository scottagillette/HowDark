package com.redshift.ShadowDarkCalculator.creatures.monsters.monstrosities;

import com.redshift.ShadowDarkCalculator.actions.PerformOneAction;
import com.redshift.ShadowDarkCalculator.actions.weapons.Weapon;
import com.redshift.ShadowDarkCalculator.conditions.ParalyzedCondition;
import com.redshift.ShadowDarkCalculator.creatures.Creature;
import com.redshift.ShadowDarkCalculator.creatures.CreatureLabel;
import com.redshift.ShadowDarkCalculator.creatures.Stats;
import com.redshift.ShadowDarkCalculator.creatures.monsters.Monster;
import com.redshift.ShadowDarkCalculator.creatures.monsters.UndeadMonster;
import com.redshift.ShadowDarkCalculator.dice.RollModifier;
import lombok.extern.slf4j.Slf4j;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.*;

/**
 * A skittering nest of nettles and thorns with marsh lights for eyes.
 * AC 13, HP 11, ATK 1 stab +0 (1d4) or 1 thorn hail (near) +2 (1d4 + poison), MV near (climb)
 * S +0, D +2, C +2, I -3, W +1, Ch -3, AL C, LV 2
 * Poison. DC 12 CON or paralyzed for 1d4 rounds.
 */

@Slf4j
public class BogThorn extends Monster {

    public BogThorn(String name) {
        super(
                name,
                2,
                new Stats(10, 14, 14, 4, 12, 4),
                13,
                D8.roll() + D8.roll() + 2,
                new PerformOneAction(
                        new Weapon("Stab", D4, RollModifier.STRENGTH).addCrushing(),
                        new ThornHail()
                )
        );
        getLabels().add(CreatureLabel.CHAOTIC);
    }

    private static class ThornHail extends Weapon {

        public ThornHail() {
            super("Paralyzing Touch", D4, RollModifier.DEXTERITY);
            addPiercing();
        }

        @Override
        protected boolean performSingleTargetAttack(Creature actor, Creature target) {
            final boolean attackHits = super.performSingleTargetAttack(actor, target);

            if (attackHits) {
                if (!target.isUnconscious() && !target.hasCondition(ParalyzedCondition.class.getName())) {
                    if (!target.getStats().constitutionSave(12)) {
                        int rounds = D4.roll();
                        log.info("{} is paralyzed for {} rounds!", target.getName(), rounds);
                        target.addCondition(new ParalyzedCondition(rounds));
                    } else {
                        log.info("{} SAVES and is NOT paralyzed!", target.getName());
                    }
                }
            }

            return attackHits;
        }
    }

}