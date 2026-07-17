package com.redshift.ShadowDarkCalculator.party.generator.ancestry;

import com.redshift.ShadowDarkCalculator.creatures.Stats;
import com.redshift.ShadowDarkCalculator.creatures.players.Ancestry;
import com.redshift.ShadowDarkCalculator.creatures.players.PlayerClass;
import com.redshift.ShadowDarkCalculator.dice.Dice;
import com.redshift.ShadowDarkCalculator.dice.SingleDie;
import com.redshift.ShadowDarkCalculator.party.generator.Bonuses;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AncestrySelector {

    private final Map<Ancestry, AncestryDecorator> ancestryDecorators = new HashMap<>();

    public AncestrySelector() {
        ancestryDecorators.put(Ancestry.DWARF, new DwarfAncestryDecorator());
        ancestryDecorators.put(Ancestry.ELF, new ElfAncestryDecorator());
        ancestryDecorators.put(Ancestry.GOBLIN, new GoblinAncestryDecorator());
        ancestryDecorators.put(Ancestry.HALF_ELF, new HalfElfAncestryDecorator());
        ancestryDecorators.put(Ancestry.HALF_ORC, new HalfOrcAncestryDecorator());
        ancestryDecorators.put(Ancestry.HALFLING, new HalflingAncestryDecorator());
        ancestryDecorators.put(Ancestry.HUMAN, new HumanAncestryDecorator());
        ancestryDecorators.put(Ancestry.KOBOLD, new KoboldAncestryDecorator());
    }

    /**
     * Given a specified class, select an ancestry and apply any bonuses.
     */

    public Ancestry select(PlayerClass playerClass, Stats stats, Bonuses bonuses) {
        final List<Ancestry> possibleAncestries;

        // Dwarf - ADV on hp rolls.
        // Elf - +1 to range or spell rolls
        // Goblin - No surprise
        // Half-Elf - TODO: Roll 2 on talent roll, choose one
        // Half-Orc - +1 to melee and damage rolls.
        // Halfling - Once per day turn invisible for 3 rounds
        // Human - 2 Talent rolls
        // Kobold - Luck token each session or +1 spell casting

        switch (playerClass) {
            case BARD: {
                if (stats.getStrength() > stats.getDexterity()) {
                    possibleAncestries = List.of(
                            Ancestry.DWARF,
                            // Ancestry.ELF, // No range or spell rolls
                            Ancestry.GOBLIN,
                            Ancestry.HALF_ELF,
                            Ancestry.HALF_ORC,
                            Ancestry.HALFLING,
                            Ancestry.HUMAN
                            //Ancestry.KOBOLD // No Kobolds (Bards already have luck tokens and are not spell casters)
                    );
                } else {
                    possibleAncestries = List.of(
                            Ancestry.DWARF,
                            Ancestry.ELF,
                            Ancestry.GOBLIN,
                            Ancestry.HALF_ELF,
                            //Ancestry.HALF_ORC, // Prefer ranged and not melee?
                            Ancestry.HALFLING,
                            Ancestry.HUMAN
                            //Ancestry.KOBOLD // No Kobolds (Bards already have luck tokens and are not spell casters)
                    );
                }
                break;
            }
            case FIGHTER, PALADIN: {
                possibleAncestries = List.of(
                        Ancestry.DWARF,
                        // Ancestry.ELF, // No range or spell rolls
                        Ancestry.GOBLIN,
                        Ancestry.HALF_ELF,
                        Ancestry.HALF_ORC,
                        Ancestry.HALFLING,
                        Ancestry.HUMAN
                        //Ancestry.KOBOLD // No Kobolds (Bards already have luck tokens and are not spell casters)
                );
                break;
            }
            case PRIEST, NECROMANCER: {
                // All ancestries make sense
                possibleAncestries = List.of(
                        Ancestry.DWARF,
                        Ancestry.ELF,
                        Ancestry.GOBLIN,
                        Ancestry.HALF_ELF,
                        Ancestry.HALF_ORC,
                        Ancestry.HALFLING,
                        Ancestry.HUMAN,
                        Ancestry.KOBOLD
                );
                break;
            }
            case RANGER, THIEF: {
                possibleAncestries = List.of(
                        Ancestry.DWARF,
                        Ancestry.ELF,
                        Ancestry.GOBLIN,
                        Ancestry.HALF_ELF,
                        Ancestry.HALF_ORC,
                        Ancestry.HALFLING,
                        Ancestry.HUMAN
                        //Ancestry.KOBOLD // No Kobolds
                );
                break;
            }
            case WITCH, WIZARD: {
                // No Half-Orc for Witch or Wizards
                possibleAncestries = List.of(
                        Ancestry.DWARF,
                        Ancestry.ELF,
                        Ancestry.GOBLIN,
                        Ancestry.HALF_ELF,
                        Ancestry.HALFLING,
                        Ancestry.HUMAN,
                        Ancestry.KOBOLD
                );
                break;
            }
            default: {
                throw new IllegalArgumentException("Invalid player class: " + playerClass);
            }
        }

        final Dice dice = new SingleDie(possibleAncestries.size());
        final Ancestry selectedAncestry = possibleAncestries.get(dice.roll() - 1);
        final AncestryDecorator ancestryDecorator = ancestryDecorators.get(selectedAncestry);
        ancestryDecorator.decorate(playerClass, bonuses);

        return selectedAncestry;
    }

}
