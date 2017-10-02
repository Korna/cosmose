package com.swar.game.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.physics.box2d.Body;
import com.swar.game.Models.Bullet;
import com.swar.game.entities.Player;
import com.swar.game.utils.Singleton;

import static com.swar.game.utils.constants.*;

/**
 * Created by Koma on 01.10.2017.
 */
public class InterfaceManager {
    Player player;
    BodyBuilder bodyBuilder;
    ObjectHandler objectHandler;
    boolean available;

    final int playerHandle = 5;
    final float playerZone = 0.5f;

    Singleton instance = Singleton.getInstance();


    public InterfaceManager(Player player, BodyBuilder bodyBuilder, ObjectHandler objectHandler, boolean available) {
        this.player = player;
        this.bodyBuilder = bodyBuilder;
        this.objectHandler = objectHandler;
        this.available = available;
    }

    public void inputUpdate(){
        int horizontalForce = 0;
        int verticalForce = 0;
        int shipSpeed = player.getSpeed();

        player.ship();

        if(available){
            float accelX = Gdx.input.getAccelerometerX();
            float accelY = Gdx.input.getAccelerometerY();

            if(accelX > playerZone){
                --horizontalForce;
                player.ship_l();
            }
            else
            if(accelX < -playerZone){
                ++horizontalForce;
                player.ship_r();
            }

            if(accelY > (playerZone + playerHandle)){
                --verticalForce;
            }
            else
            if(accelY < (-playerZone + playerHandle)){
                ++verticalForce;
            }

            if(Gdx.input.justTouched()){
                if(player.ship.getEnergy() > 0){
                    playerShot(available);

                    player.ship.setEnergy(player.ship.getEnergy() - 1);
                }
            }

        }else{


            if(Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)){
                --horizontalForce;
                player.ship_l();
            }
            else
            if(Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)){
                ++horizontalForce;
                player.ship_r();
            }

            if(Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)){
                ++verticalForce;
            }
            else
            if(Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S)){
                --verticalForce;
            }

            if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)){
                float x = player.getBody().getPosition().x;
                float y = player.getBody().getPosition().y + 5;
                String type;
                if(player.bulletIndex==1)
                    type = BULLET_PIERCING;
                else
                    type = BULLET_DESTROYABLE;

                Body bulletBody = bodyBuilder.createBulletPlayer(x, y, type);

                Bullet b;
                b = new Bullet(bulletBody, player.bulletIndex, false, false);


                bulletBody.setUserData(b);
                objectHandler.add(b);
            }

        }


        player.getBody().setLinearVelocity(horizontalForce * shipSpeed, verticalForce * shipSpeed);
    }

    private void playerShot(boolean vibrate){
        float x = player.getBody().getPosition().x;
        float y = player.getBody().getPosition().y;

        String type;
        if(player.bulletIndex==1)
            type = BULLET_PIERCING;
        else
            type = BULLET_DESTROYABLE;

        Body bulletBody;
        Bullet b;

        if(player.shipIndex==4){

            bulletBody = bodyBuilder.createBulletPlayer(x-12, y, type);
            b = new Bullet(bulletBody, player.bulletIndex, false, false);
            bulletBody.setUserData(b);
            objectHandler.add(b);

            bulletBody = bodyBuilder.createBulletPlayer(x+12, y, type);
            b = new Bullet(bulletBody, player.bulletIndex, false, false);
            bulletBody.setUserData(b);
            objectHandler.add(b);

        }else{

            bulletBody = bodyBuilder.createBulletPlayer(x, y+5, type);
            b = new Bullet(bulletBody, player.bulletIndex, false, false);
            bulletBody.setUserData(b);
            objectHandler.add(b);
        }

        if(vibrate)
            Gdx.input.vibrate(VIBRATION_LONG);
    }


}
