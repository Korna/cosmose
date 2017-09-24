package com.swar.game.managers;

/**
 * Created by Koma on 24.09.2017.
 */
public class GameConfig {


    private static boolean vibraion = true;
    private static boolean vButtons = false;

    public GameConfig(){

    }
    public GameConfig(boolean vibraion, boolean vButtons) {
        this.vibraion = vibraion;
        this.vButtons = vButtons;
    }

    public boolean isVibraion() {
        return vibraion;
    }

    public void setVibraion(boolean vibraion) {
        this.vibraion = vibraion;
    }

    public boolean isvButtons() {
        return vButtons;
    }

    public void setvButtons(boolean vButtons) {
        this.vButtons = vButtons;
    }
}
