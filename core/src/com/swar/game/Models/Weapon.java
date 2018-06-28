package com.swar.game.Models;



/**
 * Created by Koma on 29.01.2017.
 */
public class Weapon {

    private float energyCost;
    public BulletModel bulletModel;
    private float reload;
    //time after shot is delta time
    private float timeAfterShot;

    public Weapon(int energy, BulletModel bulletModel, float reload, float timeAfterShot) {
        this.energyCost = energy;
        this.bulletModel = bulletModel;
        this.reload = reload;
        this.timeAfterShot = timeAfterShot;
    }

    public void setReload(float reload) {
        this.reload = reload;
    }

    public float getReload() {
        return reload;
    }

    public float getTimeAfterShot() {
        return timeAfterShot;
    }

    public void setTimeAfterShot(float timeAfterShot) {
        this.timeAfterShot = timeAfterShot;
    }

    public float getEnergyCost() {
        return energyCost;
    }

    public void setEnergyCost(float energyCost) {
        this.energyCost = energyCost;
    }
}
