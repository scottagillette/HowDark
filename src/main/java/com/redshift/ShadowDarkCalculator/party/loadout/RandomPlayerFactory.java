package com.redshift.ShadowDarkCalculator.party.loadout;

import com.redshift.ShadowDarkCalculator.actions.Action;
import com.redshift.ShadowDarkCalculator.creatures.Stats;
import com.redshift.ShadowDarkCalculator.creatures.players.*;
import com.redshift.ShadowDarkCalculator.dice.MultipleDice;
import com.redshift.ShadowDarkCalculator.party.loadout.actions.ActionSelector;
import com.redshift.ShadowDarkCalculator.party.loadout.ancestry.AncestrySelector;
import com.redshift.ShadowDarkCalculator.party.loadout.classes.ClassSelector;
import com.redshift.ShadowDarkCalculator.targets.single.FocusFireTargetSelector;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Random;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.*;

/**
 * A factory for creating random players.
 */

@Slf4j
public class RandomPlayerFactory {

    private static final Random RANDOM = new Random();

    // Official Shadowdark random names!
    private static final List<String> NAMES = List.of(
            "Hera", "Sarenia", "Kog", "Myrtle", "Troga", "Hesta",
            "Torin", "Ravos", "Dibbs", "Robby", "Boraal", "Matteo",
            "Ginny", "Imeria", "Fronk", "Nora", "Urgana", "Rosalin",
            "Gant", "Farond", "Irv", "Percy", "Zoraal", "Endric",
            "Olga", "Isolden", "Squag", "Daisy", "Scalga", "Kiara",
            "Dendor", "Kieren", "Mort", "Jolly", "Krell", "Yao",
            "Ygrid", "Mirenel", "Vig", "Evelyn", "Voraga", "Corina",
            "Pike", "Riarden", "Sticks", "Horace", "Morak", "Rowan",
            "Sarda", "Allindra", "Gorb", "Willie", "Draga", "Hariko",
            "Brigg", "Arlomas", "Yogg", "Gertie", "Sorak", "Ikam",
            "Zorli", "Sylara", "Plok", "Peri", "Varga", "Mariel",
            "Yorin", "Tyr", "Zrak", "Carlsby", "Ulgar", "Jin",
            "Jorgena", "Rinariel", "Dent", "Nyx", "Jala", "Hana",
            "Trogin", "Saramir", "Krik", "Kellan", "Kresh", "Lios",
            "Riga", "Vedana", "Mizzo", "Fern", "Zana", "Indra",
            "Barton", "Elindos", "Bort", "Harlow", "Torvash", "Remy",
            "Katrina", "Ophelia", "Nabo", "Moira", "Rokara", "Nura",
            "Egrim", "Cydaros", "Hink", "Sage", "Gartak", "Vakesh",
            "Elsa", "Tiramel", "Bree", "Reenie", "Iskana", "Una",
            "Orgo", "Varond", "Kreeb", "Wendry", "Ziraak", "Nabilo"
    );

    public Player create() {
        final Bonuses bonuses = new Bonuses();

        // Step 1. Level 1
        final int level = 1;

        // Step 2. Generate random stats
        final Stats initialStats = generateStats();

        // Step 3. Select Class based on highest stat
        final ClassSelector classSelector = new ClassSelector();
        final PlayerClass playerClass = classSelector.selectPlayerClass(initialStats, bonuses);

        // Step 4. Select Ancestry to compliment class.
        final AncestrySelector ancestrySelector = new AncestrySelector();
        final Ancestry playerAncestry =  ancestrySelector.selectAndApplyBonuses(playerClass, bonuses);

        // Step 5. Generate random name.
        final String name = NAMES.get(RANDOM.nextInt(NAMES.size())) + " the " + playerAncestry.getDisplayName();

        // Step 6. Update any stats that have been updated based on ancestry or clas.
        final Stats finalStats = new Stats(
                Math.min(18, initialStats.getStrength() + bonuses.getStrengthBonus()),
                Math.min(18, initialStats.getDexterity() + bonuses.getDexterityBonus()),
                Math.min(18, initialStats.getConstitution() + bonuses.getConstitutionBonus()),
                Math.min(18, initialStats.getIntelligence() + bonuses.getIntelligenceBonus()),
                Math.min(18, initialStats.getWisdom() + bonuses.getWisdomBonus()),
                Math.min(18, initialStats.getCharisma() + bonuses.getCharismaBonus())
        );

        // Step 7. Roll Hit points after Ancestry selected.
        final int hitPoints = generateHitPoints(playerClass, finalStats, bonuses);

        // Step 8. Select Armor buildout based on class.
        final int armorClass = selectPlayerArmor(playerClass, bonuses) + finalStats.getDexterityModifier(); // Final AC

        // Step 9. Select player actions based on class
        final ActionSelector actionSelector = new ActionSelector();
        final Action actions =  actionSelector.selectActions(playerClass, finalStats, bonuses);

        // Step 10. Create the specific player class.
        final Player player = createPlayer(playerClass, name, level, finalStats, armorClass, hitPoints, actions);
        log.info(player.toString() + "\n");
        return player;
    }

