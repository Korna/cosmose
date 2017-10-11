package com.swar.game.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.swar.game.Game;

/**
 * Created by Koma on 11.10.2017.
 */
public class Explosion extends Sprite implements Dissapearable  {
    private float existTime = 0;

    public Explosion (Body body) {
        super(body);

        Texture tex;
        tex = Game.res.getTexture("explosion_1");
        TextureRegion[] sprites = TextureRegion.split(tex, 32, 32)[0];

        setAnimation(sprites, 1 / 12f);
    }

    public float getExistTime() {
        return existTime;
    }

    public void setExistTime(float existTime) {
        this.existTime = existTime;
    }

    @Override
    public float getMaxExistTime() {
        return 0.2f;
    }
}
