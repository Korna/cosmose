package com.swar.game.resources.weaponData;

import com.swar.game.entities.Weapon;
import com.swar.game.resources.bulletData.bulletDB;
import com.swar.game.Game;

/**
 * Created by Koma on 30.01.2017.
 */
public class weaponDB{// в статик переделать

    private Weapon weaponList[] = new Weapon[3];

    public weaponDB(){
        weaponList[0] = new Weapon(5, true, 100, "bullet_1", "weapon_1", 1,
                Game.gameBullets.getBullet(0)).get();

        weaponList[1] = new Weapon(10, false, 1, "bullet_2", "weapon_2", 1, Game.gameBullets.getBullet(1)).get();


    }

    public Weapon getWeapon(int index){return weaponList[index];}
}
