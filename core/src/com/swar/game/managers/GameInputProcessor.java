package com.swar.game.managers;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;

/**
 * Created by Koma on 17.01.2017.
 */

public class GameInputProcessor extends InputAdapter {

    public boolean keyDown(int k) {
        if(k == Input.Keys.Z) {
            GameInput.setKey(GameInput.BUTTON1, true);
        }
        if(k == Input.Keys.X) {
            GameInput.setKey(GameInput.BUTTON2, true);
        }
        return true;
    }
    public boolean keyUp(int k) {
        if(k == Input.Keys.Z) {
            GameInput.setKey(GameInput.BUTTON1, false);
        }
        if(k == Input.Keys.X) {
            GameInput.setKey(GameInput.BUTTON2, false);
        }
        return true;
    }
}