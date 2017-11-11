package com.swar.game.managers.Content;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.swar.game.Types.BonusType;
import com.swar.game.Types.BulletType;
import com.swar.game.Types.ShipType;
import com.swar.game.Types.WeaponType;

import java.util.HashMap;

/**
 * Created by Koma on 15.01.2017.
 */
public class Content {

    private ContentTexture contentTexture;
    private HashMap<String, Music> music;
    private HashMap<String, Sound> sounds;

    public Content(){
        contentTexture = new ContentTexture();

        music = new HashMap<String, Music>();
        sounds = new HashMap<String, Sound>();
    }



    public void loadTexture(String path, String key){
        contentTexture.load(path, key);
    }
    public Texture getTexture(String key) {
        return contentTexture.get(key);
    }

    public void disposeTexture(String key){
        Texture tex = contentTexture.get(key);
        if(tex != null) tex.dispose();
    }

    public void loadMusic(String path, String key) {
        Music m = Gdx.audio.newMusic(Gdx.files.internal(path));
        music.put(key, m);
    }

    public Music getMusic(String key) {
        return (Music)music.get(key);
    }

    public void removeMusic(String key) {
        Music m = (Music)music.get(key);
        if(m != null) {
            music.remove(key);
            m.dispose();
        }

    }

    public void loadSound(String path, String key) {
        Sound sound = Gdx.audio.newSound(Gdx.files.internal(path));
        sounds.put(key, sound);
    }

    public Sound getSound(String key) {
        return (Sound)sounds.get(key);
    }

    public void removeSound(String key) {
        Sound sound = (Sound)this.sounds.get(key);
        if(sound != null) {
            this.sounds.remove(key);
            sound.dispose();
        }

    }


    public void resLoader(){
        loadTexture("images/splash.png", "splash");
        loadTexture("images/background.png", "background_1");
        loadTexture("images/background_menu.png", "background_menu");
        loadTexture("images/gameHud.png", "hudCredits");

        loadTexture("images/hud.png", "hud");
        loadTexture("images/bgs.png", "bgs");

        for(WeaponType type : WeaponType.values()){
            String name = type.name();
            loadTexture("sprites/"+name+".png", name);
        }

        for(BulletType type : BulletType.values()){
            String name = type.name();
            loadTexture("sprites/" + name + ".png", name);
        }



        loadTexture("sprites/asteroid_1.png", "asteroid_1");
        loadTexture("sprites/asteroid_2.png", "asteroid_2");
        loadTexture("sprites/asteroid_3.png", "asteroid_3");
        loadTexture("sprites/enemy.png", "enemy");


        for(BonusType type : BonusType.values()){
            String name = type.name();
            loadTexture("sprites/" + name + ".png", name);
        }

        loadTexture("sprites/explosion_1.png", "explosion_1");
        loadTexture("sprites/explosion_2.png", "explosion_2");

        for(ShipType type : ShipType.values()){
            String name = type.name();
            loadTexture("sprites/" + name + ".png", name);
            loadTexture("sprites/"+name+"_left.png", name +"_l");
            loadTexture("sprites/"+name+"_right.png", name +"_r");
        }


    }
}
