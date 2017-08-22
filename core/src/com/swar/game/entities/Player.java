package com.swar.game.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.swar.game.Game;
import com.swar.game.managers.GameContactListener;

import static com.swar.game.utils.constants.GAME_WIDTH;

/**
 * Created by Koma on 17.01.2017.
 */
public class Player extends Sprite{//все параметры корабля

    public int shipIndex = 1;//выбранный корабль
    public Ship ship;

    private Texture shipTexture;//текстура корабля

    public int bulletIndex = 1;//выбранная текстура пули
    private Texture bulletTexture;

    private GameContactListener player_cl;
    private int speed = (GAME_WIDTH*12) /3;
    private Weapon listWeapon[];
    private int weaponNumber;

    public Player(Body body, GameContactListener cl, int shipIndex, Ship ship, int weaponIndex) {
        super(body);
        this.shipIndex = shipIndex;
        this.ship = ship;
        player_cl = cl;


        shipTexture = Game.res.getTexture("ship_" + String.valueOf(shipIndex));
        bulletIndex = weaponIndex;
        bulletTexture = Game.res.getTexture("bullet_" + String.valueOf(weaponIndex));

        TextureRegion[] sprites = TextureRegion.split(shipTexture, 32, 32)[0];
        setAnimation(sprites, 1 / 12f);


    }

    public void init(Body body, GameContactListener cl){
        player_cl = cl;

        shipTexture = Game.res.getTexture("ship_" + String.valueOf(shipIndex));

        TextureRegion[] sprites = TextureRegion.split(shipTexture, 32, 32)[0];
        setAnimation(sprites, 1 / 12f);
    }


    public void ship_l(){
        if(shipTexture == Game.res.getTexture("ship_" + String.valueOf(shipIndex) + "_r"))
            shipTexture = Game.res.getTexture("ship_" + String.valueOf(shipIndex));
        else
            shipTexture = Game.res.getTexture("ship_" + String.valueOf(shipIndex) + "_l");

        TextureRegion[] sprites = TextureRegion.split(shipTexture, 32, 32)[0];
        setAnimation(sprites, 1 / 12f);
    }

    public void ship_r(){
        if(shipTexture == Game.res.getTexture("ship_"+String.valueOf(shipIndex) +"_l"))
            shipTexture = Game.res.getTexture("ship_" + String.valueOf(shipIndex));
        else
            shipTexture = Game.res.getTexture("ship_"+String.valueOf(shipIndex)+"_r");

        TextureRegion[] sprites = TextureRegion.split(shipTexture, 32, 32)[0];
        setAnimation(sprites, 1 / 12f);
    }

    public void ship(){
        shipTexture = Game.res.getTexture("ship_" + String.valueOf(shipIndex));

        TextureRegion[] sprites = TextureRegion.split(shipTexture, 32, 32)[0];
        setAnimation(sprites, 1 / 12f);

    }
    public int getHp(){return player_cl.getHp();}
    public int getCredits(){return player_cl.getCredits(); }
    public int getSpeed(){return speed;}

}
