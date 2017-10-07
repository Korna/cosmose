package com.swar.game.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.swar.game.Game;
import com.swar.game.Models.Killable;
import com.swar.game.Models.Moveable;
import com.swar.game.utils.Randomizer;

import static com.swar.game.utils.constants.GAME_WIDTH;

/**
 * Created by Koma on 19.01.2017.
 */
public class Asteroid extends Sprite implements Killable, Moveable{
    public float hp = 100;
    public float collisionDmg = 10;
    public float speed = -(GAME_WIDTH);

    public Asteroid (Body body) {
        super(body);

        Texture tex;
        tex = Game.res.getTexture("asteroid_" + String.valueOf(Randomizer.getAsteroidTexture()));
        TextureRegion[] sprites = TextureRegion.split(tex, 32, 32)[0];

        setAnimation(sprites, 1 / 12f);
    }


    @Override
    public float getHp() {
        return this.hp;
    }

    @Override
    public void setHp(float hp) {
        this.hp = hp;
    }

    @Override
    public void decreaseHp(float hp) {
        this.hp -= hp;
    }

    @Override
    public void increaseHp(float hp) {
        this.hp += hp;
    }

    @Override
    public float getSpeed() {
        return this.speed;
    }

    @Override
    public void setSpeed(float speed) {
        this.speed = speed;
    }

    @Override
    public void decreaseSpeed(float speed) {
        this.speed -= speed;
    }

    @Override
    public void increaseSpeed(float speed) {
        this.speed += speed;
    }
}

