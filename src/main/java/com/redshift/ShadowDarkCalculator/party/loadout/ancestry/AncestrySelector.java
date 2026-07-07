package com.redshift.ShadowDarkCalculator.party.loadout.ancestry;

import com.redshift.ShadowDarkCalculator.creatures.players.Ancestry;
import com.redshift.ShadowDarkCalculator.creatures.players.PlayerClass;
import com.redshift.ShadowDarkCalculator.dice.Dice;
import com.redshift.ShadowDarkCalculator.dice.SingleDie;
import com.redshift.ShadowDarkCalculator.party.loadout.Bonuses;

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

    public Ancestry selectAndApplyBonuses(PlayerClass playerClass, Bonuses bonuses) {
        final List<Ancestry> possibleAncestries;

        switch (playerClass) {
            case BARD: {
                // No Kobolds (Bards already have luck tokens and are not spell casters)
                possibleAncestries = List.of(
                        Ancestry.DWARF,
                        Ancestry.ELF,
                        Ancestry.GOBLIN,
                        Ancestry.HALF_ELF,
                        Ancestry.HALF_ORC,
                        Ancestry.HALFLING,
                        Ancestry.HUMAN
                );
                break;
            }
            case FIGHTER, PALADIN: {
                // No Elf or Kobold (not ranged or spell caster)
                possibleAncestries = List.of(
                        Ancestry.DWARF,
                        Ancestry.GOBLIN,
                        Ancestry.HALF_ELF,
                        Ancestry.HALF_ORC,
                        Ancestry.HALFLING,
                        Ancestry.HUMAN
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
            case THIEF: {
                // No Kobolds for Thief
                possibleAncestries = List.of(
                        Ancestry.DWARF,
                        Ancestry.ELF,
                        Ancestry.GOBLIN,
                        Ancestry.HALF_ELF,
                        Ancestry.HALF_ORC,
                        Ancestry.HALFLING,
                        Ancestry.HUMAN
                );
                break;
            }
            case WIZARD: {
                // No Half-Orc for Wizards
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
