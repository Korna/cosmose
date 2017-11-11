package com.swar.game.managers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

/**
 * Created by Koma on 01.10.2017.
 */
public class InterfaceManagerPC implements  IInterfaceManager{


    //bundle
    public float horizontalForce = 0;
    public float verticalForce = 0;
    public boolean shot = false;
    public boolean ability = false;

    public InterfaceManagerPC() {

    }
    @Override
    public void inputUpdate() {
        horizontalForce = 0;
        verticalForce = 0;
        shot = false;
        ability = false;


            if(Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)){
                --horizontalForce;

            }
            else
            if(Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)){
                ++horizontalForce;

            }

            if(Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)){
                ++verticalForce;
            }
            else
            if(Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S)){
                --verticalForce;
            }

            if(Gdx.input.isKeyJustPressed(Input.Keys.SPACE)){
                shot = true;
            }
            if(Gdx.input.isKeyJustPressed(Input.Keys.ENTER)){
                ability = true;
            }

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
