package com.redshift.ShadowDarkCalculator.actions.items.generator;

import com.redshift.ShadowDarkCalculator.dice.DeckOfCards;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.*;

/**
 * Generate random magic potion.
 */

@Slf4j
public class MagicPotionGenerator implements MagicItemGenerator {

    private String feature;
    private final List<String> benefits = new ArrayList<>();
    private final List<String> curses = new ArrayList<>();

    public static void main(String[] args) {
        for (int i = 0; i < 1; i++) {
            final Properties properties = Properties.generate();
            final MagicItemGenerator generator = new MagicPotionGenerator();
            generator.generate(properties);
        }
    }

    public static void addPotionBenefits(List<String> benefits, int number) {
        final DeckOfCards cards = new DeckOfCards(12);

        String immuneToDamage = "";

        switch (D4.roll()) {
            case 1 -> immuneToDamage = "Immune 5 rounds to fire.";
            case 2 -> immuneToDamage = "Immune 5 rounds to cold.";
            case 3 -> immuneToDamage = "Immune 5 rounds to lightning.";
            case 4 -> immuneToDamage = "Immune 5 rounds to poison.";
        }

        String healingPotion = "";

        switch (D4.roll()) {
            case 1 -> healingPotion = "Heals 1d4 hit points.";
            case 2 -> healingPotion = "Heals 2d6 hit points.";
            case 3 -> healingPotion = "Heals 3d8 hit points.";
            case 4 -> healingPotion = "Heals 4d10 hit points.";
        }

        for (int i = 0; i < number; i++) {
            switch (cards.draw()) {
                case 1 -> benefits.add(immuneToDamage);
                case 2 -> benefits.add(healingPotion);
                case 3 -> benefits.add("Read the minds of all creatures within near for 1 hour.");
                case 4 -> benefits.add("Fly a near distance for 5 rounds.");
                case 5 -> benefits.add("For 5 rounds, move far on your turn and still take an action.");
                case 6 -> benefits.add("Become invisible for 5 rounds.");
                case 7 -> benefits.add("Breathe underwater and know Merran language for 1 hour.");
                case 8 -> benefits.add("A stat becomes 18 (+4) for 5 rounds.");
                case 9 -> benefits.add("Turn into purple, flying gas for 5 rounds.");
                case 10 -> benefits.add("Cures any disease or affliction affecting drinker.");
                case 11 -> benefits.add("Speak to and understand animals for 1 hour.");
                case 12 -> benefits.add("You are immune to all damage for 5 rounds.");
            }
        }
    }

    public static void addPotionCurses(List<String> curses, int number) {
        final DeckOfCards cards = new DeckOfCards(12);

        final String petrify = (D4.roll() > 2) ?
                "Your legs petrify for 5 rounds.":
                "Your arms petrify for 5 rounds.";

        for (int i = 0; i < number; i++) {
            switch (cards.draw()) {
                case 1 -> curses.add("DC 15 WIS check or attack nearest creature for 3 rounds.");
                case 2 -> curses.add("Turn into a 1 HP newt for 3 rounds.");
                case 3 -> curses.add("A stat becomes 3 (-4) for 1 hour.");
                case 4 -> curses.add("DC 15 CON check or take 2d10 damage.");
                case 5 -> curses.add("Forget all languages you know for 1 hour.");
                case 6 -> curses.add("Shrink to half size and disadvantage on attacks for 5 rounds.");
                case 7 -> curses.add("Sing at the top of your lungs for 3 rounds.");
                case 8 -> curses.add("You become magnetic to all metal near to you for 1 hour.");
                case 9 -> curses.add("You are compelled to jump into any pits you see for 1 hour.");
                case 10 -> curses.add("DC 15 CON check or go blind for 5 rounds.");
                case 11 -> curses.add("You are the source of an anti-magic shell spell for 1 hour.");
                case 12 -> curses.add(petrify);            }
        }
    }

    private static String selectFeature() {
        String feature1 = "";

        switch (D8.roll()) {
            case 1 -> feature1 = "Spicy";
            case 2 -> feature1 = "Clear as water";
            case 3 -> feature1 = "Deep blue";
            case 4 -> feature1 = "Citrus smell";
            case 5 -> feature1 = "Sulfurous";
            case 6 -> feature1 = "Fizzy";
            case 7 -> feature1 = "Chilly";
            case 8 -> feature1 = "Blood red";
        }

        String feature2 = "";

        switch (D8.roll()) {
            case 1 -> feature2 = "Pickled spider inside";
            case 2 -> feature2 = "Green fumes";
            case 3 -> feature2 = "Tiny stars and moon";
            case 4 -> feature2 = "Gold flakes in liquid";
            case 5 -> feature2 = "Swirling vortex";
            case 6 -> feature2 = "Quiet whistling";
            case 7 -> feature2 = "Rattles and shakes";
            case 8 -> feature2 = "Eyeball inside";
        }

        String feature3 = "";

        switch (D8.roll()) {
            case 1 -> feature3 = "Bubbling";
            case 2 -> feature3 = "Purple streaks";
            case 3 -> feature3 = "Flames on surface";
            case 4 -> feature3 = "Floral smell";
            case 5 -> feature3 = "Skull on bottle";
            case 6 -> feature3 = "Warm";
            case 7 -> feature3 = "Large molar inside";
            case 8 -> feature3 = "Pink starbursts";
        }

        return feature1 + ", " + feature2 + ", " + feature3;
    }

    @Override
    public void generate(Properties properties) {
        feature = selectFeature();

        addPotionBenefits(benefits, properties.getBenefits());

        addPotionCurses(curses, properties.getCurses());

        log.info(this.toString());
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();

        builder.append("Magic Potion\n");
        builder.append("Feature: ").append(feature).append("\n");

        for (String benefit : benefits) {
            builder.append("Benefit: ").append(benefit).append("\n");
        }
        for (String curse : curses) {
            builder.append("Curse: ").append(curse).append("\n");
        }

        return builder.toString();
    }
}
