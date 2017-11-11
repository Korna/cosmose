package com.swar.game.managers;

import com.badlogic.gdx.Gdx;

/**
 * Created by Koma on 11.10.2017.
 */
public enum VibrationManager {
    SHORT, MEDIUM, LONG;

    public static int vibrate(VibrationManager vibrationManager){
        int valShort = 10;
        int valMedium = 20;
        int valLong = 30;

        int value = 0;

        switch(vibrationManager){
            case SHORT:
                value = valShort;
                break;
            case MEDIUM:
                value = valMedium;
                break;
            case LONG:
                value = valLong;
                break;
        }

        Gdx.input.vibrate(value);
        return value;
    }
}
