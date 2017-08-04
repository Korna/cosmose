package com.swar.game.resources.bulletData;

/**
 * Created by Koma on 30.01.2017.
 */
public class bullet {
    private float damage;
    private String name;
    private int id;
    private float speed;
    private boolean wavy;
    private float variable;
    public boolean pierces;
    public int pierceNumber;


    public bullet(float damage, String name, int id, float speed,  boolean wavy, float variable, int pierceNumber, boolean pierces){
        this.damage = damage;
        this.name = name;
        this.id = id;
        this.speed = speed;
        this.wavy = wavy;
        this.variable = variable;
        this.pierceNumber = pierceNumber;
        this.pierces = pierces;

    }
}
