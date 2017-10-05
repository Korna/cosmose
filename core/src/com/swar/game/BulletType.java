package com.swar.game;

import com.swar.game.Models.BulletModel;

/**
 * Created by Koma on 03.10.2017.
 */
public enum BulletType {
    bullet_1, bullet_2, bullet_3, bullet_4;

    public static BulletModel getbullet(BulletType bulletType){
        BulletModel bullet = null;
        switch(bulletType){
            case bullet_1:
                bullet = new BulletModel(40.0f, true, true, PierceType.BulletPiercing, 2000, bulletType);
                break;
            case bullet_2:
                bullet = new BulletModel(50.0f, false, false, PierceType.BulletDestroyable, 3000, bulletType);
                break;
            case bullet_3:
                bullet = new BulletModel(100.0f, true, false, PierceType.BulletPiercing, 3000, bulletType);
                break;
            case bullet_4:
                bullet = new BulletModel(100.0f, false, false, PierceType.BulletDestroyable, 5000, bulletType);
                break;
        }

        return bullet;
    }

}
