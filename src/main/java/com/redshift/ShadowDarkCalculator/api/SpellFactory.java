package com.redshift.ShadowDarkCalculator.api;

import com.redshift.ShadowDarkCalculator.actions.spells.BurningHands;
import com.redshift.ShadowDarkCalculator.actions.spells.CureWounds;
import com.redshift.ShadowDarkCalculator.actions.spells.Eyebite;
import com.redshift.ShadowDarkCalculator.actions.spells.HolyWeapon;
import com.redshift.ShadowDarkCalculator.actions.spells.Hypnotize;
import com.redshift.ShadowDarkCalculator.actions.spells.MageArmor;
import com.redshift.ShadowDarkCalculator.actions.spells.MagicMissile;
import com.redshift.ShadowDarkCalculator.actions.spells.ProtectionFromEvil;
import com.redshift.ShadowDarkCalculator.actions.spells.ShieldOfFaith;
import com.redshift.ShadowDarkCalculator.actions.spells.Sleep;
import com.redshift.ShadowDarkCalculator.actions.spells.Spell;
import com.redshift.ShadowDarkCalculator.actions.spells.TurnUndead;
import com.redshift.ShadowDarkCalculator.actions.spells.Undeath;
import com.redshift.ShadowDarkCalculator.actions.spells.Withermark;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Supplier;

/**
 * Creates spells by type name (e.g. "cure-wounds") from a SpellConfig, applying the spell
 * check bonus, advantage and the action priority.
 */

public final class SpellFactory {

    private static final Map<String, Supplier<Spell>> REGISTRY = new LinkedHashMap<>();

    static {
        register("burning-hands", BurningHands::new);
        register("cure-wounds", CureWounds::new);
        register("eyebite", Eyebite::new);
        register("holy-weapon", HolyWeapon::new);
        register("hypnotize", Hypnotize::new);
        register("mage-armor", MageArmor::new);
        register("magic-missile", MagicMissile::new);
        register("protection-from-evil", ProtectionFromEvil::new);
        register("shield-of-faith", ShieldOfFaith::new);
        register("sleep", Sleep::new);
        register("turn-undead", TurnUndead::new);
        register("undeath", Undeath::new);
        register("withermark", Withermark::new);
    }

    private SpellFactory() {
    }

    public static Spell create(SpellConfig config) {
        config.validate();

        final Supplier<Spell> supplier = REGISTRY.get(normalize(config.getType()));

        if (supplier == null) {
            throw new IllegalArgumentException(
                    "Unknown spell: '" + config.getType() + "'. Available: " + availableSpells());
        }

        final Spell spell = supplier.get();

        if (config.isAdvantage()) {
            spell.addAdvantage();
        }
        if (config.getSpellCheckBonus() != 0) {
            spell.addSpellCheckBonus(config.getSpellCheckBonus());
        }

        spell.setPriority(config.getPriority());

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
