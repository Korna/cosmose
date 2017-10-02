package com.swar.game.Models;



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


    public Weapon(float projectileSpeed, boolean pierces, int pierceNumber, String projectileSprite, String Sprite, int tier){
        this.projectileSpeed = projectileSpeed;
        this.pierces = pierces;
        this.pierceNumber = pierceNumber;
        this.projectileSprite = projectileSprite;
        this.Sprite = Sprite;
        this.tier = tier;


    }

    public Weapon getNewWeapon(float projectileSpeed, boolean pierces, int pierceNumber, String projectileSprite, String Sprite, int tier){
        this.projectileSpeed = projectileSpeed;
        this.pierces = pierces;
        this.pierceNumber = pierceNumber;
        this.projectileSprite = projectileSprite;
        this.Sprite = Sprite;
        this.tier = tier;

        return this;
    }

    public Weapon get(){
        return this;
    }

}
