package com.swar.game.states;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.swar.game.Game;
import com.swar.game.managers.GameStateManagement;

/**
 * Created by Koma on 17.01.2017.
 */
public abstract class GameState {

    protected GameStateManagement gsm;
    protected Game app;

    protected SpriteBatch batch, batch_hud;
    protected OrthographicCamera maincamera;



    protected GameState(GameStateManagement gsm){
        System.out.println("loaded");
        this.gsm = gsm;
        System.out.println("loaded2");
        this.app = gsm.application();
        System.out.println("loaded3");

        batch = app.getBatch();
        batch_hud = app.getBatch();

        maincamera = app.getCamera();

    }

    public void resize(int w, int h){
        maincamera.setToOrtho(false, w, h);
    }



    public abstract void update(float delta);
    public abstract void render();
    public abstract void dispose();

}
