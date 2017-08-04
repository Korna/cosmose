package com.swar.game.resources.weaponData;

/**
 * Created by Koma on 27.01.2017.
 */
public class weapon_1{
    private float projectileSpeed;
    private boolean pierces;
    private int pierceNumber;
    private String projectileSprite;
    private String Sprite;
    private int tier;

    public weapon_1(){
        projectileSpeed = 100;

        pierces = true;
        pierceNumber = 100;

        projectileSprite = "bullet_1";
        Sprite = "weapon_1";
        tier = 1;
    }


}
