package com.swar.game.managers;

import com.badlogic.gdx.Gdx;

/**
 * Created by Koma on 11.10.2017.
 */
public enum VibrationManager {
    SHORT, MEDIUM, LONG;

    public static void vibrate(VibrationManager vibrationManager){
        switch(vibrationManager){
            case SHORT:
                Gdx.input.vibrate(10);
                break;
            case MEDIUM:
                Gdx.input.vibrate(20);
                break;
            case LONG:
                Gdx.input.vibrate(30);
                break;
        }
    }
}
