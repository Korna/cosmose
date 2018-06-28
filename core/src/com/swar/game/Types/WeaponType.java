package com.swar.game.Types;

import com.swar.game.Models.Weapon;

/**
 * Created by Koma on 03.10.2017.
 */
public enum WeaponType {
    weapon_1, weapon_2, weapon_3, weapon_4, weapon_5;


    public static Weapon getWeapon(WeaponType weaponType){
        Weapon weapon = null;

        switch(weaponType){
            case weapon_1:
                weapon = new Weapon(5, BulletType.getbullet(BulletType.bullet_1), 0.9f, 0.0f);
                break;
            case weapon_2:
                weapon = new Weapon(2, BulletType.getbullet(BulletType.bullet_2), 0.1f, 0.0f);
                break;
            case weapon_3:
                weapon = new Weapon(10, BulletType.getbullet(BulletType.bullet_3), 0.75f, 0.0f);
                break;
            case weapon_4:
                weapon = new Weapon(1, BulletType.getbullet(BulletType.bullet_4), 0.075f, 0.0f);
                break;
            case weapon_5:
                weapon = new Weapon(5, BulletType.getbullet(BulletType.bullet_5), 1.5f, 0.0f);
                break;
        }

        return weapon;
    }
}
