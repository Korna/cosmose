package tests;

import com.badlogic.gdx.graphics.Texture;
import com.swar.game.managers.Content.ContentTexture;
import com.swar.game.managers.Content.IContent;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;


/**
 * Created by Koma on 13.10.2017.
 */
public class IContentTest  {
    IContent<Texture> iContent = new ContentTexture();
    @Test
    public void testGet() throws Exception {
        Texture result = iContent.get("NotFound");
        assertEquals(result, null);
    }

    public void testLoad() throws Exception {
    }

}