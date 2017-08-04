package com.swar.game.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.swar.game.Game;

import static com.badlogic.gdx.math.MathUtils.random;

/**
 * Created by Koma on 25.01.2017.
 */

public class Bullet extends Sprite {

    public float damage;
    public String name;
    public int id;
    public float speed;
    public boolean wavy;
    public float variable;

    public Bullet (Body body) {
        super(body);

        Texture tex;
        tex = Game.res.getTexture("bullet_1");
        TextureRegion[] sprites = TextureRegion.split(tex, 32, 32)[0];

        setAnimation(sprites, 1 / 12f);
    }

    public void init(float damage, String name, int id, float speed,  boolean wavy, float variable){
        this.damage = damage;
        this.name = name;
        this.id = id;
        this.speed = speed;
        this.wavy = wavy;
        this.variable = variable;

    }

    public void getdmg(){

    }
}