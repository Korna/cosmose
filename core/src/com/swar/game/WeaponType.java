package com.swar.game;

import com.swar.game.Models.Weapon;

/**
 * Created by Koma on 03.10.2017.
 */
public enum WeaponType {
    weapon_1, weapon_2, weapon_3, weapon_4;


    public static Weapon getWeapon(WeaponType weaponType){
        Weapon weapon = null;

        switch(weaponType){
            case weapon_1:
                weapon = new Weapon(3000, BulletType.getbullet(BulletType.bullet_1));
                break;
            case weapon_2:
                weapon = new Weapon(4500, BulletType.getbullet(BulletType.bullet_2));
                break;
            case weapon_3:
                weapon = new Weapon(4500, BulletType.getbullet(BulletType.bullet_3));
                break;
            case weapon_4:
                weapon = new Weapon(4500, BulletType.getbullet(BulletType.bullet_4));
                break;
        }

        return weapon;
    }
}
