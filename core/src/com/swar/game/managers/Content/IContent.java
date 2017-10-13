package com.swar.game.managers.Content;

/**
 * Created by Koma on 13.10.2017.
 */
public interface IContent<A>{

    public A get(String key);
    public boolean load(String path, String key);
}
