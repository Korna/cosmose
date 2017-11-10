package com.swar.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Gdx2DPixmap;
import com.swar.game.managers.Content.ContentTexture;
import com.swar.game.managers.Content.IContent;
import org.junit.Test;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;

/**
 * Created by Koma on 01.11.2017.
 */
public class ContentTextureTest {
    /*
    static{
        ApplicationListener applicationListener = new Game(true, true, 0f);
        AndroidLauncher androidLauncher = new AndroidLauncher();
        androidLauncher.initialize(applicationListener);

        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = GAME_NAME;
        config.width = 640;
        config.height = 960;
        config.resizable = false;
        LwjglApplication application = new LwjglApplication(new Game(false, false, 0), config);

    }*/

    @Test
    public void get() throws Exception {
        IContent<Texture> content = new ContentTexture();
        Texture texture = content.get("Null");
        assertNull(texture);

    }

    @Test
    public void load() throws Exception {
        IContent<Texture> content = new ContentTexture();


        content.load("data/sprites/explosion_1.png","key");
        Texture texture = content.get("key");
        assertNotNull(texture);
    }



    @Test
    public void load3() throws Exception {
        IContent<Texture> content = new ContentTexture();
        String path = "sprites/explosion_1.png";
        content.load(path,"key");

        Texture texture = new Texture(path);
        assertNotNull(texture);
    }

    @Test
    public void load2() throws Exception {
        IContent<Texture> content = new ContentTexture();
        String path = "data/sprites/explosion_1.png";
        content.load(path,"key");

        Texture texture = new Texture(Gdx.files.internal(path));
        assertNotNull(texture);
    }

    private void test(){
        Gdx2DPixmap gdx2DPixmap = new Gdx2DPixmap(32, 32 ,1);
        Pixmap pixmap = new Pixmap(gdx2DPixmap);
        pixmap.drawRectangle(0, 0, 32, 32);
        Texture texture = new Texture(pixmap);

    }

}