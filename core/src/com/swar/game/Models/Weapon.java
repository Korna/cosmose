package com.swar.game.Models;



/**
 * Created by Koma on 29.01.2017.
 */
public class Weapon {

    private int energy;
    public BulletModel bulletModel;

    public Weapon(int energy, BulletModel bulletModel) {
        this.energy = energy;
        this.bulletModel = bulletModel;
    }
}
