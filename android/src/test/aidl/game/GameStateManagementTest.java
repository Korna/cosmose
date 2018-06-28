package game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.swar.game.Game;
import com.swar.game.Models.AbilityExplode;
import com.swar.game.Models.Ship;
import com.swar.game.Models.Weapon;
import com.swar.game.Types.State;
import com.swar.game.entities.Player;
import com.swar.game.managers.Content.Content;
import com.swar.game.managers.GameStateManagement;
import com.swar.game.managers.World.BodyBuilder;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

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

        Content content = new Content();
        content.resLoader();
        Game.res = content;

        gameStateManagement = new GameStateManagement(app);
        gameStateManagement.world = new World(new Vector2(-10, -10), false);
        BodyBuilder bodyBuilder = new BodyBuilder(gameStateManagement.world);
        Body body = bodyBuilder.createPlayer(10, 10, 32, 32);

        Ship ship = new Ship(600f, 100, 5, "ship_1", "ship_1", new float[]{1}, new float[]{1},
                3, 4,
                new ArrayList<Weapon>(), 32, 32, new AbilityExplode());
        Player player = new Player(body, ship);
        gameStateManagement.player = player;
    }

    @Test
    public void testInitialization() throws Exception {
        assertEquals(app, gameStateManagement.application());
    }

    @Test
    public void testUpdateWorldForSingleObject() throws Exception {
        gameStateManagement.setState(State.PLAY_CLASSIC);

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
    public void testUpdateWorldForMultipleObjects() throws Exception {
        gameStateManagement.setState(State.PLAY_CLASSIC);

        Body body = buildObject(gameStateManagement.world);
        Vector2 pos = body.getPosition();
        float x_b = pos.x;
        float y_b = pos.y;
        BodyBuilder builder = new BodyBuilder(gameStateManagement.world);
        for(int i = 0; i < 25; ++i){
            builder.createBonus(10 + i * 2, 10 + i*2);
            builder.createExplosion(-i, -i);
            builder.createPlayer(i*3, i*3, 32, 32);

        }

        Thread.sleep(10);
        pos = body.getPosition();
        assertEquals(new Vector2(x_b, y_b), pos);

        gameStateManagement.update(0.1f);
        pos = body.getPosition();
        assertTrue(pos.x != x_b);
        assertTrue(pos.y != y_b);
    }

    @Test
    public void dispose() throws Exception {
        gameStateManagement.dispose();
    }


    @Test
    public void setState() throws Exception {
        gameStateManagement.setState(State.PLAY_CLASSIC);
        dispose();
    }

    private Body buildObject(World world){
        BodyBuilder builder = new BodyBuilder(world);
        return builder.createBonus(10, 10);
    }
}