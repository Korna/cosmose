package com.swar.game.managers.Content;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import java.util.HashMap;

/**
 * Created by Koma on 13.10.2017.
 */
public class ContentTexture implements IContent<Texture>{
    private HashMap<String, Texture> textures = new HashMap<>();

    @Override
    public Texture get(String key) {
            return textures.get(key);

    }

    @Override
    public boolean load(String path, String key) {
        Texture tex = null;
        try {
            tex = new Texture(Gdx.files.internal(path));
        }catch(Exception e){
            return false;
        }
        textures.put(key, tex);
        return true;
    }
}
