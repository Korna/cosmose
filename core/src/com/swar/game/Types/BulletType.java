package com.swar.game.Types;

import com.swar.game.Models.BulletModel;

/**
 * Created by Koma on 03.10.2017.
 */
public enum BulletType {
    bullet_1, bullet_2, bullet_3, bullet_4, bullet_5,
    bullet_6

    ;

    public static BulletModel getbullet(BulletType bulletType){
        BulletModel bullet = null;
        switch(bulletType){
            case bullet_1:
                bullet = new BulletModel(100.0f, true, true, PierceType.BulletPiercing, 5000, bulletType);
                break;
            case bullet_2:
                bullet = new BulletModel(50.0f, false, false, PierceType.BulletDestroyable, 3000, bulletType);
                break;
            case bullet_3:
                bullet = new BulletModel(100.0f, true, false, PierceType.BulletPiercing, 3000, bulletType);
                break;
            case bullet_4:
                bullet = new BulletModel(25.0f, false, false, PierceType.BulletDestroyable, 5000, bulletType);
                break;
            case bullet_5:
                bullet = new BulletModel(100.0f, false, false, PierceType.BulletExplosive, 3000, bulletType);
                break;
            case bullet_6:
                bullet = new BulletModel(200.0f, true, true, PierceType.BulletPiercing, 5000, bulletType);
                break;

        }

        return bullet;
    }

    public static float[] getSize(BulletType bulletType){
        switch (bulletType){
            case bullet_1:
            case bullet_2:
            case bullet_3:
            case bullet_4:
            case bullet_5:
                return new float[] {32,32};
            case bullet_6:
                return new float[] {320,10};
        }
        return null;

   }

}
