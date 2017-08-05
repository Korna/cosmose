package com.swar.game;

import com.swar.game.entities.Player;

/**
 * Created by Koma on 04.08.2017.
 */
public class Singleton {
    private static Singleton ourInstance = new Singleton();

    public static Singleton getInstance() {
        return ourInstance;
    }

    public Player player;

    private Singleton() {
    }


}
