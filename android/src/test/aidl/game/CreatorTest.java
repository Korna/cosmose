package game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.swar.game.Game;
import com.swar.game.Models.*;
import com.swar.game.Types.WeaponType;
import com.swar.game.entities.*;
import com.swar.game.managers.Content.Content;
import com.swar.game.managers.World.BodyBuilder;
import com.swar.game.managers.World.ObjectHandler;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * Created by Koma on 11.11.2017.
 */
public class CreatorTest {
    Creator creator;
    BodyBuilder bodyBuilder;
    ObjectHandler objectHandler;
    @Before
    public void before(){
        Content content = new Content();
        content.resLoader();
        Game.res = content;

        World world = new World(new Vector2(0, 0), false);
        bodyBuilder = new BodyBuilder(world);


        objectHandler = new ObjectHandler(new Array<Asteroid>(), new Array<Bullet>(), new Array<Sprite>(), new Array<Enemy>(), world);
    }
    @Test
    public void testPlayerNoShot(){
        //настройка игрока не имеющего снаряжения

        World world = new World(new Vector2(0, 0), false);
        Body body = bodyBuilder.createPlayer(10, 10, 32, 32);
        Ship ship = new Ship(600f, 100, 5, "ship_1", "ship_1", new float[]{1}, new float[]{1},
                3, 4,
                new ArrayList<Weapon>(), 32, 32, new AbilityExplode());
        creator = new Player(body, ship);
        objectHandler = new ObjectHandler(new Array<Asteroid>(), new Array<Bullet>(), new Array<Sprite>(), new Array<Enemy>(), world);


        int sizeBefore = objectHandler.listBulletPlayer.size;
        creator.createObject(bodyBuilder, objectHandler);
        int sizeAfter = objectHandler.listBulletPlayer.size;
        assertEquals(sizeBefore, sizeAfter);
    }

    @Test
    public void testPlayerSingleShot(){
        //настройка игрока имеющего 1 пушку
        ArrayList<Weapon> weaponArrayList = new ArrayList<>();

        World world = new World(new Vector2(0, 0), false);
        Body body = bodyBuilder.createPlayer(10, 10, 32, 32);
        Ship ship = new Ship(600f, 100, 5, "ship_1", "ship_1", new float[]{1}, new float[]{1},
                3, 4,
                weaponArrayList, 32, 32, new AbilityExplode());

        //добавляем оружие
        ship.weapons.add(WeaponType.getWeapon(WeaponType.weapon_2));

        creator = new Player(body, ship);
        objectHandler = new ObjectHandler(new Array<Asteroid>(), new Array<Bullet>(), new Array<Sprite>(), new Array<Enemy>(), world);

        ship.weapons.get(0).setTimeAfterShot(1000);//заряжаем оружие

        int sizeBefore = objectHandler.listBulletPlayer.size;
        boolean shot = creator.createObject(bodyBuilder, objectHandler);
        int sizeAfter = objectHandler.listBulletPlayer.size;
        assertTrue(shot);
        assertEquals(sizeBefore + 1, sizeAfter);
    }

    @Test
    public void testPlayerDoubleShot(){
        //настройка игрока имеющего 1 пушку
        ArrayList<Weapon> weaponArrayList = new ArrayList<>();

        World world = new World(new Vector2(0, 0), false);
        Body body = bodyBuilder.createPlayer(10, 10, 32, 32);
        Ship ship = new Ship(600f, 100, 5, "ship_1", "ship_1", new float[]{1}, new float[]{1},
                3, 4,
                weaponArrayList, 32, 32, new AbilityExplode());

        //добавляем оружие
        ship.weapons.add(WeaponType.getWeapon(WeaponType.weapon_2));
        ship.weapons.add(WeaponType.getWeapon(WeaponType.weapon_2));

        creator = new Player(body, ship);
        objectHandler = new ObjectHandler(new Array<Asteroid>(), new Array<Bullet>(), new Array<Sprite>(), new Array<Enemy>(), world);
        //заряжаем оружие
        ship.weapons.get(0).setTimeAfterShot(1000);
        ship.weapons.get(1).setTimeAfterShot(1000);

        int sizeBefore = objectHandler.listBulletPlayer.size;
        boolean shot = creator.createObject(bodyBuilder, objectHandler);
        int sizeAfter = objectHandler.listBulletPlayer.size;
        assertTrue(shot);
        assertEquals(sizeBefore + 2, sizeAfter);
    }

    @Test
    public void testEnemyShot(){
        //настройка игрока имеющего 1 пушку

        World world = new World(new Vector2(0, 0), false);
        Body body = bodyBuilder.createEnemy(10, 10);
        creator = new Enemy(body);



        objectHandler = new ObjectHandler(new Array<Asteroid>(), new Array<Bullet>(), new Array<Sprite>(), new Array<Enemy>(), world);


        int sizeBefore = objectHandler.listBulletPlayer.size;
        boolean shot = creator.createObject(bodyBuilder, objectHandler);
        int sizeAfter = objectHandler.listBulletPlayer.size;
        assertTrue(shot);
        assertEquals(sizeBefore + 1, sizeAfter);
    }

    @Test
    public void testAbilityJugg(){

        World world = new World(new Vector2(0, 0), false);
        AbilityJugg abilityJugg = new AbilityJugg();
        //настройка позиции выстрела
        abilityJugg.setPosition(new Vector2(0, 0));

        creator = abilityJugg;


        objectHandler = new ObjectHandler(new Array<Asteroid>(), new Array<Bullet>(), new Array<Sprite>(), new Array<Enemy>(), world);


        int sizeBefore = objectHandler.listBulletPlayer.size;
        boolean shot = creator.createObject(bodyBuilder, objectHandler);
        int sizeAfter = objectHandler.listBulletPlayer.size;
        assertTrue(shot);
        assertEquals(sizeBefore + 1, sizeAfter);
    }
}
