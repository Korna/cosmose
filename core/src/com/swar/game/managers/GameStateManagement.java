package com.swar.game.managers;

/**
 * Created by Koma on 09.01.2017.
 */


import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.swar.game.Game;
import com.swar.game.entities.Player;
import com.swar.game.states.*;

import java.util.Stack;


public class GameStateManagement {
    private final Game app;

    public GameContactListener cl  = new GameContactListener();
    public Body playerBody;
    public Player player;
    public World world;



    private Stack<GameState> states;
    public enum State{
        SPLASH,
        MAINMENU,
        PLAY,
        HUB,
        DEATH
    }

    public GameStateManagement(final Game app){
        this.app = app;
        this.states = new Stack<>();


        this.setState(State.SPLASH);

    }
    public Game application(){
        return app;
    }

    public void update(float delta){
        states.peek().update(delta);

    }

    public void render(){
        states.peek().render();
    }

    public void dispose(){
        for(GameState gs : states){
            gs.dispose();
        }
        states.clear();
    }

    public void resize(int w, int h){
        states.peek().resize(w, h);

    }

    public void setState(State state){
        if(states.size() >= 1){
            states.pop().dispose();
        }
        states.push(getState(state));

    }

    private GameState getState(State state){
        switch(state){

            case SPLASH: return new SplashState(this);
            case MAINMENU: return new MenuState(this);
            case PLAY:
                return new PlayState(this);
            case DEATH:
                System.out.println("death state");
                return new DeathState(this);
            case HUB:
                world = new World(new Vector2(0, 0), false);//потому как создается игрок в хабе
                return new HubState(this);
        }
        return null;
    }

}
