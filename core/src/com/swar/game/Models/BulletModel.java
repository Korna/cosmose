package com.swar.game.Models;

import com.swar.game.Types.BulletType;
import com.swar.game.Types.PierceType;

/**
 * Created by Koma on 03.10.2017.
 */
public class BulletModel {
    private float damage;
    public boolean pierce;
    public boolean wavy;
    public PierceType pierceType;
    private float speedY;
    public BulletType bulletType;

    public BulletModel(float damage, boolean pierce, boolean wavy, PierceType pierceType, float speedY, BulletType bulletType) {
        this.damage = damage;
        this.pierce = pierce;
        this.wavy = wavy;
        this.pierceType = pierceType;
        this.speedY = speedY;
        this.bulletType = bulletType;
    }

    public float getDamage() {
        return damage;
    }
}
