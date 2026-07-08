package com.redshift.ShadowDarkCalculator.actions.items;

import com.redshift.ShadowDarkCalculator.actions.Action;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Supplier;

/**
 * Creates items by type name (e.g. "healing-potion") from a ItemConfig, applying
 * the item priority.
 */

public class ItemFactory {

    private static final Map<String, Supplier<Action>> REGISTRY = new LinkedHashMap<>();

    static {
        register("curative", Curative::new);
        register("healing-potion", HealingPotion::new);
    }

    private ItemFactory() {
    }

    public static Action create(ItemConfig itemConfig) {
        itemConfig.validate();

        final Supplier<Action> supplier = REGISTRY.get(normalize(itemConfig.getType()));

        if (supplier == null) {
            throw new IllegalArgumentException("Unknown item: '" + itemConfig.getType() + "'. Available: " + availableItems());
        }

        final Action item = supplier.get();
        item.setPriority(itemConfig.getPriority());
        return item;
    }

    public static Set<String> availableItems() {
        return new TreeSet<>(REGISTRY.keySet());
    }

    private static void register(String key, Supplier<Action> supplier) {
        REGISTRY.put(key, supplier);
    }

    private static String normalize(String value) {
        return value.trim().toLowerCase().replaceAll("[\\s_]+", "-");
    }
}
