package com.swar.game.utils;

/**
 * Created by Koma on 15.01.2017.
 */
public class constants {

    public static final float PPM = 32;
    public static final int GAME_WIDTH = 640;
    public static final int GAME_HEIGHT = 960;

    public static final float STEP = 1 / 60f;
    public static final int BLOCK_SIZE = 32;




    public static final short BIT_OBJECT = 2; // 0000 0000 0000 0001
    public static final short BIT_ENEMY = 4;  // 0000 0000 0000 0010
    public static final short BIT_PLAYER = 8; // 0000 0000 0000 0100
    public static final short BIT_BULLET = 16; // 0000 0000 0000 1000
    public static final short BIT_BORDER = 32; // 0000 0000 0000 1000
}
