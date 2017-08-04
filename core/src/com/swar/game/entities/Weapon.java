package com.swar.game.entities;

import com.swar.game.resources.bulletData.bullet;

/**
 * Created by Koma on 29.01.2017.
 */
public class Weapon {

    private float projectileSpeed;
    private boolean pierces;
    private int pierceNumber;
    private String projectileSprite;
    private String Sprite;
    private int tier;
    private bullet projectile;

    public Weapon(float projectileSpeed, boolean pierces, int pierceNumber, String projectileSprite, String Sprite, int tier, bullet projectile){
        this.projectileSpeed = projectileSpeed;
        this.pierces = pierces;
        this.pierceNumber = pierceNumber;
        this.projectileSprite = projectileSprite;
        this.Sprite = Sprite;
        this.tier = tier;
        this.projectile = projectile;

    }

    public Weapon getNewWeapon(float projectileSpeed, boolean pierces, int pierceNumber, String projectileSprite, String Sprite, int tier, bullet projectil){
        this.projectileSpeed = projectileSpeed;
        this.pierces = pierces;
        this.pierceNumber = pierceNumber;
        this.projectileSprite = projectileSprite;
        this.Sprite = Sprite;
        this.tier = tier;
        this.projectile = projectile;
        return this;
    }

    public Weapon get(){
        return this;
    }

}
