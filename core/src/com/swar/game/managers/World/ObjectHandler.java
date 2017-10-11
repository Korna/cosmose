package com.swar.game.managers.World;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.swar.game.entities.*;

/**
 * Created by Koma on 02.10.2017.
 */
public class ObjectHandler {

    public Array<Asteroid> listAsteroid;
    public Array<Bullet> listBulletPlayer;
    public Array<Sprite> listDisappearable;
    public Array<Enemy> listEnemy;

    public Player player;


    private World world;

    public ObjectHandler(Array<Asteroid> listAsteroid, Array<Bullet> listBulletPlayer, Array<Sprite> listDisappearable, Array<Enemy> enemyList, World world) {
        this.listAsteroid = listAsteroid;
        this.listBulletPlayer = listBulletPlayer;
        this.listDisappearable = listDisappearable;
        this.listEnemy = enemyList;
        this.world = world;
    }

    public void clearAll(){

        for(Asteroid asteroid : listAsteroid)
            world.destroyBody(asteroid.getBody());
        for(Bullet bullet : listBulletPlayer)
            world.destroyBody(bullet.getBody());
        for(Sprite bonus : listDisappearable)
            world.destroyBody(bonus.getBody());
        for(Enemy enemy : listEnemy)
            world.destroyBody(enemy.getBody());

        listDisappearable.clear();
        listBulletPlayer.clear();
        listAsteroid.clear();
        listEnemy.clear();

    }

    public void add(Asteroid a){
        this.listAsteroid.add(a);
    }

    public void add(Bullet b){
        this.listBulletPlayer.add(b);
    }
    public void add(Bonus b){
        this.listDisappearable.add(b);
    }
    public void add(Enemy e){
        this.listEnemy.add(e);
    }
    public void add(Explosion e){
        this.listDisappearable.add(e);
    }



    public void remove(Asteroid a){
        listAsteroid.removeValue(a, true);
    }
    public void remove(Bonus b){
        listDisappearable.removeValue(b, true);
    }
    public void remove(Bullet b){
        listBulletPlayer.removeValue(b, true);
    }
    public void remove(Enemy e){
        listEnemy.removeValue(e, true);
    }

    public void render(SpriteBatch batch){
        for(Asteroid asteroid : listAsteroid)
            asteroid.render(batch);

        for(Sprite bonus : listDisappearable)
            bonus.render(batch);

        for(Bullet bullet : listBulletPlayer)
            bullet.render(batch);

        for(Enemy enemy : listEnemy)
            enemy.render(batch);
    }

}
