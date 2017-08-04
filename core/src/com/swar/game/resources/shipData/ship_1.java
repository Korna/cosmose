package com.swar.game.resources.shipData;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by Koma on 27.01.2017.
 */
public class ship_1{
    private float speed;
    private int hp;
    private int armor;
    private String shipSprite;
    private String Sprite;

    private float weaponPositionX[];
    private float weaponPositionY[];
    private int carryWeapons;
    private int carryEquipment;

    public ship_1(){
        speed = 250;
        hp = 100;
        armor = 5;
        shipSprite = "ship_1";
        Sprite = "ship_1";

        carryWeapons = 1;
        weaponPositionX[0] = 0;
        weaponPositionY[0] = +10;

        carryEquipment = 0;
    }
}
