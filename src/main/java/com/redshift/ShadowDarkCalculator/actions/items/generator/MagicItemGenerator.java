package com.redshift.ShadowDarkCalculator.actions.items.generator;

/**
 * A class of component that can generate a specific type of magic item.
 */

public interface MagicItemGenerator {

    /**
     * Generates a magic item with the given properties.
     */

    void generate(Properties properties);

}
