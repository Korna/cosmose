package com.swar.game.entities;

import com.badlogic.gdx.physics.box2d.Body;
import com.swar.game.Models.Killable;
import com.swar.game.Models.Moveable;

/**
 * Created by Koma on 07.10.2017.
 */
public class Enemy extends Sprite implements Killable, Moveable{


    public Enemy(Body body){
        super(body);
    }

    @Override
    public float getHp() {
        return 0;
    }

    @Override
    public void setHp(float hp) {

    }

    @Override
    public void decreaseHp(float hp) {

    }

    @Override
    public void increaseHp(float hp) {

    }

    @Override
    public float getSpeed() {
        return 0;
    }

    @Override
    public void setSpeed(float speed) {

    }

    @Override
    public void decreaseSpeed(float speed) {

    }

    @Override
    public void increaseSpeed(float speed) {

    }
}
