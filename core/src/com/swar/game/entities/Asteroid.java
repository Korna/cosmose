package com.swar.game.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.swar.game.Game;
import com.swar.game.utils.Randomizer;

import static com.swar.game.utils.constants.GAME_WIDTH;

/**
 * Created by Koma on 19.01.2017.
 */
public class Asteroid extends Sprite {
    public float hp = 100;
    public float speed = -(GAME_WIDTH);

    public Asteroid (Body body) {
        super(body);

        Texture tex;
        tex = Game.res.getTexture("asteroid_" + String.valueOf(Randomizer.getAsteroidTexture()));
        TextureRegion[] sprites = TextureRegion.split(tex, 32, 32)[0];

        setAnimation(sprites, 1 / 12f);
    }

    public void setHp(float hp) {
        this.hp = hp;
    }

    public float getHp() {
        return hp;
    }
}

