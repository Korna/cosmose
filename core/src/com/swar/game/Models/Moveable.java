package com.swar.game.Models;

/**
 * Created by Koma on 07.10.2017.
 */
public interface Moveable {
    public float getSpeed();
    public void setSpeed(float speed);
    public void decreaseSpeed(float speed);
    public void increaseSpeed(float speed);
}
