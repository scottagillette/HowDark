package com.redshift.ShadowDarkCalculator.actions.items.generator;

import com.redshift.ShadowDarkCalculator.dice.DeckOfCards;
import com.redshift.ShadowDarkCalculator.dice.MultipleDice;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.*;

/**
 * Generate random magic weapon.
 */

@Slf4j
public class MagicWeaponGenerator implements MagicItemGenerator {

    private String type;
    private int bonus;
    private String feature;
    private final List<String> benefits = new ArrayList<>();
    private final List<String> curses = new ArrayList<>();

    public static void main(String[] args) {
        for (int i = 0; i < 1; i++) {
            final Properties properties = Properties.generate();
            final MagicItemGenerator generator = new MagicWeaponGenerator();
            generator.generate(properties);
        }
    }

    public static void addWeaponBenefits(List<String> benefits, int number) {
        final DeckOfCards cards = new DeckOfCards(12);

        String doubleDamage = "";

        switch (D4.roll()) {
            case 1, 2 -> doubleDamage = "Double damage to undead.";
            case 3 -> doubleDamage = "Double damage to demons.";
            case 4 -> doubleDamage = "Double damage to dragons.";
        }

        for (int i = 0; i < number; i++) {
            switch (cards.draw()) {
                case 1 -> benefits.add("Cut or smash through any material.");
                case 2 -> benefits.add("Once per day, ignites for 5 rounds, deals 1d4 extra damage.");
                case 3 -> benefits.add("DC 15 CHA check to command a wild animal within far.");
                case 4 -> benefits.add("Behead the enemy on a critical hit.");
                case 5 -> benefits.add("When you hit a creature, learn its True Name.");
                case 6 -> benefits.add("Shoot a bolt of energy near with DEX, 1d6 damage.");
                case 7 -> benefits.add("Once per day, deflect a melee attack that would hit you.");
                case 8 -> benefits.add("Regain 1d6 hit points when you slay a creature.");
                case 9 -> benefits.add("You have advantage on initiative rolls.");
                case 10 -> benefits.add("Has thrown property, near distance, returns to you.");
                case 11 -> benefits.add(doubleDamage);
                case 12 -> benefits.add("Re-roll natural 1s once each when attacking with this weapon.");
            }
        }
    }

    public static void addWeaponCurses(List<String> curses, int number) {
        final DeckOfCards cards = new DeckOfCards(12);

        String cantSee = "";

        switch (D4.roll()) {
            case 1 -> cantSee = "You can't see undead.";
            case 2 -> cantSee = "You can't see demons.";
            case 3 -> cantSee = "You can't see snakes.";
            case 4 -> cantSee = "You can't see spiders.";
        }

        for (int i = 0; i < number; i++) {
            switch (cards.draw()) {
                case 1 -> curses.add(cantSee);
                case 2 -> curses.add("You are compelled to swallow all gemstones at first sight.");
                case 3 -> curses.add("Burn a straw doll daily or item temporarily loses magic.");
                case 4 -> curses.add("Any light source you hold immediately extinguishes.");
                case 5 -> curses.add("You must loudly praise a god whenever you see its symbol.");
                case 6 -> curses.add("Venomous creatures always target you with attacks.");
                case 7 -> curses.add("You turn into a rat every day at midnight for one hour.");
                case 8 -> curses.add("Your checks to swim are always extreme (DC 18).");
                case 9 -> curses.add("You are burned by the touch of gold.");
                case 10 -> curses.add("Bathe item in blood daily or it temporarily loses its magic.");
                case 11 -> curses.add("You cannot wear armor while wielding this item.");
                case 12 -> curses.add("Weapon can possess you by winning contested CHA (+2).");
            }
        }
    }

    private static String selectFeature() {
        String feature = "";

        switch (D20.roll()) {
            case 1 -> feature = "Trails sparkles";
            case 2 -> feature = "Starmetal";
            case 3 -> feature = "Rusted and chipped";
            case 4 -> feature = "Gem in pommel/handle";
            case 5 -> feature = "Drips green ichor";
            case 6 -> feature = "Moon motif and silvered";
            case 7 -> feature = "Galaxies swirl on surface";
            case 8 -> feature = "Ironwood";
            case 9 -> feature = "Rune-scribed";
            case 10 -> feature = "Faint, ghostly aura";
            case 11 -> feature = "Inlaid with gold";
            case 12 -> feature = "Trails incense";
            case 13 -> feature = "Studded with gemstones";
            case 14 -> feature = "Sparks dance on surface";
            case 15 -> feature = "Shaped like an animal";
            case 16 -> feature = "Carved from granite";
            case 17 -> feature = "Dragonbone hardware";
            case 18 -> feature = "Whispers in a language";
            case 19 -> feature = "Drips ocean water";
            case 20 -> feature = "Turns blood to rose petals";
        }

        return feature;
    }

    @Override
    public void generate(Properties properties) {
        feature = selectFeature();

        switch (D20.roll()) {
            case 1 -> type = "Arrows";
            case 2, 3 -> type = "Bastard Sword";
            case 4 -> type = "Club";
            case 5 -> type = "Crossbow";
            case 6 -> type = "Crossbow Bolts";
            case 7, 8 -> type = "Dagger";
            case 9 -> type = "Greataxe";
            case 10 -> type = "Greatsword";
            case 11 -> type = "Javelin";
            case 12 -> type = "Longbow";
            case 13, 14 -> type = "Longsword";
            case 15 -> type = "Mace";
            case 16 -> type = "Shortbow";
            case 17, 18 -> type = "Shortsword";
            case 19 -> type = "Staff";
            case 20 -> type = "Warhammer";
        }

        switch (new MultipleDice(D6, D6).roll()) {
            case 2, 3 -> bonus = 0;
            case 4, 5, 6, 7, 8, 9 -> bonus = 1;
            case 10, 11 -> bonus = 2;
            case 12 -> bonus = 3;
        }

        addWeaponBenefits(benefits, properties.getBenefits());

        addWeaponCurses(curses, properties.getCurses());

        log.info(this.toString());
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();

        builder.append("Magic +").append(bonus).append(" ").append(type).append("\n");
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
