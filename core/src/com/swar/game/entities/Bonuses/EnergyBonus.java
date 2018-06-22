package com.swar.game.entities.Bonuses;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.swar.game.Game;
import com.swar.game.entities.Player;
import com.swar.game.entities.Sprite;

/**
 * Created by Koma on 03.09.2017.
 */
public class EnergyBonus extends Sprite implements Dissapearable, Effectable {
    private float existTime = 0;

    public EnergyBonus(Body body) {
        super(body);

        Texture tex;
        tex = Game.res.getTexture("bonus_1");
        try {
            TextureRegion[] sprites = TextureRegion.split(tex, 32, 32)[0];

            setAnimation(sprites, 1 / 12f);
        }catch(IllegalArgumentException iae){

        }catch(NullPointerException npe){

        }

    }

    public float getExistTime() {
        return existTime;
    }

    public void setExistTime(float existTime) {
        this.existTime = existTime;
    }

    @Override
    public float getMaxExistTime() {
        return 30f;
    }

    @Override
    public void pickUp(Player player) {
        int energy = player.ship.getEnergy() + 25;
        player.ship.setEnergy(energy);
    }
}
