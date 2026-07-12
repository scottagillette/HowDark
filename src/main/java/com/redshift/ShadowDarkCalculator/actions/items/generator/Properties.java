package com.redshift.ShadowDarkCalculator.actions.items.generator;

import com.redshift.ShadowDarkCalculator.dice.MultipleDice;
import lombok.Data;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D6;

/**
 * Randomly generate magic item properties; benefits, curses, virtues and flaws.
 */

@Data
public class Properties {

    private int benefits;
    private int curses;
    private int virtues;
    private int flaws;

    /**
     * Generate randomly magic item properties.
     */

    public static Properties generate() {
        final Properties properties = new Properties();

        switch (new MultipleDice(D6, D6).roll()) {
            case 2, 3:
                properties.setCurses(1);
                break;
            case 4, 5, 6, 7:
                properties.setBenefits(1);
                properties.setCurses(1);
                break;
            case 8, 9, 10, 11:
                properties.setBenefits(1);
                break;
            case 12:
                properties.setBenefits(2);
                break;
        }

        switch (new MultipleDice(D6, D6).roll()) {
            case 2, 3:
                properties.setFlaws(1);
                break;
            case 4, 5, 6, 7, 8, 9:
                break;
            case 10, 11:
                properties.setVirtues(1);
                properties.setFlaws(1);
                break;
            case 12:
                properties.setBenefits(1);
                break;
        }

        return properties;
    }

}
