package com.swar.game.states;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.swar.game.Game;
import com.swar.game.managers.GameStateManagement;

import static com.swar.game.utils.constants.GAME_HEIGHT;
import static com.swar.game.utils.constants.GAME_WIDTH;

/**
 * Created by Koma on 17.01.2017.
 */
public class SplashState  extends GameState{

    float acc = 0f;
    private float time = 0f;
    private Texture tex_splash;


    public SplashState(GameStateManagement gsm){
        super(gsm);
        tex_splash = Game.res.getTexture("splash");
       // tex_splash = new Texture("data/splash.png");

    }

    @Override
    public void update(float delta) {
        acc += delta;
        if(acc >= time){
            gsm.setState(GameStateManagement.State.MAINMENU);
        }
    }

    @Override
    public void render() {
        Gdx.gl.glClearColor(1f, 1f, 1f, 1f);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        batch.setProjectionMatrix(maincamera.combined);
        batch.begin();

        batch.draw(tex_splash, GAME_WIDTH / 4 - tex_splash.getWidth() / 2, GAME_HEIGHT / 4 - tex_splash.getHeight() / 2);
        batch.end();
    }

    @Override
    public void dispose() {
        tex_splash.dispose();
    }

}