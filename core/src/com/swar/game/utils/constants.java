package com.swar.game.utils;

import com.badlogic.gdx.Gdx;

/**
 * Created by Koma on 15.01.2017.
 */
public class constants {
    public static final String GAME_NAME = "Looper";

    public static final float PPM = 32;

    public static final int GAME_WIDTH = Gdx.graphics.getWidth() / 2;

    public static final int GAME_HEIGHT = Gdx.graphics.getHeight()/ 2;

    public static final float STEP = 1 / 60f;
    public static final int BLOCK_SIZE = 32;

    public static final String BULLET_PIERCING = "BulletPiercing";
    public static final String BULLET_DESTROYABLE = "BulletDestroyable";
    public static final String BULLET_EXPLOSIVE = "BulletExplosive";

    public static final String BONUS = "Bonus";
    public static final String SHADOW = "Shadow";
    public static final String ASTEROID = "Asteroid";
    public static final String PLAYER_SHIP = "PlayerShip";

    public static final int VIBRATION_LONG = 30;


    public static final short BIT_OBJECT = 2; // 0000 0000 0000 0001
    public static final short BIT_ENEMY = 4;  // 0000 0000 0000 0010
    public static final short BIT_PLAYER = 8; // 0000 0000 0000 0100
    public static final short BIT_BULLET = 16; // 0000 0000 0000 1000
    public static final short BIT_BORDER = 32; // 0000 0000 0000 1000
    public static final short BIT_SHADOW = 64; // 0000 0000 0001 0000
}
