package com.swar.game.Models;

/**
 * Created by Koma on 07.10.2017.
 */
public interface Killable {
    public float getHp();
    public void setHp(float hp);
    public void decreaseHp(float hp);
    public void increaseHp(float hp);
}
