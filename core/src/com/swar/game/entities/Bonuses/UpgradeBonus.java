package com.swar.game.entities.Bonuses;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.swar.game.Game;
import com.swar.game.Models.Weapon;
import com.swar.game.entities.Player;
import com.swar.game.entities.Sprite;

/**
 * Created by Koma on 14.10.2017.
 */
public class UpgradeBonus extends Sprite implements Dissapearable, Effectable {
    private float existTime = 0;

    public UpgradeBonus(Body body){
        super(body);

        Texture tex;
        tex = Game.res.getTexture("bonus_3");
        TextureRegion[] sprites = TextureRegion.split(tex, 32, 32)[0];

        setAnimation(sprites, 1 / 12f);
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

    @Override
    public void pickUp(Player player) {
        for(Weapon weapon : player.ship.weapons) {
            weapon.setReload(weapon.getReload() * 0.95f);
            weapon.setEnergyCost(weapon.getEnergyCost() * 0.9f);
        }
        player.addToken();
    }
}
