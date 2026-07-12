package com.redshift.ShadowDarkCalculator.actions.items.generator;

import com.redshift.ShadowDarkCalculator.dice.MultipleDice;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D12;
import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D6;

/**
 * Generate random magic scroll.
 */

@Slf4j
public class MagicScrollGenerator implements MagicItemGenerator {

    private int tier;
    private String spellName;
    private final List<String> benefits = new ArrayList<>();
    private final List<String> curses = new ArrayList<>();

    public static void main(String[] args) {
        for (int i = 0; i < 1; i++) {
            final Properties properties = Properties.generate();
            final MagicScrollGenerator generator = new MagicScrollGenerator();
            generator.generate(properties);
        }
    }

    public static String selectTier1Spell() {
        switch (D12.roll()) {
            case 1 -> { return "Alarm"; }
            case 2 -> { return "Burning hands"; }
            case 3 -> { return "Charm person"; }
            case 4 -> { return "Detect magic"; }
            case 5 -> { return "Feather fall"; }
            case 6 -> { return "Floating disk"; }
            case 7 -> { return "Hold portal"; }
            case 8 -> { return "Light"; }
            case 9 -> { return "Mage armor"; }
            case 10 -> { return "Magic missile"; }
            case 11 -> { return "Protection from evil"; }
            case 12 -> { return "Sleep"; }
        }
        throw new IllegalStateException("No spell selected!");
    }

    public static String selectTier2Spell() {
        switch (D12.roll()) {
            case 1 -> { return "Acid arrow"; }
            case 2 -> { return "Alter self"; }
            case 3 -> { return "Detect thoughts"; }
            case 4 -> { return "Fixed object"; }
            case 5 -> { return "Hold person"; }
            case 6 -> { return "Invisibility"; }
            case 7 -> { return "Knock"; }
            case 8 -> { return "Levitate"; }
            case 9 -> { return "Mirror image"; }
            case 10 -> { return "Misty step"; }
            case 11 -> { return "Silence"; }
            case 12 -> { return "Web"; }
        }
        throw new IllegalStateException("No spell selected!");
    }

    public static String selectTier3Spell() {
        switch (D12.roll()) {
            case 1 -> { return "Animate dead"; }
            case 2 -> { return "Dispel magic"; }
            case 3 -> { return "Fabricate"; }
            case 4 -> { return "Fireball"; }
            case 5 -> { return "Fly"; }
            case 6 -> { return "Gaseous form"; }
            case 7 -> { return "Illusion"; }
            case 8 -> { return "Lightning bolt"; }
            case 9 -> { return "Magic circle"; }
            case 10 -> { return "Protection from energy"; }
            case 11 -> { return "Sending"; }
            case 12 -> { return "Speak with dead"; }
        }
        throw new IllegalStateException("No spell selected!");
    }

    public static String selectTier4Spell() {
        switch (D12.roll()) {
            case 1 -> { return "Arcane eye"; }
            case 2 -> { return "Cloudkill"; }
            case 3 -> { return "Confusion"; }
            case 4 -> { return "Control water"; }
            case 5 -> { return "Dimension door"; }
            case 6 -> { return "Divination"; }
            case 7 -> { return "Passwall"; }
            case 8 -> { return "Polymorph"; }
            case 9 -> { return "Resilient sphere"; }
            case 10 -> { return "Stoneskin"; }
            case 11 -> { return "Telekinesis"; }
            case 12 -> { return "Wall of force"; }
        }
        throw new IllegalStateException("No spell selected!");
    }

    public static String selectTier5Spell() {
        switch (D12.roll()) {
            case 1 -> { return "Antimagic shell"; }
            case 2 -> { return "Create undead"; }
            case 3 -> { return "Disintegrate"; }
            case 4 -> { return "Hold monster"; }
            case 5 -> { return "Plane shift"; }
            case 6 -> { return "Power word kill"; }
            case 7 -> { return "Prismatic orb"; }
            case 8 -> { return "Scrying"; }
            case 9 -> { return "Shapechange"; }
            case 10 -> { return "Summon extraplanar"; }
            case 11 -> { return "Teleport"; }
            case 12 -> { return "Wish"; }
        }
        throw new IllegalStateException("No spell selected!");
    }

    @Override
    public void generate(Properties properties) {
        // TODO: Features

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

        builder.append("Magic Scroll ").append(spellName).append(" [").append(tier).append("]").append("\n");

        for (String benefit : benefits) {
            builder.append("Befit: ").append(benefit).append("\n");
        }
        for (String curse : curses) {
            builder.append("Curse: ").append(curse).append("\n");
        }

        return builder.toString();
    }
}
