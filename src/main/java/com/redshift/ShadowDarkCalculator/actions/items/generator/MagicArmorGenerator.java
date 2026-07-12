package com.redshift.ShadowDarkCalculator.actions.items.generator;

import com.redshift.ShadowDarkCalculator.dice.DeckOfCards;
import com.redshift.ShadowDarkCalculator.dice.MultipleDice;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D20;
import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D6;

/**
 * Generate random magic armor.
 */

@Slf4j
public class MagicArmorGenerator implements MagicItemGenerator {

    private String type;
    private boolean mithral;
    private int bonus;
    private String feature;
    private final List<String> benefits = new ArrayList<>();
    private final List<String> curses = new ArrayList<>();

    public static void main(String[] args) {
        for (int i = 0; i < 1; i++) {
            final Properties properties = Properties.generate();
            final MagicArmorGenerator generator = new MagicArmorGenerator();
            generator.generate(properties);
        }
    }

    public static void addArmorBenefits(List<String> benefits, int number) {
        final DeckOfCards cards = new DeckOfCards(12);

        for (int i = 0; i < number; i++) {
            switch (cards.draw()) {
                case 1 -> benefits.add("Once per day, deflect a ranged attack that would hit you.");
                case 2 -> benefits.add("Checks to stabilize you are easy (DC 9).");
                case 3 -> benefits.add("You cannot be knocked over while you are conscious.");
                case 4 -> benefits.add("Undetected creatures do not have advantage to attack you.");
                case 5 -> benefits.add("You know Diabolic and are immune to fire, lava, and magma.");
                case 6 -> benefits.add("You are immune to the curses of one item you choose.");
                case 7 -> benefits.add("Once per day, gain advantage on all attacks for 3 rounds.");
                case 8 -> benefits.add("You have a +4 bonus to your death timers.");
                case 9 -> benefits.add("Gain immunity to a poison after suffering its effects once.");
                case 10 -> benefits.add("You know Celestial and can fly for 3 rounds once per day.");
                case 11 -> benefits.add("Treat critical hits against you as normal hits.");
                case 12 -> benefits.add("Ignore any damage dealt to you of 3 points or below.");
            }
        }
    }

    public static void addArmorCurses(List<String> curses, int number) {
        final DeckOfCards cards = new DeckOfCards(12);

        for (int i = 0; i < number; i++) {
            switch (cards.draw()) {
                case 1 -> curses.add("You take 2d10 damage if you remove this armor.");
                case 2 -> curses.add("Your party cannot add CHA bonuses to reaction checks.");
                case 3 -> curses.add("Mounts fear you and will not allow you to ride them.");
                case 4 -> curses.add("DC 15 WIS first round of combat or attack nearest creature.");
                case 5 -> curses.add("You take double damage from blunt/bludgeoning weapons.");
                case 6 -> curses.add("Armor uses 5 gear slots and is extremely loud and clunky.");
                case 7 -> curses.add("Ranged attacks against you have advantage.");
                case 8 -> curses.add("Treat a natural 1 attack roll against you as a critical hit.");
                case 9 -> curses.add("Beneficial spells that target you are hard to cast (DC 15).");
                case 10 -> curses.add("You have disadvantage on Dexterity checks.");
                case 11 -> curses.add("There's a secret 1-in-6 chance each NPC ally will betray you.");
                case 12 -> curses.add("You take double damage from silvered weapons.");
            }
        }
    }

    private static String selectFeature() {
        String feature = "";

        switch (D20.roll()) {
            case 1 -> feature = "Demonic horned face";
            case 2 -> feature = "Oak leaf motif";
            case 3 -> feature = "Studded with shark teeth";
            case 4 -> feature = "Dragon scales";
            case 5 -> feature = "Bone or metal spikes";
            case 6 -> feature = "Faint arcane runes";
            case 7 -> feature = "Turtle shell plating";
            case 8 -> feature = "Made of scorpion chitin";
            case 9 -> feature = "Gilded metal/gold thread";
            case 10 -> feature = "Scorched, smells burned";
            case 11 -> feature = "Pearl-white fish scales";
            case 12 -> feature = "Oozes blood";
            case 13 -> feature = "Festooned with fungi";
            case 14 -> feature = "Distant sound of ocean";
            case 15 -> feature = "Set with crystals";
            case 16 -> feature = "Draped in holy symbols";
            case 17 -> feature = "Exudes tree sap";
            case 18 -> feature = "Blurry, indistinct edges";
            case 19 -> feature = "Large golden cat eye";
            case 20 -> feature = "Covered in frost";
        }

        return feature;
    }

    @Override
    public void generate(Properties properties) {
        feature = selectFeature();

        switch (new MultipleDice(D6, D6).roll()) {
            case 2, 3, 4, 5 -> type = "Leather";
            case 6, 7 -> type = "Chainmail";
            case 8, 9 -> type = "Shield";
            case 10, 11 -> type = "Plate mail";
            case 12 -> mithral = true;
        }

        if (type == null) {
            switch (D6.roll()) {
                case 1, 2 -> type = "Chainmail";
                case 3, 4 -> type = "Shield";
                case 5, 6 -> type = "Plate mail";
            }
        }

        switch (new MultipleDice(D6, D6).roll()) {
            case 2, 3, 4, 5 -> bonus = 0;
            case 6, 7, 8 -> bonus = 1;
            case 9, 10, 11 -> bonus = 2;
            case 12 -> bonus = 3;
        }

        addArmorBenefits(benefits, properties.getBenefits());

        addArmorCurses(curses, properties.getCurses());

        log.info(this.toString());
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();

        builder.append("Magic +").append(bonus);
        if (mithral) builder.append(" Mithral");
        builder.append(" ").append(type).append("\n");

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
