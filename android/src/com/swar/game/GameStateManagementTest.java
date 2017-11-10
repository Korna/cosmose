package com.swar.game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.swar.game.Types.State;
import com.swar.game.managers.Content.Content;
import com.swar.game.managers.GameStateManagement;
import com.swar.game.managers.World.BodyBuilder;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * Created by Koma on 01.11.2017.
 */
public class GameStateManagementTest {
    Game app = null;
    GameStateManagement gameStateManagement = null;

    @Before
    public void before(){
        app = new Game(true, true, 0f);
        gameStateManagement = new GameStateManagement(app);
    }

    @Test
    public void application() throws Exception {
        assertEquals(app, gameStateManagement.application());
    }

    @Test
    public void update() throws Exception {
        app = new Game(true, true, 0f);
        Content content = new Content();
        content.resLoader();
        Game.res = content;

        gameStateManagement = new GameStateManagement(app);

        gameStateManagement.world = new World(new Vector2(-10, -10), false);

        gameStateManagement.setState(State.PLAY);

        Body body = buildObject(gameStateManagement.world);
        Vector2 pos = body.getPosition();
        float x_b = pos.x;
        float y_b = pos.y;

        Thread.sleep(10);
        pos = body.getPosition();
        assertEquals(new Vector2(x_b, y_b), pos);

        gameStateManagement.update(0.1f);
        pos = body.getPosition();
        assertTrue(pos.x != x_b);
        assertTrue(pos.y != y_b);
    }

    @Test
    public void render() throws Exception {
    }

    @Test
    public void dispose() throws Exception {
    }

    @Test
    public void resize() throws Exception {
    }

    @Test
    public void setState() throws Exception {
    }

    private Body buildObject(World world){
        BodyBuilder builder = new BodyBuilder(world);
        return builder.createBonus(10, 10);
    }
}