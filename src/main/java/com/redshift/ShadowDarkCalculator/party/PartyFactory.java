package com.redshift.ShadowDarkCalculator.party;

import com.redshift.ShadowDarkCalculator.creatures.Creature;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Supplier;

/**
 * Creates a full, hand-tuned party by name (e.g. "lost-citadel"), so a fight can reuse an
 * existing PartyBuilder instead of listing members individually. Unlike listing players by
 * class, these carry their exact stats, armor and loadout.
 */

public final class PartyFactory {

    private static final Map<String, Supplier<PartyBuilder>> REGISTRY = new LinkedHashMap<>();

    static {
        register("lost-citadel", LostCitadelPartyBuilder::new);
        register("the-wolf-pack", TheWolfPackBuilder::new);
        register("the-crab-crushers", TheCrabCrushersBuilder::new);
        register("diablerie", DiableriePartyBuilder::new);
        register("funnel", FunnelPartyBuilder::new);
    }

    private PartyFactory() {
    }

    public static Set<String> availableParties() {
        return new TreeSet<>(REGISTRY.keySet());
    }

    public static List<Creature> create(String name) {
        final Supplier<PartyBuilder> builder = REGISTRY.get(normalize(name));

        if (builder == null) {
            throw new IllegalArgumentException("Unknown party: '" + name + "'. Available: " + availableParties());
        }

        return builder.get().build();
    }

    private static String normalize(String value) {
        return value.trim().toLowerCase().replaceAll("[\\s_]+", "-");
    }

    private static void register(String key, Supplier<PartyBuilder> builder) {
        REGISTRY.put(key, builder);
    }

}
