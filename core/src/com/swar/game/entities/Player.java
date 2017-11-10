package com.swar.game.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.swar.game.Game;
import com.swar.game.Models.Creator;
import com.swar.game.Models.Ship;
import com.swar.game.managers.World.BodyBuilder;
import com.swar.game.managers.World.ObjectHandler;

/**
 * Created by Koma on 17.01.2017.
 */
public class Player extends Sprite implements Creator {//все параметры корабля


    public Ship ship;

    private Texture shipTexture;//текстура корабля

    private int credits = 0;
    private int tokens = 0;

    public float timeInGame = 0;

    private boolean dead = false;

    public Player(Body body, Ship ship) {
        super(body);


        this.ship = ship;

        try {
            shipTexture = Game.res.getTexture(ship.getShipSprite());
            setUpAnimation(shipTexture);

        }catch(IllegalArgumentException npe){
            System.out.println(npe.toString());
        }



    }

    private void setUpAnimation(Texture texture) throws NullPointerException, IllegalArgumentException{
        TextureRegion[] sprites = TextureRegion.split(texture, ship.getWidth(), ship.getHeight())[0];
        setAnimation(sprites, 1 / 12f);

    }


    public void ship_l() {
        if (shipTexture == Game.res.getTexture(ship.getShipSprite() + "_r"))
            shipTexture = Game.res.getTexture(ship.getShipSprite());
        else
            shipTexture = Game.res.getTexture(ship.getShipSprite() + "_l");


        setUpAnimation(shipTexture);
    }

    public void ship_r() {
        if (shipTexture == Game.res.getTexture(ship.getShipSprite() + "_l"))
            shipTexture = Game.res.getTexture(ship.getShipSprite());
        else
            shipTexture = Game.res.getTexture(ship.getShipSprite() + "_r");


        setUpAnimation(shipTexture);
    }

    public void ship() {
        shipTexture = Game.res.getTexture(ship.getShipSprite());

        setUpAnimation(shipTexture);

    }


    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
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
                bulletBody = bodyBuilder.createBulletPlayer(x - 12, y, type, null);
                b = new Bullet(bulletBody, ship.weapons.get(0).bulletModel.bulletType, ship.weapons.get(0).bulletModel, 560);
                bulletBody.setUserData(b);
                objectHandler.add(b);
                ship.setEnergy(ship.getEnergy() - ship.weapons.get(0).getEnergyCost());

                bulletBody = bodyBuilder.createBulletPlayer(x + 12, y, type, null);
                b = new Bullet(bulletBody, ship.weapons.get(0).bulletModel.bulletType, ship.weapons.get(0).bulletModel, 560);
                bulletBody.setUserData(b);
                objectHandler.add(b);
                ship.setEnergy(ship.getEnergy() - ship.weapons.get(0).getEnergyCost());

                return true;

            }

        } else {
            if (ship.weapons.get(0).getReload() < ship.weapons.get(0).getTimeAfterShot()) {
                ship.weapons.get(0).setTimeAfterShot(0.0f);
                bulletBody = bodyBuilder.createBulletPlayer(x, y + 5, type, null);
                b = new Bullet(bulletBody, ship.weapons.get(0).bulletModel.bulletType, ship.weapons.get(0).bulletModel, 560);
                bulletBody.setUserData(b);
                objectHandler.add(b);
                ship.setEnergy(ship.getEnergy() - ship.weapons.get(0).getEnergyCost());

                return true;

            }
        }

        return false;
    }

    public void addToken(){
        tokens++;
    }

    public void removeToken(){
        tokens--;
    }
}
