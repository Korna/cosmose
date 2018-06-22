package com.swar.game.managers;

import com.badlogic.gdx.Gdx;

/**
 * Created by Koma on 01.10.2017.
 */
public class InterfaceManagerAndroid implements IInterfaceManager{

    float playerHandle = 4.5f;
    float playerZone = 0.3f;
    float playerNear = 0.25f;

    //bundle
    public float horizontalForce = 0;
    public float verticalForce = 0;
    public boolean shot = false;
    public boolean ability = false;

    public InterfaceManagerAndroid(float playerZone, float playerHandle, float playerNear) {
        this.playerHandle = playerHandle;
        this.playerZone = playerZone;
        this.playerNear = playerNear;


    }
    @Override
    public void inputUpdate(){
        horizontalForce = 0;
        verticalForce = 0;
        shot = false;
        ability = false;


            float accelX = Gdx.input.getAccelerometerX();
            float accelY = Gdx.input.getAccelerometerY();

            if(accelX > playerNear){
                horizontalForce = -0.25f;
                if(accelX > playerZone + playerNear)
                    horizontalForce -= 0.8f;
            }
            else
            if(accelX < -playerNear){
                horizontalForce = 0.25f;
                if(accelX < -(playerZone + playerNear))
                    horizontalForce += 0.8f;

            }

            if(accelY > (playerZone + playerHandle)){
                --verticalForce;
            }
            else
            if(accelY < (-playerZone + playerHandle)){
                ++verticalForce;
            }

            if(Gdx.input.justTouched()){
               shot = true;
            }
            if(Gdx.input.isTouched())
                shot = true;


            //обработка абилки
            boolean fire = false;
            boolean fast = false;
            final int fireAreaMax = Gdx.graphics.getWidth()/2;
            final int fastAreaMin = Gdx.graphics.getWidth()/2;
            for (int i = 0; i < 2; i++) { // 20 is max number of touch points
                if (Gdx.input.isTouched(i)) {
                    final int iX = Gdx.input.getX(i);
                    fire = fire || (iX < fireAreaMax);
                    fast = fast || (iX > fastAreaMin);
                }
            }

            if (fast && fire) {
                ability = true;
                System.out.println("ability");
            }
    }

    public boolean calculateZone(float zone, float handle, float pos) {

        return pos < (zone + handle);
    }


    @Override
    public boolean getShot() {
        return shot;
    }

    @Override
    public boolean getAbility() {
        return ability;
    }

    @Override
    public float getVForce() {
        return verticalForce;
    }

    @Override
    public float getHFloat() {
        return horizontalForce;
    }


}
