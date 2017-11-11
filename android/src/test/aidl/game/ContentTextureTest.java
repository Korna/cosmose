package game;

import com.badlogic.gdx.graphics.Texture;
import com.swar.game.managers.Content.ContentTexture;
import com.swar.game.managers.Content.IContent;
import org.junit.Test;

import static junit.framework.Assert.assertNull;

/**
 * Created by Koma on 01.11.2017.
 */
public class ContentTextureTest {


    @Test
    public void get() throws Exception {
        IContent<Texture> content = new ContentTexture();
        Texture texture = content.get("Null");
        assertNull(texture);

    }

    @Test
    public void load() throws Exception {
        IContent<Texture> content = new ContentTexture();
        content.load("sprites/null","key");
        Texture texture = content.get("key");
        assertNull(texture);
    }

}