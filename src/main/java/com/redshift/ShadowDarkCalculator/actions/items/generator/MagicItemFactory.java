package com.redshift.ShadowDarkCalculator.actions.items.generator;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D6;

/**
 * A factory to create a random magic item.
 */

public class MagicItemFactory {

    public static void main(String[] args) {
        for (int i = 0; i < 1; i++) {
            final MagicItemFactory factory = new MagicItemFactory();
            factory.create();
        }
    }

    /**
     * Create a random magic item.
     */

    public void create() {
        final Properties properties = Properties.generate();

        // 1 Armor 2 Potion 3 Scroll 4 Utility 5 Wand 6 Weapon

        switch (D6.roll()) {
            case 1 -> new MagicArmorGenerator().generate(properties);
            case 2 -> new MagicPotionGenerator().generate(properties);
            case 3 -> new MagicScrollGenerator().generate(properties);
            case 4 -> new MagicTrinketGenerator().generate(properties);
            case 5 -> new MagicWandGenerator().generate(properties);
            case 6 -> new MagicWeaponGenerator().generate(properties);
        }
    }

}
