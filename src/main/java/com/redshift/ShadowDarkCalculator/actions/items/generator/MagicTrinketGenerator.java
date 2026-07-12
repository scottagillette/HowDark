package com.redshift.ShadowDarkCalculator.actions.items.generator;

import com.redshift.ShadowDarkCalculator.dice.DeckOfCards;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.*;

/**
 * Generate random magic trinket.
 */

@Slf4j
public class MagicTrinketGenerator implements MagicItemGenerator {

    private String type;
    private String feature;
    private final List<String> benefits = new ArrayList<>();
    private final List<String> curses = new ArrayList<>();

    public static void main(String[] args) {
        for (int i = 0; i < 1; i++) {
            final Properties properties = Properties.generate();
            final MagicItemGenerator generator = new MagicTrinketGenerator();
            generator.generate(properties);
        }
    }

    public static void addTrinketBenefits(List<String> benefits, int number) {
        final DeckOfCards cards = new DeckOfCards(12);

        String damageImmunity = "";

        switch (D4.roll()) {
            case 1 -> damageImmunity = "You're immune to fire.";
            case 2 -> damageImmunity = "You're immune to cold.";
            case 3 -> damageImmunity = "You're immune to lightning.";
            case 4 -> damageImmunity = "You're immune to poison.";
        }

        for (int i = 0; i < number; i++) {
            switch (cards.draw()) {
                case 1 -> benefits.add("You can't be magically scryed upon or detected.");
                case 2 -> benefits.add("Connects to an interdimensional pocket with 5 gear slots.");
                case 3 -> benefits.add("A stat becomes 18 (+4) while using/wearing item.");
                case 4 -> benefits.add("Once per day, teleport a near distance.");
                case 5 -> benefits.add("Harmful spells that target you are DC 15 to cast.");
                case 6 -> benefits.add(damageImmunity);
                case 7 -> benefits.add("Sense secret doors when they're within close range.");
                case 8 -> benefits.add("You can see invisible and incorporeal creatures.");
                case 9 -> benefits.add("Your movement isn't hindered by any terrain.");
                case 10 -> benefits.add("You can hold your breath for 1 hour.");
                case 11 -> benefits.add("You do not need to eat or drink to survive.");
                case 12 -> benefits.add("You can walk on non-solid surfaces for 2 rounds at a time.");
            }
        }
    }

    public static void addTrinketCurses(List<String> curses, int number) {
        final DeckOfCards cards = new DeckOfCards(12);

        for (int i = 0; i < number; i++) {
            switch (cards.draw()) {
                case 1 -> curses.add("Slowly rots all other non-magical items that touch it.");
                case 2 -> curses.add("Deals 1d4 damage and leaves blisters whenever used.");
                case 3 -> curses.add("Item attracts bad weather to its location.");
                case 4 -> curses.add("You cannot be healed by magic; only by resting.");
                case 5 -> curses.add("Crashes like a gong whenever wielder slays a creature.");
                case 6 -> curses.add("Item attracts all undead within a far distance.");
                case 7 -> curses.add("Temporarily loses magic if doused in water.");
                case 8 -> curses.add("You have disadvantage on CON checks.");
                case 9 -> curses.add("You are compelled to light parchment objects on fire.");
                case 10 -> curses.add("You must drink blood once a day or take 1d8 damage.");
                case 11 -> curses.add("Item must eat 1d10 gp a day or it loses its magic until fed.");
                case 12 -> curses.add("Item has horrid smell that makes all your CHA checks hard.");
            }
        }
    }

    private static String selectFeature() {
        String feature = "";

        switch (D20.roll()) {
            case 1 -> feature = "Shaped like a raven";
            case 2 -> feature = "Iridescent";
            case 3 -> feature = "Cruel spikes and spines";
            case 4 -> feature = "Made from a big frog";
            case 5 -> feature = "Gem-studded";
            case 6 -> feature = "Gold thread/hardware";
            case 7 -> feature = "Made of basilisk hide";
            case 8 -> feature = "Possessed by a spirit";
            case 9 -> feature = "Made of shaped smoke";
            case 10 -> feature = "Covered in small thorns";
            case 11 -> feature = "Made with rare feathers";
            case 12 -> feature = "Has tiny wings";
            case 13 -> feature = "Slowly changes colors";
            case 14 -> feature = "Shaped like a bat";
            case 15 -> feature = "Tarnished silver hardware";
            case 16 -> feature = "Made of spidersilk";
            case 17 -> feature = "Hums quiet, sweet tones";
            case 18 -> feature = "Jolt of pain at first touch";
            case 19 -> feature = "Throbs like a heart";
            case 20 -> feature = "Trails faint mist";
        }

        return feature;
    }

    @Override
    public void generate(Properties properties) {
        feature = selectFeature();

        switch (D20.roll()) {
            case 1 -> type = "Brooch";
            case 2 -> type = "Ring";
            case 3 -> type = "Boots";
            case 4 -> type = "Cloak";
            case 5 -> type = "Amulet";
            case 6 -> type = "Flask";
            case 7 -> type = "Tome";
            case 8 -> type = "Circlet";
            case 9 -> type = "Eyepatch";
            case 10 -> type = "Gauntlets";
            case 11 -> type = "Holy symbol";
            case 12 -> type = "Hat";
            case 13 -> type = "Goblet";
            case 14 -> type = "Helm";
            case 15 -> type = "Statuette";
            case 16 -> type = "Goggles";
            case 17 -> type = "Bag";
            case 18 -> type = "Rock";
            case 19 -> type = "Surcoat";
            case 20 -> type = "Mask";
        }

        addTrinketBenefits(benefits, properties.getBenefits());

        addTrinketCurses(curses, properties.getCurses());

        log.info(this.toString());
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();

        builder.append("Magic ").append(type).append("\n");
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