    private Player createPlayer(PlayerClass playerClass, String name, int level, Stats stats, int armorClass, int hitPoints, Action actions) {
        final Player player;

        switch (playerClass) {
            case BARD -> player = new Bard(name, level, stats, armorClass, hitPoints, actions, new FocusFireTargetSelector());
            case FIGHTER -> player = new Fighter(name, level, stats, armorClass, hitPoints, actions, new FocusFireTargetSelector());
            case NECROMANCER -> player = new Necromancer(name, level, stats, armorClass, hitPoints, actions, new FocusFireTargetSelector());
            case PALADIN -> player = new Paladin(name, level, stats, armorClass, hitPoints, actions, new FocusFireTargetSelector());
            case PRIEST -> player = new Priest(name, level, stats, armorClass, hitPoints, actions, new FocusFireTargetSelector());
            case RANGER -> player = new Ranger(name, level, stats, armorClass, hitPoints, actions, new FocusFireTargetSelector());
            case THIEF -> player = new Thief(name, level, stats, armorClass, hitPoints, actions, new FocusFireTargetSelector());
            case WITCH -> player = new Witch(name, level, stats, armorClass, hitPoints, actions, new FocusFireTargetSelector());
            case WIZARD -> player = new Wizard(name, level, stats, armorClass, hitPoints, actions, new FocusFireTargetSelector());
            default -> throw new IllegalArgumentException("Invalid player class: " + playerClass);
        }

        return player;
    }

    private int generateHitPoints(PlayerClass playerClass, Stats stats, Bonuses bonuses) {
        int hitPoints;

        if (bonuses.isHitPointAdvantageRoll()) {
            int roll1 = playerClass.getHitDice().roll();
            int roll2 = playerClass.getHitDice().roll();
            hitPoints = Math.max(roll1, roll2);
        } else {
            hitPoints = playerClass.getHitDice().roll();
        }

        // Min 1 hp after CON modifier and any bonuses.
        hitPoints = Math.max(1, hitPoints + stats.getConstitutionModifier() + bonuses.getHitPointsBonus());

        return hitPoints;
    }

    private Stats generateStats() {
        final Stats stats = new Stats(
                new MultipleDice(D6, D6, D6).roll(),
                new MultipleDice(D6, D6, D6).roll(),
                new MultipleDice(D6, D6, D6).roll(),
                new MultipleDice(D6, D6, D6).roll(),
                new MultipleDice(D6, D6, D6).roll(),
                new MultipleDice(D6, D6, D6).roll()
        );

        if (stats.getStrength() >= 14) return stats;
        if (stats.getDexterity() >= 14) return stats;
        if (stats.getConstitution() >= 14) return stats;
        if (stats.getWisdom() >= 14) return stats;
        if (stats.getIntelligence() >= 14) return stats;
        if (stats.getCharisma() >= 14) return stats;

        return generateStats();
    }

    private int selectPlayerArmor(PlayerClass playerClass, Bonuses bonuses) {
        int armorClass;

        switch (playerClass) {
            case BARD, FIGHTER, PALADIN, PRIEST: {
                // Leather & Shield (13) OR leather (11); no shield can use two-handed weapons.
                int randomRoll = D2.roll();
                if (randomRoll == 1) {
                    armorClass = 13; // Weapon and Shield!
                } else {
                    bonuses.addTwoHandsFree();
                    armorClass = 11; // Two-handed weapon please!
                }
                break;
            }
            case RANGER, THIEF, NECROMANCER, WITCH: {
                armorClass = 11; // Leather
                bonuses.addTwoHandsFree();
                break;
            }
            case WIZARD: {
                armorClass = 10; // Robes!
                bonuses.addTwoHandsFree();
                break;
            }
            default: {
                throw new IllegalArgumentException("Invalid player class: " + playerClass);
            }
        }

        return armorClass;
    }

}
