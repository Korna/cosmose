package com.swar.game.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.swar.game.Game;
import com.swar.game.Models.Creator;
import com.swar.game.Models.Ship;
import com.swar.game.managers.World.BodyBuilder;
import com.swar.game.managers.World.GameContactListener;
import com.swar.game.managers.World.ObjectHandler;

/**
 * Created by Koma on 17.01.2017.
 */
public class Player extends Sprite implements Creator {//все параметры корабля


    public Ship ship;

    private Texture shipTexture;//текстура корабля


    private GameContactListener player_cl;


    public float timeInGame = 0;

    private boolean dead = false;

    public Player(Body body, GameContactListener cl, Ship ship) {
        super(body);


        this.ship = ship;
        player_cl = cl;


        shipTexture = Game.res.getTexture(ship.getShipSprite());


        setUpAnimation();
    }

    private void setUpAnimation() {
        TextureRegion[] sprites = TextureRegion.split(shipTexture, ship.getWidth(), ship.getHeight())[0];
        setAnimation(sprites, 1 / 12f);

    }


    public void ship_l() {
        if (shipTexture == Game.res.getTexture(ship.getShipSprite() + "_r"))
            shipTexture = Game.res.getTexture(ship.getShipSprite());
        else
            shipTexture = Game.res.getTexture(ship.getShipSprite() + "_l");

        setUpAnimation();
    }

    public void ship_r() {
        if (shipTexture == Game.res.getTexture(ship.getShipSprite() + "_l"))
            shipTexture = Game.res.getTexture(ship.getShipSprite());
        else
            shipTexture = Game.res.getTexture(ship.getShipSprite() + "_r");

        setUpAnimation();
    }

    public void ship() {
        shipTexture = Game.res.getTexture(ship.getShipSprite());

        setUpAnimation();

    }

    private int credits = 0;

    public int getCredits() {
        credits += player_cl.getCredits();

        return credits;
    }

    public int getSpeed() {
        return (int) ship.getSpeed();
    }

    public boolean isDead() {
        return dead;
    }

    public void setDead(boolean dead) {
        this.dead = dead;
    }

    @Override
    public boolean createObject(BodyBuilder bodyBuilder, ObjectHandler objectHandler) {
        float x = getBody().getPosition().x;
        float y = getBody().getPosition().y;

        String type = ship.weapons.get(0).bulletModel.pierceType.name();


        Body bulletBody;
        Bullet b;

        if (ship.weapons.size() > 1) {

            if (ship.weapons.get(0).getReload() < ship.weapons.get(0).getTimeAfterShot()) {
                ship.weapons.get(0).setTimeAfterShot(0.0f);
                bulletBody = bodyBuilder.createBulletPlayer(x - 12, y, type);
                b = new Bullet(bulletBody, ship.weapons.get(0).bulletModel.bulletType, ship.weapons.get(0).bulletModel, 560);
                bulletBody.setUserData(b);
                objectHandler.add(b);
                ship.setEnergy(ship.getEnergy() - 1);

                bulletBody = bodyBuilder.createBulletPlayer(x + 12, y, type);
                b = new Bullet(bulletBody, ship.weapons.get(0).bulletModel.bulletType, ship.weapons.get(0).bulletModel, 560);
                bulletBody.setUserData(b);
                objectHandler.add(b);
                ship.setEnergy(ship.getEnergy() - 1);

                return true;

            }

        } else {
            if (ship.weapons.get(0).getReload() < ship.weapons.get(0).getTimeAfterShot()) {
                ship.weapons.get(0).setTimeAfterShot(0.0f);
                bulletBody = bodyBuilder.createBulletPlayer(x, y + 5, type);
                b = new Bullet(bulletBody, ship.weapons.get(0).bulletModel.bulletType, ship.weapons.get(0).bulletModel, 560);
                bulletBody.setUserData(b);
                objectHandler.add(b);
                ship.setEnergy(ship.getEnergy() - 1);

                return true;

            }
        }

        return false;
    }

}
