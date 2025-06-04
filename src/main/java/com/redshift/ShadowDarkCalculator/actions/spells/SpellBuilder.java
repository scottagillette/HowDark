package com.redshift.ShadowDarkCalculator.actions.spells;

import java.util.function.Supplier;

public enum SpellBuilder {
    TURN_UNDEAD(TurnUndead::new),
    CURE_WOUNDS(CureWounds::new),
    SHIELD_OF_FAITH(ShieldOfFaith::new),
    SLEEP(Sleep::new),
    MAGIC_MISSILE(MagicMissile::new),
    BURNING_HANDS(BurningHands::new);

    private final Supplier<Spell> ctor;

    SpellBuilder(Supplier<Spell> ctor) {
        this.ctor = ctor;
    }

    public Spell build() {
        return ctor.get();
    }
}

