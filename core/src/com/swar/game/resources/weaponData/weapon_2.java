package com.swar.game.resources.weaponData;

/**
 * Created by Koma on 27.01.2017.
 */
public class weapon_2{
    private float projectileSpeed;
    private boolean pierces;
    private int pierceNumber;
    private String projectileSprite;
    private String Sprite;
    private int tier;

    public weapon_2(){
        projectileSpeed = 50;

        pierces = false;
        pierceNumber = 0;

        projectileSprite = "bullet_2";
        Sprite = "weapon_2";
        tier = 1;
    }

}
