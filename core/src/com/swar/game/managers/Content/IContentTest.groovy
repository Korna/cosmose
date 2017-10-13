package com.swar.game.managers.Content

import com.badlogic.gdx.graphics.Texture

/**
 * Created by Koma on 13.10.2017.
 */
class IContentTest extends GroovyTestCase {
    IContent<Texture> iContent = new ContentTexture();
    
    void testGet() {
        Texture result = iContent.get("NotFound");
        assertEquals(result, null);
    }

    void testLoad() {
    }
}
