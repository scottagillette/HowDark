package com.redshift.ShadowDarkCalculator.actions.items.generator;

import com.redshift.ShadowDarkCalculator.dice.MultipleDice;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

import static com.redshift.ShadowDarkCalculator.actions.items.generator.MagicScrollGenerator.*;
import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D6;
import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D8;

/**
 * Generate random magic wand.
 */

@Slf4j
public class MagicWandGenerator implements MagicItemGenerator {

    private int tier;
    private String spellName;
    private String feature;
    private final List<String> benefits = new ArrayList<>();
    private final List<String> curses = new ArrayList<>();

    public static void main(String[] args) {
        for (int i = 0; i < 1; i++) {
            final Properties properties = Properties.generate();
            final MagicWandGenerator generator = new MagicWandGenerator();
            generator.generate(properties);
        }
    }

    private static String selectFeature() {
        String feature = "";

        switch (D8.roll()) {
            case 1 -> feature = "Carved from bone";
            case 2 -> feature = "Blinking eye in handle";
            case 3 -> feature = "Sleek starmetal";
            case 4 -> feature = "Polished wood";
            case 5 -> feature = "Obsidian with ivory tips";
            case 6 -> feature = "Electrical sparks";
            case 7 -> feature = "Jagged crystal";
            case 8 -> feature = "Made of tiny skulls";
        }

        return feature;
    }
    @Override
    public void generate(Properties properties) {
        feature = selectFeature();

        switch (new MultipleDice(D6, D6).roll()) {
            case 2, 3, 4, 5:
                tier = 1;
                spellName = selectTier1Spell();
                break;
            case 6, 7:
                tier = 2;
                spellName = selectTier2Spell();
                break;
            case 8, 9:
                tier = 3;
                spellName = selectTier3Spell();
                break;
            case 10, 11:
                tier = 4;
                spellName = selectTier4Spell();
                break;
            case 12:
                tier = 5;
                spellName = selectTier5Spell();
                break;
        }

        switch (new MultipleDice(D6, D6).roll()) {
            case 2, 3, 4, 5, 6:
                MagicArmorGenerator.addArmorBenefits(benefits, properties.getBenefits());
                MagicArmorGenerator.addArmorCurses(curses, properties.getCurses());
                break;
            case 7, 8:
                MagicPotionGenerator.addPotionBenefits(benefits, properties.getBenefits());
                MagicPotionGenerator.addPotionCurses(curses, properties.getCurses());
                break;
            case 9, 10, 11:
                MagicTrinketGenerator.addTrinketBenefits(benefits, properties.getBenefits());
                MagicTrinketGenerator.addTrinketCurses(curses, properties.getCurses());
                break;
            case 12:
                MagicWeaponGenerator.addWeaponBenefits(benefits, properties.getBenefits());
                MagicWeaponGenerator.addWeaponCurses(curses, properties.getCurses());
        }

        log.info(this.toString());
    }

    @Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();

        builder.append("Magic Wand ").append(spellName).append(" [").append(tier).append("]").append("\n");
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
