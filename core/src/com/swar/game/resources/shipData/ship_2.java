package com.swar.game.resources.shipData;

/**
 * Created by Koma on 27.01.2017.
 */
public class ship_2 extends ship {

    private float speed;
    private int hp;
    private int armor;
    private String shipSprite;
    private String Sprite;

    private float weaponPositionX[];
    private float weaponPositionY[];

    private int carryWeapons;
    private int carryEquipment;

    public ship_2(){
        speed = 450;
        hp = 100;
        armor = 0;
        shipSprite = "ship_2";
        Sprite = "ship_2";

        weaponPositionX[0] = +10;
        weaponPositionY[0] = -10;
        weaponPositionX[1] = -10;
        weaponPositionY[1] = -10;

        carryWeapons = 2;
        carryEquipment = 0;
    }

}
