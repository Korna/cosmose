package com.swar.game.entities.Bonuses;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.swar.game.Game;
import com.swar.game.entities.Player;
import com.swar.game.entities.Sprite;

/**
 * Created by Koma on 14.10.2017.
 */
public abstract class Bonus implements Dissapearable{
    private float existTime = 0;
    TextureRegion[] sprites;
    Sprite sprite;

    Effectable effectable;

    public Bonus(String texture, Effectable effectable) {
        Texture tex;
        tex = Game.res.getTexture(texture);
        sprites = TextureRegion.split(tex, 32, 32)[0];
        this.effectable = effectable;
    }

    @Override
    public float getExistTime() {
        return existTime;
    }
    @Override
    public void setExistTime(float existTime) {
        this.existTime = existTime;
    }

    @Override
    public float getMaxExistTime() {
        return 30f;
    }

    public void pickUp(Player player){
        effectable.pickUp(player);
    }
}
