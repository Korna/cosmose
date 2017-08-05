package com.swar.game.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.swar.game.Game;

/**
 * Created by Koma on 25.01.2017.
 */

public class Bullet extends Sprite {

    private float damage;
    public String name;
    private int id;
    private float speed;
    private boolean wavy;

    private float variable;

    public Bullet (Body body, int bulletIndex) {
        super(body);

        Texture tex;
        tex = Game.res.getTexture("bullet_" + String.valueOf(bulletIndex));
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


}