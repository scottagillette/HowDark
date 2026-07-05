package com.redshift.ShadowDarkCalculator.party;

import com.redshift.ShadowDarkCalculator.actions.Action;
import com.redshift.ShadowDarkCalculator.actions.weapons.WeaponBuilder;
import com.redshift.ShadowDarkCalculator.creatures.Stats;
import com.redshift.ShadowDarkCalculator.creatures.players.Player;
import com.redshift.ShadowDarkCalculator.dice.MultipleDice;
import com.redshift.ShadowDarkCalculator.targets.single.FocusFireTargetSelector;

import java.util.List;
import java.util.Random;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D6;

/**
 * A factory for creating random players.
 */

public class RandomPlayerFactory {

    private static final Random RANDOM = new Random();

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
        // Generate random name.
        final String name = NAMES.get(RANDOM.nextInt(NAMES.size()));

        // Level 1
        final int level = 1;
        // Race

        // Gold

        // Class

        // Equipment

        // Armor Class
        final int armorClass = 10; // TODO

        // Hit points after race...
        final int hitPoints = 4; // TODO

        final Action actions = WeaponBuilder.STAFF.build(); // TODO

        Stats stats = new Stats(
                new MultipleDice(D6, D6, D6).roll(),
                new MultipleDice(D6, D6, D6).roll(),
                new MultipleDice(D6, D6, D6).roll(),
                new MultipleDice(D6, D6, D6).roll(),
                new MultipleDice(D6, D6, D6).roll(),
                new MultipleDice(D6, D6, D6).roll()
        );

        final Player player = new Player(name, level, stats, armorClass, hitPoints, actions, new FocusFireTargetSelector());

        return player;
    }
}
