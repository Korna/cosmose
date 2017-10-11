package com.swar.game.managers;

/**
 * Created by Koma on 24.09.2017.
 */
public class GameConfig {

    private static boolean vibraion = true;
    private static boolean vButtons = false;


    private static float posY = 0f;



    public GameConfig(){

    }
    public GameConfig(boolean vibraion, boolean vButtons, float posY) {
        this.vibraion = vibraion;
        this.vButtons = vButtons;

        this.posY = posY;

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

    public float getPosY() {
        return posY;
    }

    public void setPosY(float posY) {
        GameConfig.posY = posY;
    }
}
