package com.swar.game.entities;

/**
 * Created by Koma on 27.01.2017.
 */
public class Ship {
    private float speed;
    private int hp;
    private int armor;
    private String shipSprite;
    private String Sprite;

    private float weaponPositionX[] = new float[10];//пусть будет 10 оружек - максимум
    private float weaponPositionY[] = new float[10];
    private Weapon weaponList[] = new Weapon[10];

    private int carryWeapons;
    private int carryEquipment;


    private int height;
    private int width;

    public Ship(float speed, int hp, int armor, String shipSprite, String Sprite, float weaponPositionX[], float weaponPositionY[], int carryWeapons, int carryEquipment, Weapon weaponList[],
    int height, int width){
        this.speed = speed;
        this.hp = hp;
        this.armor = armor;
        this.shipSprite = shipSprite;
        this.Sprite = Sprite;

        for (int i = 0; i < carryWeapons; i++)
            this.weaponPositionX[i] = weaponPositionX[i];

        for (int i = 0; i < carryWeapons; i++)
            this.weaponPositionY[i] = weaponPositionY[i];

        this.carryWeapons = carryWeapons;
        this.carryEquipment = carryEquipment;

        for (int i = 0; i < carryWeapons; i++)
            this.weaponList[i] = weaponList[i];

        this.height = height;
        this.width = width;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }
}
