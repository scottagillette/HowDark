package com.redshift.ShadowDarkCalculator.rogue;


import lombok.Data;

@Data
public class Player {
    private int gold = 100;
    private int reputation = 0;
    private String name;
    private String partyName;
    private String enemyName;
    
    public Player() {
        this.partyName = "lost-citadel";
        this.enemyName = "storm-giant";

    }

}
