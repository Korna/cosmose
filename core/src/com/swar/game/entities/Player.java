package com.swar.game.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.swar.game.Game;
import com.swar.game.managers.GameContactListener;

/**
 * Created by Koma on 17.01.2017.
 */
public class Player extends Sprite{//все параметры игрока: деньги итд

    public int shipIndex = 1;//выбранный корабль
    public Ship ship;

    private Texture tex;//текстура корабля

    private GameContactListener player_cl;
    private int speed = 300;
    private Weapon listWeapon[];
    private int weaponNumber;

    public Player(Body body, GameContactListener cl, int shipIndex, Ship ship) {
        super(body);
        this.shipIndex = shipIndex;
        this.ship = ship;
        player_cl = cl;


        tex = Game.res.getTexture("ship_" + String.valueOf(shipIndex));

        TextureRegion[] sprites = TextureRegion.split(tex, 32, 32)[0];
        setAnimation(sprites, 1 / 12f);


    }

    public void init(Body body, GameContactListener cl){
        player_cl = cl;


        tex = Game.res.getTexture("ship_" + String.valueOf(shipIndex));

        TextureRegion[] sprites = TextureRegion.split(tex, 32, 32)[0];
        setAnimation(sprites, 1 / 12f);
    }


    public void ship_l(){
        if(tex == Game.res.getTexture("ship_" + String.valueOf(shipIndex) + "_r"))
            tex = Game.res.getTexture("ship_" + String.valueOf(shipIndex));
        else
            tex = Game.res.getTexture("ship_" + String.valueOf(shipIndex) + "_l");

        TextureRegion[] sprites = TextureRegion.split(tex, 32, 32)[0];
        setAnimation(sprites, 1 / 12f);
    }

    public void ship_r(){
        if(tex == Game.res.getTexture("ship_"+String.valueOf(shipIndex) +"_l"))
            tex = Game.res.getTexture("ship_" + String.valueOf(shipIndex));
        else
            tex = Game.res.getTexture("ship_"+String.valueOf(shipIndex)+"_r");

        TextureRegion[] sprites = TextureRegion.split(tex, 32, 32)[0];
        setAnimation(sprites, 1 / 12f);
    }

    public void ship(){
        tex = Game.res.getTexture("ship_" + String.valueOf(shipIndex));

        TextureRegion[] sprites = TextureRegion.split(tex, 32, 32)[0];
        setAnimation(sprites, 1 / 12f);

    }
    public int getHp(){return player_cl.getHp();}
    public int getCredits(){return player_cl.getCredits(); }
    public int getSpeed(){return speed;}
    public int getWeaponNumber(){return weaponNumber;}
    public void setWeaponNumber(int num){weaponNumber = num;}
}
