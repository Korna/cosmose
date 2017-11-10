package com.swar.game.entities.Bonuses;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.swar.game.Game;
import com.swar.game.entities.Player;
import com.swar.game.entities.Sprite;

/**
 * Created by Koma on 12.10.2017.
 */
public class HealthBonus  extends Sprite implements Dissapearable, Effectable {
    private float existTime = 0;

    public HealthBonus(Body body) {
        super(body);

        Texture tex;
        tex = Game.res.getTexture("bonus_2");
        try {
            TextureRegion[] sprites = TextureRegion.split(tex, 32, 32)[0];

            setAnimation(sprites, 1 / 12f);
        }catch(NullPointerException npe){
            System.out.println(npe.toString());
        } catch(IllegalArgumentException iae){
            System.out.println(iae.toString());
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
        float hp = player.ship.getHp() + 10f;
        player.ship.setHp(hp);
    }
}
