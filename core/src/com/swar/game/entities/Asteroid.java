package com.swar.game.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.swar.game.Game;
import com.swar.game.Randomizer;

/**
 * Created by Koma on 19.01.2017.
 */
public class Asteroid extends Sprite {
    public int hp = 100;
    public float speed = 50;

    public Asteroid (Body body) {
        super(body);

        Texture tex;
        tex = Game.res.getTexture("asteroid_" + String.valueOf(Randomizer.getAsteroidTexture()));
        TextureRegion[] sprites = TextureRegion.split(tex, 32, 32)[0];

        setAnimation(sprites, 1 / 12f);
    }
}

