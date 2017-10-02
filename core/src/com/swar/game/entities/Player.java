package com.swar.game.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.swar.game.Game;
import com.swar.game.Models.Ship;
import com.swar.game.Models.Weapon;
import com.swar.game.managers.GameContactListener;

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

    private Weapon listWeapon[];
    private int weaponNumber;

    public float timeInGame = 0;

    private boolean dead = false;
    public Player(Body body, GameContactListener cl, int shipIndex, Ship ship, int weaponIndex) {
        super(body);
        this.shipIndex = shipIndex;

        this.ship = ship;
        player_cl = cl;


        shipTexture = Game.res.getTexture(ship.getShipSprite());
        bulletIndex = weaponIndex;
        bulletTexture = Game.res.getTexture("bullet_" + String.valueOf(weaponIndex));

        setUpAnimation();
    }
    private void setUpAnimation(){
        TextureRegion[] sprites = TextureRegion.split(shipTexture, ship.getWidth(), ship.getHeight())[0];
        setAnimation(sprites, 1 / 12f);

    }


    public void ship_l(){
        if(shipTexture == Game.res.getTexture(ship.getShipSprite() + "_r"))
            shipTexture = Game.res.getTexture(ship.getShipSprite());
        else
            shipTexture = Game.res.getTexture(ship.getShipSprite() + "_l");

        setUpAnimation();
    }

    public void ship_r(){
        if(shipTexture == Game.res.getTexture(ship.getShipSprite() +"_l"))
            shipTexture = Game.res.getTexture(ship.getShipSprite());
        else
            shipTexture = Game.res.getTexture(ship.getShipSprite()+"_r");

        setUpAnimation();
    }

    public void ship(){
        shipTexture = Game.res.getTexture(ship.getShipSprite());

        setUpAnimation();

    }
    private int credits = 0;
    public int getCredits() {
        credits += player_cl.getCredits();

        return credits; }
    public int getSpeed(){return (int)ship.getSpeed();}

    public boolean isDead() {
        return dead;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }
}
