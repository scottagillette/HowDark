package com.redshift.ShadowDarkCalculator.actions;

import lombok.Getter;
import lombok.Setter;

/**
 * List various types of damage that are used in game effects.
 */

@Getter
//@Setter
public class DamageType {

    private boolean silvered = false;

    private boolean magical = false;

    private boolean acid = false;
    private boolean cold = false;
    private boolean fire = false;
    private boolean poison = false;
    private boolean lightning = false;

    // Weapon types
    private boolean slashing = false;
    private boolean piercing = false;
    private boolean crushing = false;


    public DamageType addAcid() {
        acid = true;
        return this;
    }

    public DamageType addCold() {
        cold = true;
        return this;
    }

    public DamageType addCrushing() {
        crushing = true;
        return this;
    }

    public DamageType addFire() {
        fire = true;
        return this;
    }

    public DamageType addLightning() {
        lightning = true;
        return this;
    }

    public DamageType addMagical() {
        magical = true;
        return this;
    }

    public DamageType addPiercing() {
        piercing = true;
        return this;
    }

    public DamageType addPoison() {
        poison = true;
        return this;
    }

    public DamageType addSilvered() {
        silvered = true;
        return this;
    }

    public DamageType addSlashing() {
        slashing = true;
        return this;
    }
}
