package com.redshift.ShadowDarkCalculator.creatures;

import com.redshift.ShadowDarkCalculator.dice.SingleDie;

import static com.redshift.ShadowDarkCalculator.dice.SingleDie.D20;

public class Stats {

    private final int strength;
    private int currentStrength;
    private final int dexterity;
    private int currentDexterity;
    private final int constitution;
    private int currentConstitution;
    private final int intelligence;
    private int currentIntelligence;
    private final int wisdom;
    private int currentWisdom;
    private final int charisma;
    private int currentCharisma;

    public Stats(int strength, int dexterity, int constitution, int intelligence, int wisdom, int charisma) {
            this.strength = strength;
            this.currentStrength = strength;
            this.dexterity = dexterity;
            this.currentDexterity = dexterity;
            this.constitution = constitution;
            this.currentConstitution = constitution;
            this.intelligence = intelligence;
            this.currentIntelligence = intelligence;
            this.wisdom = wisdom;
            this.currentWisdom = wisdom;
            this.charisma = charisma;
            this.currentCharisma = charisma;
    }

    private int getModifier(int attribute) {
        return switch (attribute) {
            case 1, 2, 3 -> -4;
            case 4, 5 -> -3;
            case 6, 7 -> -2;
            case 8, 9 -> -1;
            case 10, 11 -> 0;
            case 12, 13 -> 1;
            case 14, 15 -> 2;
            case 16, 17 -> 3;
            case 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30 -> 4;
            default -> 0;
        };
    }

    public int getStrength() {
        return strength;
    }

    public int getCurrentStrength() {
        return currentStrength;
    }

    public int getStrengthModifier() {
        return getModifier(currentStrength);
    }

    public boolean strengthSave(int difficultyCheck) {
        return D20.roll() + getModifier(currentStrength) >= difficultyCheck;
    }

    public int strengthSave() {
        return D20.roll() + getModifier(currentStrength);
    }


    public int getDexterity() {
        return dexterity;
    }

    public int getCurrentDexterity() {
        return currentDexterity;
    }

    public int getDexterityModifier() {
        return getModifier(currentDexterity);
    }

    public boolean dexteritySave(int difficultyCheck) {
        return D20.roll() + getModifier(currentDexterity) >= difficultyCheck;
    }

    public int dexteritySave() {
        return D20.roll() + getModifier(currentDexterity);
    }


    public int getConstitution() {
        return constitution;
    }

    public int getCurrentConstitution() {
        return currentConstitution;
    }

    public int getConstitutionModifier() {
        return getModifier(currentConstitution);
    }

    public boolean constitutionSave(int difficultyCheck) {
        return D20.roll() + getModifier(currentConstitution) >= difficultyCheck;
    }

    public int constitutionSave() {
        return D20.roll() + getModifier(currentConstitution);
    }

    public int constitutionDrain(SingleDie die) {
        // Reduce the targets constitution by the rolled amount.
        currentConstitution = currentConstitution - die.roll();
        return currentConstitution;
    }


    public int getIntelligence() {
        return intelligence;
    }

    public int getCurrentIntelligence() {
        return currentIntelligence;
    }

    public int getIntelligenceModifier() {
        return getModifier(currentIntelligence);
    }

    public boolean intelligenceSave(int difficultyCheck) {
        return D20.roll() + getModifier(currentIntelligence) >= difficultyCheck;
    }

    public int intelligenceSave() {
        return D20.roll() + getModifier(currentIntelligence);
    }


    public int getWisdom() {
        return wisdom;
    }

    public int getCurrentWisdom() {
        return currentWisdom;
    }

    public int getWisdomModifier() {
        return getModifier(currentWisdom);
    }

    public boolean wisdomSave(int difficultyCheck) {
        return D20.roll() + getModifier(currentWisdom) >= difficultyCheck;
    }

    public int wisdomSave() {
        return D20.roll() + getModifier(currentWisdom);
    }


    public int getCharisma() {
        return charisma;
    }

    public int getCurrentCharisma() {
        return currentCharisma;
    }

    public int getCharismaModifier() {
        return getModifier(currentCharisma);
    }

    public boolean charismaSave(int difficultyCheck) {
        return D20.roll() + getModifier(currentCharisma) >= difficultyCheck;
    }

    public int charismaSave() {
        return D20.roll() + getModifier(currentCharisma);
    }
}
