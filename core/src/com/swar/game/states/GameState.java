package com.swar.game.states;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.swar.game.Game;
import com.swar.game.entities.Player;
import com.swar.game.managers.GameContactListener;
import com.swar.game.managers.GameStateManagement;
import com.swar.game.resources.bulletData.bulletDB;
import com.swar.game.resources.shipData.shipDB;
import com.swar.game.resources.weaponData.weaponDB;
import com.swar.game.states.PlayState;

import static com.swar.game.utils.constants.*;
import static com.swar.game.utils.constants.BIT_OBJECT;

/**
 * Created by Koma on 17.01.2017.
 */
public abstract class GameState {

    protected GameStateManagement gsm;
    protected Game app;

    protected SpriteBatch batch, batch_hud;
    protected OrthographicCamera maincamera;
    protected OrthographicCamera camera_hud;


    protected GameState(GameStateManagement gsm){

        this.gsm = gsm;
        this.app = gsm.application();



        batch = app.getBatch();
        batch_hud = app.getBatch();

        maincamera = app.getCamera();
        camera_hud = app.getCameraHud();

    }

    public void resize(int w, int h){
        maincamera.setToOrtho(false, w, h);
    }



    public abstract void update(float delta);
    public abstract void render();
    public abstract void dispose();

}
