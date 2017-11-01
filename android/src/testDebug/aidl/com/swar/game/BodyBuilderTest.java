package com.swar.game;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.swar.game.Models.Moveable;
import com.swar.game.entities.Asteroid;
import com.swar.game.entities.Bonuses.Dissapearable;
import com.swar.game.entities.Bonuses.EnergyBonus;
import com.swar.game.entities.Bullet;
import com.swar.game.entities.Enemy;
import com.swar.game.entities.Explosion;
import com.swar.game.managers.Content.Content;
import com.swar.game.managers.World.BodyBuilder;
import org.junit.Before;
import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

/**
 * Created by Koma on 01.11.2017.
 */
public class BodyBuilderTest{
    static Content content = null;

    static{
        content = new Content();
        content.resLoader();
        Game.res = content;
    }

    @Before
    public void before(){
    }

    private void dynamicObject(World world, Body body, float speed){
        Array<Body> bodies = new Array<>();

        world.getBodies(bodies);

        float y = -1;
        float x = -1;

        for(Body bodyReal : bodies){
            assertEquals(body, bodyReal);

            BodyDef.BodyType bodyType = body.getType();
            assertEquals(bodyType, BodyDef.BodyType.DynamicBody);
            Vector2 vector2 = bodyReal.getPosition();

            y = vector2.y;
            x = vector2.x;

            assertEquals(vector2, body.getPosition());
        }
        for(int i = 0; i < 60; ++i)
            world.step(1.5f, 6, 2);

        world.getBodies(bodies);
        Body bodyReal = bodies.get(0);
        bodyReal.setLinearVelocity(new Vector2(0, speed));

        assertTrue(x == bodyReal.getPosition().x);
        if(speed != 0f)
            assertTrue(y != bodyReal.getPosition().y);
    }

    private void staticObject(World world, Body body){
        Array<Body> bodies = new Array<>();

        world.getBodies(bodies);

        float y = -1;
        float x = -1;

        for(Body bodyReal : bodies){
            assertEquals(body, bodyReal);

            BodyDef.BodyType bodyType = body.getType();
            assertEquals(bodyType, BodyDef.BodyType.StaticBody);
            Vector2 vector2 = bodyReal.getPosition();

            y = vector2.y;
            x = vector2.x;

            assertEquals(vector2, body.getPosition());
        }
        for(int i = 0; i < 60; ++i)
            world.step(1.5f, 6, 2);

        world.getBodies(bodies);
        Body bodyReal = bodies.get(0);


        assertTrue(x == bodyReal.getPosition().x);
        assertTrue(y == bodyReal.getPosition().y);
    }

    @Test
    public void createAsteroid() throws Exception {
        World world = new World(new Vector2(0, 0), false);
        BodyBuilder builder = new BodyBuilder(world);

        Body body = builder.createAsteroid(1, 1);
        Moveable moveable = new Asteroid(body);
        dynamicObject(world, body, moveable.getSpeed());
    }

    @Test
    public void createEnemy() throws Exception {
        World world = new World(new Vector2(0, 0), false);
        BodyBuilder builder = new BodyBuilder(world);

        Body body = builder.createEnemy(1, 1);
        Moveable moveable = new Enemy(body);
        dynamicObject(world, body, moveable.getSpeed());
    }

    @Test
    public void createBonus() throws Exception {
        World world = new World(new Vector2(0, 0), false);
        BodyBuilder builder = new BodyBuilder(world);

        Body body = builder.createBonus(1, 1);
        Dissapearable dissapearable = new EnergyBonus(body);
        dynamicObject(world, body, 0);
    }

    @Test
    public void createExplosion() throws Exception {
        World world = new World(new Vector2(0, 0), false);
        BodyBuilder builder = new BodyBuilder(world);

        Body body = builder.createExplosion(1, 1);
        Dissapearable dissapearable = new Explosion(body, 1f, "explosion");
        dynamicObject(world, body, 0);
    }

    @Test
    public void createBulletPlayer() throws Exception {
        World world = new World(new Vector2(0, 0), false);
        BodyBuilder builder = new BodyBuilder(world);

        Body body = builder.createBulletPlayer(100, 100, null, null);
        Moveable moveable = new Bullet(body, null, null, -600f);
        dynamicObject(world, body, moveable.getSpeed());
    }

    @Test
    public void createBulletEnemy() throws Exception {
        World world = new World(new Vector2(0, 0), false);
        BodyBuilder builder = new BodyBuilder(world);

        Body body = builder.createBulletEnemy(100, 100);

        dynamicObject(world, body, 600f);
    }

    @Test
    public void createShadow() throws Exception {
        World world = new World(new Vector2(0, 0), false);
        BodyBuilder builder = new BodyBuilder(world);

        Body body = builder.createShadow(333, 333, 32, 32);

        dynamicObject(world, body, 0f);
    }

    @Test
    public void createBorder() throws Exception {
        World world = new World(new Vector2(0, 0), false);
        BodyBuilder builder = new BodyBuilder(world);

        Body body = builder.createBorder(null, 320, 640, 32, 32);

        staticObject(world, body);
    }

    @Test
    public void createPlayer() throws Exception {
        World world = new World(new Vector2(0, 0), false);
        BodyBuilder builder = new BodyBuilder(world);

        Body body = builder.createPlayer(1, 1, 32, 32);

        dynamicObject(world, body, 0f);
    }

    @Test
    public void createMultipleObjects() throws Exception {
        World world = new World(new Vector2(0, 0), false);
        BodyBuilder builder = new BodyBuilder(world);

        builder.createPlayer(1, 1, 32, 32);
        builder.createBulletEnemy(1, 1);
        builder.createBonus(30, 30);
        builder.createExplosion(-2, -5);

        assertEquals(4, world.getBodyCount());
    }

}