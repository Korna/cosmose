package com.swar.game.resources.bulletData;


/**
 * Created by Koma on 30.01.2017.
 */
public class bulletDB { // в статик
    private bullet bulletList[] = new bullet[2];

    public bulletDB(){
        bulletList[0] = new bullet(100, "bullet_1", 1, 400, false, 0, 100, true);
        bulletList[1] = new bullet(100, "bullet_2", 1, 200, false, 0, 1, false);


    }

    public bullet getBullet(int index){return bulletList[index];}
}
