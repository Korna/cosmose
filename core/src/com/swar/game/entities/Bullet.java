package com.swar.game.entities;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.swar.game.Game;
import com.swar.game.Models.BulletModel;
import com.swar.game.Models.Moveable;
import com.swar.game.Types.BulletType;

import static com.swar.game.utils.constants.GAME_WIDTH;

/**
 * Created by Koma on 25.01.2017.
 */

public class Bullet extends Sprite implements Moveable{


    private float speedY = GAME_WIDTH;
    public float speedX;
    private int bdt = 0;
    public float currentSpeed = 0;

    private BulletModel bulletModel;




    public Bullet (Body body, BulletType bulletType, BulletModel bulletModel, float speedY) {
        super(body);
        this.bulletModel = bulletModel;
        this.speedY = speedY;

        Texture tex;
        tex = Game.res.getTexture(bulletType.name());
        TextureRegion[] sprites = TextureRegion.split(tex, 32, 32)[0];

        setAnimation(sprites, 1 / 12f);
    }

    @Override
    public void update(float delta){
        animation.update(delta);
        ++bdt;
        if(bulletModel.wavy) {
            bdt %= 5;
            switch (bdt) {
                case 0:
                    currentSpeed = 500;
                case 1:
                    currentSpeed = 1500;
                    break;
                case 2:
                    currentSpeed = 0;
                    break;
                case 3:
                    currentSpeed = -500;
                case 4:
                    currentSpeed = -1500;
                    break;
                case 5:
                default:
                    currentSpeed = 0;
                    break;
            }
        }else{
            if(bdt>10){
                speedY = 3000;
            }
        }

    }

    public BulletModel getBulletModel() {
        return bulletModel;
    }

    @Override
    public float getSpeed() {
        return this.speedY;
    }

    @Override
    public void setSpeed(float speed) {
        this.speedY = speed;
    }

    @Override
    public void decreaseSpeed(float speed) {

    }

    @Override
    public void increaseSpeed(float speed) {

    }
}