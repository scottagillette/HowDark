package com.redshift.ShadowDarkCalculator.party.loadout;

import lombok.Getter;

@Getter
public class Bonuses {

    private boolean damageDiceD12;

    private boolean twoHandsFree = false;
    private boolean luckToken = false;

    private int strengthBonus;
    private int dexterityBonus;
    private int constitutionBonus;
    private int wisdomBonus;
    private int intelligenceBonus;
    private int charismaBonus;

    private int armorClassBonus;

    private int hitPointsBonus;
    private boolean hitPointAdvantageRoll = false;

    private int meleeAttackBonus;
    private int meleeDamageBonus;

    private int rangedAttackBonus;
    private int rangedDamageBonus;

    private int spellCheckBonus;
    private int spellAdvantages;
    private int extraSpellChoice;

    private boolean advantageOnTalentRoll = false;
    private int talentRolls = 1;

    public void addDamageDiceD12() {
        this.damageDiceD12 = true;
    }

    public void addTwoHandsFree() {
        this.twoHandsFree = true;
    }

    public void addLuckToken() {
        this.luckToken = true;
    }

    public void addExtraSpellChoice() {
        this.extraSpellChoice++;
    }

    public void addArmorClassBonus() {
        this.armorClassBonus++;
    }

    public void addAdvantageOnTalentRoll() {
        this.advantageOnTalentRoll = true;
    }

    public void addExtraTalentRoll() {
        this.talentRolls++;
    }

    public void addStrengthBonus() {
        strengthBonus++;
    }

    public void addDexterityBonus() {
        dexterityBonus++;
    }

    public void addConstitutionBonus() {
        constitutionBonus++;
    }

    public void addWisdomBonus() {
        wisdomBonus++;
    }

    public void addIntelligenceBonus() {
        intelligenceBonus++;
    }

    public void addCharismaBonus() {
        charismaBonus++;
    }

    public void addHitPointsBonus() {
        hitPointsBonus++;
    }

    public void addHitPointAdvantageRoll() {
        hitPointAdvantageRoll = true;
    }

    public void addMeleeAttackBonus() {
        meleeAttackBonus++;
    }

    public void addMeleeDamageBonus() {
        meleeDamageBonus++;
    }

    public void addRangedAttackBomus() {
        rangedAttackBonus++;
    }

    public void addRangedDamageBonus() {
        rangedDamageBonus++;
    }

    public void addSpellCastingBonus() {
        spellCheckBonus++;
    }

    public void addSpellAdvantage() {
        spellAdvantages++;
    }

}
