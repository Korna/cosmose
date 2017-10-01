package com.swar.game.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.swar.game.Singleton;
import com.swar.game.entities.Player;

import static com.swar.game.utils.constants.VIBRATION_LONG;

/**
 * Created by Koma on 01.10.2017.
 */
public class InterfaceManager {
    Player player;
    boolean available;

    final int playerHandle = 5;
    final float playerZone = 0.5f;

    Singleton instance = Singleton.getInstance();

    public InterfaceManager(){

    }




    public void inputUpdate(float delta){
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
                  //  createBulletPlayer();
                    Gdx.input.vibrate(VIBRATION_LONG);
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
               // createBulletPlayer();
            }

        }


        player.getBody().setLinearVelocity(horizontalForce * shipSpeed, verticalForce * shipSpeed);
    }


}
