package com.redshift.ShadowDarkCalculator.creatures.players;

import lombok.Getter;

@Getter
public enum Ancestry {
    DWARF("Dwarf"), // ADV on hp rolls.
    ELF("Elf"), // +1 to range or spell rolls
    GOBLIN("Goblin"), // No surprise
    HALF_ELF("Half-Elf"), // Roll 2 on talent roll, choose one
    HALF_ORC ("Half-Orc"), // +1 to melee and damage rolls.
    HALFLING("Halfling"), // Once per day turn invisible for 3 rounds
    HUMAN("Human"), // 2 Talent rolls
    KOBOLD("Kobold"); // Luck token each session or +1 spell casting

    private String displayName;

    private Ancestry(String dispalyName) {
        this.displayName = dispalyName;
    }
}
