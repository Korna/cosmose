package com.swar.game.managers;

/**
 * Created by Koma on 09.01.2017.
 */


import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.swar.game.Game;
import com.swar.game.ShipType;
import com.swar.game.entities.Player;
import com.swar.game.states.*;

import java.util.Stack;

import static com.swar.game.utils.constants.*;


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
        DEATH,
        PLAYSURVIVAL
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
            case SPLASH:
                return new SplashState(this);
            case MAINMENU:
                return new MenuState(this);
            case PLAY:
                return new PlayClassicState(this);
            case DEATH:
                return new DeathState(this);
            case HUB:
                world = new World(new Vector2(0, 0), false);//потому как создается игрок в хабе
                return new HubState(this);
            case PLAYSURVIVAL:
                world = new World(new Vector2(0, 0), false);//потому как создается игрок в хабе

                playerBody = createPlayer(GAME_WIDTH / 4, 15, GAME_WIDTH/30, GAME_WIDTH/20);

                player = new Player(playerBody, cl, 1, ShipType.getShip(ShipType.valueOf("ship_1")), 2);//здесь по индексу передаём корабль из ДБ

                player.initSprite(playerBody);


                return new PlaySurvivalState(this);
        }
        return null;
    }
    private Body createPlayer(int x, int y, int width, int height){
        BodyDef def = new BodyDef();
        def.type = BodyDef.BodyType.DynamicBody;
        def.position.set(x, y);
        def.fixedRotation = true;


        FixtureDef fdef = new FixtureDef();

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(width, height);

        fdef.shape = shape;
        fdef.filter.categoryBits = BIT_PLAYER;
        fdef.filter.maskBits = BIT_ENEMY | BIT_OBJECT | BIT_BORDER;


        Body pBody = world.createBody(def);


        pBody.createFixture(fdef).setUserData(PLAYER_SHIP);

        shape.dispose();

        return pBody;
    }

}
