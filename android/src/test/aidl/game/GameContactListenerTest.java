package game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.swar.game.Game;
import com.swar.game.Models.AbilityExplode;
import com.swar.game.Models.Ship;
import com.swar.game.Models.Weapon;
import com.swar.game.Types.BulletType;
import com.swar.game.entities.Bonuses.HealthBonus;
import com.swar.game.entities.Player;
import com.swar.game.managers.Content.Content;
import com.swar.game.managers.World.BodyBuilder;
import com.swar.game.managers.World.GameContactListener;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static com.swar.game.utils.constants.BULLET_DESTROYABLE;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * Created by Koma on 01.11.2017.
 */
public class GameContactListenerTest {

    static World world;
    static GameContactListener gameContactListener;
    static  BodyBuilder bodyBuilder;

    @Before
    public void beforeInit(){
        Content content = new Content();

        content.resLoader();
        Game.res = content;
        world = new World(new Vector2(0f, 0f), false);
        bodyBuilder = new BodyBuilder(world);
        Body body = bodyBuilder.createPlayer(10, 10, 32, 32);
        Ship ship = new Ship(600f, 100, 5, "ship_1", "ship_1", new float[]{1}, new float[]{1},
                3, 4,
                new ArrayList<Weapon>(), 32, 32, new AbilityExplode());

        Player player = new Player(body, ship);
         gameContactListener = new GameContactListener(player);

        world.setContactListener(gameContactListener);
    }

    @Test
    public void playerAndAsteroidContact() throws Exception {
        Body bodyAsteroid = bodyBuilder.createAsteroid(10, 10);

        int sizeBefore = gameContactListener.getBodiesToRemove().size;
        world.step(0.1f, 2, 6);
        int sizeAfter = gameContactListener.getBodiesToRemove().size;
        assertTrue(sizeBefore == 0);
        assertTrue(sizeAfter != 0);

    }

    @Test
    public void playerAndBonusContact() throws Exception {
        Body bodyBonus = bodyBuilder.createBonus(10, 10);
        bodyBonus.setUserData(new HealthBonus(bodyBonus));
        int sizeBefore = gameContactListener.getBodiesToRemove().size;
        world.step(0.1f, 2, 6);
        int sizeAfter = gameContactListener.getBodiesToRemove().size;
        assertTrue(sizeBefore == 0);
        assertTrue(sizeAfter != 0);

    }

    @Test
    public void bulletAndAsteroidContact() throws Exception {
        Body bodyAsteroid = bodyBuilder.createAsteroid(50, 50);
        Body bodyBullet = bodyBuilder.createBulletPlayer(50,50, BULLET_DESTROYABLE, null);

        int sizeBefore = gameContactListener.getBodiesToRemove().size;
        world.step(0.1f, 2, 6);
        int sizeAfter = gameContactListener.getBodiesToRemove().size;
        assertTrue(sizeBefore == 0);
        assertTrue(sizeAfter != 0);

    }

    @Test
    public void bulletAndEnemyContact() throws Exception {
        Body bodyEnemy = bodyBuilder.createEnemy(90,90);
        Body bodyBullet = bodyBuilder.createBulletPlayer(90,90, BULLET_DESTROYABLE, null);

        int sizeBefore = gameContactListener.getBodiesToRemove().size;
        world.step(0.1f, 2, 6);
        int sizeAfter = gameContactListener.getBodiesToRemove().size;
        assertTrue(sizeBefore == 0);
        assertTrue(sizeAfter != 0);

    }

    @Test
    public void enemyAndAsteroidNoContact() throws Exception {
        Body bodyEnemy = bodyBuilder.createEnemy(90,90);
        Body bodyAsteroid = bodyBuilder.createAsteroid(90, 90);

        int sizeBefore = gameContactListener.getBodiesToRemove().size;
        world.step(0.1f, 2, 6);
        int sizeAfter = gameContactListener.getBodiesToRemove().size;
        assertTrue(sizeBefore == 0);
        assertTrue(sizeAfter == 0);

    }

    @Test
    public void enemyAndEnemyBulletNoContact() throws Exception {
        Body bodyEnemy = bodyBuilder.createEnemy(90,90);
        Body bodyAsteroid = bodyBuilder.createBulletEnemy(90, 90);

        int sizeBefore = gameContactListener.getBodiesToRemove().size;
        world.step(0.1f, 2, 6);
        int sizeAfter = gameContactListener.getBodiesToRemove().size;
        assertTrue(sizeBefore == 0);
        assertTrue(sizeAfter == 0);
    }

    @Test
    public void beginContact() throws Exception {
        boolean error = false;
        try {
            gameContactListener.beginContact(null);
        }catch (NullPointerException npe){
            error = true;
        }
        assertTrue(error);
    }

    @Test
    public void endContact() throws Exception {
        boolean error = false;
        try {
            gameContactListener.endContact(null);
        }catch (NullPointerException npe){
            error = true;
        }
        assertTrue(error);
    }


    @Test
    public void bulletAndEnemyffect() throws Exception {
        Body bodyBlast = bodyBuilder.createBulletPlayer(20, 20, BULLET_DESTROYABLE, BulletType.bullet_2);

        int sizeBefore = gameContactListener.getBlasts().size;
        Body bodyEnemy = bodyBuilder.createEnemy(20, 20);
        world.step(0.1f, 2, 6);
        int sizeAfter = gameContactListener.getBlasts().size;
        assertTrue(sizeBefore == 0);
        assertTrue(sizeAfter != 0);
    }

    @Test
    public void clearList() throws Exception {
        Body bodyBlast = bodyBuilder.createBulletPlayer(20, 20, BULLET_DESTROYABLE, BulletType.bullet_2);
        Body bodyAsteroid = bodyBuilder.createEnemy(20, 20);
        world.step(0.1f, 2, 6);
        gameContactListener.clearList();
        int size = gameContactListener.getBlasts().size;
        assertEquals(size, 0);
    }




}