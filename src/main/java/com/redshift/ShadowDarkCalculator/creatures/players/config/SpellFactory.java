package com.redshift.ShadowDarkCalculator.creatures.players.config;

import com.redshift.ShadowDarkCalculator.actions.spells.*;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Supplier;

/**
 * Creates spells by type name (e.g. "cure-wounds") from a SpellConfig, applying
 * the spell check bonus, advantage and the action priority.
 */

public final class SpellFactory {

    private static final Map<String, Supplier<Spell>> REGISTRY = new LinkedHashMap<>();

    static {
        register("burning-hands", BurningHands::new);
        register("cure-wounds", CureWounds::new);
        register("eyebite", Eyebite::new);
        register("fascinate", Fascinate::new);
        register("holy-weapon", HolyWeapon::new);
        register("hypnotize", Hypnotize::new);
        register("mage-armor", MageArmor::new);
        register("magic-missile", MagicMissile::new);
        register("protection-from-evil", ProtectionFromEvil::new);
        register("shield-of-faith", ShieldOfFaith::new);
        register("sleep", Sleep::new);
        register("turn-undead", TurnUndead::new);
        register("undeath", Undeath::new);
        register("willowman", Willowman::new);
        register("witchlight", Witchlight::new);
        register("withermark", Withermark::new);
    }

    private SpellFactory() {
    }

    public static Spell create(SpellConfig spellConfig) {
        spellConfig.validate();

        final Supplier<Spell> supplier = REGISTRY.get(normalize(spellConfig.getType()));

        if (supplier == null) {
            throw new IllegalArgumentException("Unknown spell: '" + spellConfig.getType() + "'. Available: " + availableSpells());
        }

        final Spell spell = supplier.get();

        if (spellConfig.isAdvantage()) spell.addAdvantage();
        if (spellConfig.getSpellCheckBonus() != 0) spell.addSpellCheckBonus(spellConfig.getSpellCheckBonus());

        spell.setPriority(spellConfig.getPriority());

        return spell;
    }

    public static Set<String> availableSpells() {
        return new TreeSet<>(REGISTRY.keySet());
    }

    private static void register(String key, Supplier<Spell> supplier) {
        REGISTRY.put(key, supplier);
    }

    private static String normalize(String value) {
        return value.trim().toLowerCase().replaceAll("[\\s_]+", "-");
    }

}
